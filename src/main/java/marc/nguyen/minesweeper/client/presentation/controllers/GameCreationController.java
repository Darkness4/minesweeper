package marc.nguyen.minesweeper.client.presentation.controllers;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.swing.DefaultListModel;
import javax.swing.JSpinner;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import marc.nguyen.minesweeper.client.core.IO;
import marc.nguyen.minesweeper.client.di.components.GameComponent.Builder;
import marc.nguyen.minesweeper.client.domain.entities.GameMode;
import marc.nguyen.minesweeper.client.domain.usecases.Connect;
import marc.nguyen.minesweeper.client.domain.usecases.DeleteSettings;
import marc.nguyen.minesweeper.client.domain.usecases.FetchAllSettingsName;
import marc.nguyen.minesweeper.client.domain.usecases.LoadSettings;
import marc.nguyen.minesweeper.client.domain.usecases.Quit;
import marc.nguyen.minesweeper.client.domain.usecases.SaveSettings;
import marc.nguyen.minesweeper.client.domain.usecases.SendPlayerToServer;
import marc.nguyen.minesweeper.client.presentation.controllers.listeners.OnUpdate;
import marc.nguyen.minesweeper.client.presentation.models.GameCreationModel;
import marc.nguyen.minesweeper.client.presentation.views.GameCreationView;
import marc.nguyen.minesweeper.common.data.models.Level;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Player;
import org.jetbrains.annotations.Nullable;

/**
 * The Game Creation Controller.
 *
 * <p>Mutate the game creation model (form) and notify the game creation view (validation).
 *
 * <p>Factory should be used as the primary constructor. The Frame should assist the injection to
 * avoid using Singletons with the presentation dependencies (model and view must NOT be
 * singletons). (Note that we could have used member injection using a setter or a builder.)
 *
 * @see GameCreationModel
 * @see GameCreationView
 * @see Factory
 */
public class GameCreationController {

  private final GameCreationModel model;
  private final GameCreationView view;
  private final DefaultListModel<String> listModel;
  private final Lazy<Connect> connect;
  private final Lazy<LoadSettings> loadSettings;
  private final Lazy<SaveSettings> saveSettings;
  private final Lazy<DeleteSettings> deleteSettings;
  private final Lazy<FetchAllSettingsName> fetchAllSettingsName;
  private final Lazy<SendPlayerToServer> sendPlayerToServerLazy;
  private final Lazy<Quit> quit;
  private final Provider<Builder> gameComponentProvider;
  private @Nullable Disposable fetchAllSettingsNameListener;
  private @Nullable Disposable startGameListener;

  public GameCreationController(
      Lazy<Connect> connect,
      Lazy<LoadSettings> loadSettings,
      Lazy<SaveSettings> saveSettings,
      Lazy<DeleteSettings> deleteSettings,
      Lazy<FetchAllSettingsName> fetchAllSettingsName,
      Lazy<Quit> quit,
      Lazy<SendPlayerToServer> sendPlayerToServerLazy,
      Provider<Builder> gameComponentProvider,
      GameCreationModel model,
      GameCreationView view) {
    this.model = model;
    this.view = view;
    this.connect = connect;
    this.loadSettings = loadSettings;
    this.saveSettings = saveSettings;
    this.deleteSettings = deleteSettings;
    this.fetchAllSettingsName = fetchAllSettingsName;
    this.quit = quit;
    this.gameComponentProvider = gameComponentProvider;
    this.sendPlayerToServerLazy = sendPlayerToServerLazy;

    listModel = new DefaultListModel<>();
    this.view.savedSettingsPanel.settingsList.setModel(listModel);

    /* Player JTextView */
    this.view
        .editSettingsPanel
        .playerTextField
        .getDocument()
        .addDocumentListener(new OnUpdate(this::onPlayerNameInput));

    /* Game Settings Panel */
    this.view.editSettingsPanel.gameSettingsPanel.heightSpinner.addChangeListener(
        (e) -> {
          final JSpinner source = (JSpinner) e.getSource();
          model.setHeight((Integer) source.getValue());
        });

    this.view.editSettingsPanel.gameSettingsPanel.lengthSpinner.addChangeListener(
        (e) -> {
          final JSpinner source = (JSpinner) e.getSource();
          model.setLength((Integer) source.getValue());
        });
    this.view.editSettingsPanel.gameSettingsPanel.minesSpinner.addChangeListener(
        (e) -> {
          final JSpinner source = (JSpinner) e.getSource();
          model.setMines((Integer) source.getValue());
        });
    this.view.editSettingsPanel.networkSettingsPanel.portSpinner.addChangeListener(
        (e) -> {
          final JSpinner source = (JSpinner) e.getSource();
          model.setPort((Integer) source.getValue());
        });
    this.view.editSettingsPanel.gameSettingsPanel.levelComboBox.addItemListener(
        this::levelStateChanged);
    this.view.editSettingsPanel.gameModeComboBox.addItemListener(this::gameModeStateChanged);
    this.view.editSettingsPanel.startButton.addActionListener((e) -> onStartButtonPushed());
    this.view
        .editSettingsPanel
        .networkSettingsPanel
        .ipTextField
        .getDocument()
        .addDocumentListener(new OnUpdate(this::onIpAddressInput));
    this.view
        .savedSettingsPanel
        .settingsNameTextField
        .getDocument()
        .addDocumentListener(new OnUpdate(this::onNameSettingsInput));

    /* Saved Settings Panel */
    this.view.savedSettingsPanel.settingsList.addListSelectionListener(
        (e) -> {
          final var value = this.view.savedSettingsPanel.settingsList.getSelectedValue();
          if (value != null) {
            this.model.setSettingsName(value);
            SwingUtilities.invokeLater(
                () -> this.view.savedSettingsPanel.settingsNameTextField.setText(value));
          }
        });
    this.view.savedSettingsPanel.saveButton.addActionListener(this::onSaveSettingsPushed);
    this.view.savedSettingsPanel.loadButton.addActionListener(this::onLoadSettingsPushed);
    this.view.savedSettingsPanel.deleteButton.addActionListener(this::onDeleteSettingsPushed);

    fetchAllSettingsNameListener =
        this.fetchAllSettingsName
            .get()
            .execute(null)
            .subscribe(
                (settings) -> settings.forEach(listModel::addElement),
                t -> {
                  System.err.println("[ERROR] fetchAllSettingsName error.");
                  t.printStackTrace();
                });

    this.view.editSettingsPanel.networkSettingsPanel.ipTextField.setText(this.model.getAddress());
    this.view.savedSettingsPanel.settingsNameTextField.setText(this.model.getSettingsName());
  }

  /** Closes every listeners */
  public void dispose() {
    if (fetchAllSettingsNameListener != null) {
      fetchAllSettingsNameListener.dispose();
      fetchAllSettingsNameListener = null;
    }
    if (startGameListener != null) {
      startGameListener.dispose();
      startGameListener = null;
    }
  }

  private void onDeleteSettingsPushed(ActionEvent e) {
    deleteSettings
        .get()
        .execute(model.getSettingsName())
        .doOnError(
            t -> {
              System.err.println("[ERROR] deleteSettings error.");
              t.printStackTrace();
            })
        .doFinally(this::refreshListModel)
        .subscribe();
  }

  private void onSaveSettingsPushed(ActionEvent e) {
    try {
      saveSettings
          .get()
          .execute(model.toEntity())
          .doOnError(
              t -> {
                System.err.println("[ERROR] saveSettings error.");
                t.printStackTrace();
              })
          .doFinally(this::refreshListModel)
          .subscribe();
    } catch (UnknownHostException unknownHostException) {
      view.invokeErrorDialog(
          String.format("Error: Incorrect Internet Address %s", model.getAddress()));
    }
  }

  /**
   * This refreshes the list.
   *
   * <p>This is called in any CRUD operation around the list (besides Read).
   */
  private void refreshListModel() {
    if (fetchAllSettingsNameListener != null) {
      fetchAllSettingsNameListener.dispose();
    }
    fetchAllSettingsNameListener =
        fetchAllSettingsName
            .get()
            .execute(null)
            .doOnError(
                t -> {
                  System.err.println("[ERROR] fetchAllSettingsName error.");
                  t.printStackTrace();
                })
            .subscribe(
                settings -> {
                  listModel.clear();
                  settings.forEach(listModel::addElement);
                },
                throwable -> System.out.println("Settings couldn't be loaded."));
  }

  private void onLoadSettingsPushed(ActionEvent e) {
    loadSettings
        .get()
        .execute(view.savedSettingsPanel.settingsNameTextField.getText())
        .subscribe(
            settings -> {
              model.fromEntity(settings);
              view.loadSettings(settings);
            },
            throwable -> System.out.println("Settings couldn't be loaded."));
  }

  /** Create or fetch a minefield and create the Game Frame. */
  private void onStartButtonPushed() {
    try {
      final var mode = model.getMode();
      if (mode == GameMode.SINGLEPLAYER) {
        final var level = model.getLevel();
        final var minefield =
            CompletableFuture.supplyAsync(
                () -> {
                  if (level == Level.CUSTOM) {
                    return new Minefield(
                        model.getLength(),
                        model.getHeight(),
                        model.getMines(),
                        model.isSinglePlayer());
                  } else {
                    return new Minefield(level, model.isSinglePlayer());
                  }
                },
                IO.executor);

        SwingUtilities.windowForComponent(view).dispose();
        SwingUtilities.invokeLater(
            () -> {
              try {
                gameComponentProvider
                    .get()
                    .minefield(minefield.get())
                    .player(new Player(model.getPlayerName()))
                    .updateTiles(Observable.empty())
                    .endGameMessages(Observable.empty())
                    .playerList(Observable.empty())
                    .build()
                    .gameFrame();
              } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
              }
            });

      } else if (mode == GameMode.MULTIPLAYER) {
        connect
            .get()
            .execute(new Connect.Params(model.getInetAddress(), model.getPort()))
            .subscribe(
                result -> {
                  final var minefield = result.initialMinefield;

                  sendPlayerToServerLazy
                      .get()
                      .execute(new Player(model.getPlayerName()))
                      .doOnError(
                          e -> {
                            System.err.println("[ERROR] sendPlayerToServer error.");
                            e.printStackTrace();
                          })
                      .subscribe();

                  // Wait for server start signal before launching the game.
                  result.startGame$.subscribe(
                      s ->
                          SwingUtilities.invokeLater(
                              () -> {
                                SwingUtilities.windowForComponent(view).dispose();
                                gameComponentProvider
                                    .get()
                                    .minefield(minefield)
                                    .player(new Player(model.getPlayerName()))
                                    .updateTiles(result.position$)
                                    .endGameMessages(result.endGameMessage$)
                                    .playerList(result.playerList$)
                                    .build()
                                    .gameFrame();
                              }),
                      t -> {
                        System.err.println("[ERROR] startGame$ error.");
                        t.printStackTrace();
                      });

                  // Launch a waiting dialog
                  this.view.invokeGameWaitingToStartDialog(
                      new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent windowEvent) {
                          quit.get()
                              .execute(null)
                              .doOnError(Throwable::printStackTrace)
                              .subscribe();
                        }
                      },
                      result.startGame$);
                },
                t -> {
                  System.err.format("[ERROR] connect error : %s.\n", t);

                  if (t instanceof IOException) {
                    this.view.invokeErrorDialog(t.toString());
                  } else if (t instanceof NullPointerException) {
                    // TODO: Better handling
                    this.view.invokeErrorDialog("Max players reached.");
                  }
                });
      }
    } catch (UnknownHostException e) {
      view.invokeErrorDialog(
          String.format("Error: Incorrect Internet Address %s", model.getAddress()));
    }
  }

  /**
   * Handles Level Combo Box events
   *
   * @param e Events
   */
  private void levelStateChanged(ItemEvent e) {
    final var item = e.getItem();
    if (item instanceof Level) {
      final Level level = (Level) item;
      model.setLevel(level);
      if (level == Level.CUSTOM) {
        view.editSettingsPanel.gameSettingsPanel.enableCustomSettings();
      } else {
        view.editSettingsPanel.gameSettingsPanel.disableCustomSettings();
        view.editSettingsPanel.gameSettingsPanel.setValueFromLevel(level);
      }
    }
  }

  /**
   * Handles GameMode Combo Box events
   *
   * @param e Events
   */
  private void gameModeStateChanged(ItemEvent e) {
    final var item = e.getItem();
    if (item instanceof GameMode) {
      final GameMode mode = (GameMode) item;
      model.setMode(mode);
      view.editSettingsPanel.changeCard(e);
    }
  }

  private void onPlayerNameInput(DocumentEvent e) {
    model.setPlayerName(view.editSettingsPanel.playerTextField.getText());
  }

  private void onIpAddressInput(DocumentEvent e) {
    model.setAddress(view.editSettingsPanel.networkSettingsPanel.ipTextField.getText());
  }

  private void onNameSettingsInput(DocumentEvent e) {
    CompletableFuture.runAsync(
        () -> {
          final var text = view.savedSettingsPanel.settingsNameTextField.getText();
          model.setSettingsName(text);
          final var isValid = model.isSettingsNameValid();
          view.savedSettingsPanel.enableLoadAndDeleteIfValid(listModel.contains(text));
          view.savedSettingsPanel.enableLoadAndDeleteIfValid(isValid);
          view.savedSettingsPanel.changeColorNameTextIfValid(isValid);
        },
        IO.executor);
  }

  /** Factory used for assisted dependencies injection. */
  public static final class Factory {

    private final Lazy<Connect> connect;
    private final Lazy<LoadSettings> loadSettings;
    private final Lazy<SaveSettings> saveSettings;
    private final Lazy<DeleteSettings> deleteSettings;
    private final Lazy<FetchAllSettingsName> fetchAllSettingsName;
    private final Lazy<Quit> quit;
    private final Lazy<SendPlayerToServer> sendPlayerToServerLazy;
    private final Provider<Builder> mainComponentProvider;

    @Inject
    public Factory(
        Lazy<Connect> connect,
        Lazy<LoadSettings> loadSettings,
        Lazy<SaveSettings> saveSettings,
        Lazy<DeleteSettings> deleteSettings,
        Lazy<FetchAllSettingsName> fetchAllSettingsName,
        Lazy<Quit> quit,
        Lazy<SendPlayerToServer> sendPlayerToServerLazy,
        Provider<Builder> mainComponentProvider) {
      this.connect = connect;
      this.loadSettings = loadSettings;
      this.saveSettings = saveSettings;
      this.deleteSettings = deleteSettings;
      this.fetchAllSettingsName = fetchAllSettingsName;
      this.quit = quit;
      this.sendPlayerToServerLazy = sendPlayerToServerLazy;
      this.mainComponentProvider = mainComponentProvider;
    }

    public GameCreationController create(GameCreationModel model, GameCreationView view) {
      return new GameCreationController(
          connect,
          loadSettings,
          saveSettings,
          deleteSettings,
          fetchAllSettingsName,
          quit,
          sendPlayerToServerLazy,
          mainComponentProvider,
          model,
          view);
    }
  }
}
