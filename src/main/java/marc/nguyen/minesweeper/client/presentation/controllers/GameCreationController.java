package marc.nguyen.minesweeper.client.presentation.controllers;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Observable;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
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
import marc.nguyen.minesweeper.client.domain.usecases.SaveSettings;
import marc.nguyen.minesweeper.client.presentation.controllers.listeners.OnUpdate;
import marc.nguyen.minesweeper.client.presentation.models.GameCreationModel;
import marc.nguyen.minesweeper.client.presentation.views.GameCreationView;
import marc.nguyen.minesweeper.common.data.models.Level;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Player;

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
  private final Provider<Builder> gameComponentProvider;

  public GameCreationController(
      Lazy<Connect> connect,
      Lazy<LoadSettings> loadSettings,
      Lazy<SaveSettings> saveSettings,
      Lazy<DeleteSettings> deleteSettings,
      Lazy<FetchAllSettingsName> fetchAllSettingsName,
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
    this.gameComponentProvider = gameComponentProvider;

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
          final var value = view.savedSettingsPanel.settingsList.getSelectedValue();
          if (value != null) {
            model.setSettingsName(value);
            SwingUtilities.invokeLater(
                () -> view.savedSettingsPanel.settingsNameTextField.setText(value));
          }
        });
    this.view.savedSettingsPanel.saveButton.addActionListener(this::onSaveSettingsPushed);
    this.view.savedSettingsPanel.loadButton.addActionListener(this::onLoadSettingsPushed);
    this.view.savedSettingsPanel.deleteButton.addActionListener(this::onDeleteSettingsPushed);

    this.fetchAllSettingsName
        .get()
        .execute(null)
        .subscribe((settings) -> settings.forEach(listModel::addElement));

    this.view.editSettingsPanel.networkSettingsPanel.ipTextField.setText(this.model.getAddress());
    this.view.savedSettingsPanel.settingsNameTextField.setText(this.model.getSettingsName());
  }

  private void onDeleteSettingsPushed(ActionEvent e) {
    deleteSettings.get().execute(model.getSettingsName()).subscribe(this::refreshListModel);
  }

  private void onSaveSettingsPushed(ActionEvent e) {
    try {
      saveSettings.get().execute(model.toEntity()).subscribe(this::refreshListModel);
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
    fetchAllSettingsName
        .get()
        .execute(null)
        .subscribe(
            (settings) -> {
              listModel.clear();
              settings.forEach(listModel::addElement);
            });
  }

  private void onLoadSettingsPushed(ActionEvent e) {
    loadSettings
        .get()
        .execute(view.savedSettingsPanel.settingsNameTextField.getText())
        .subscribe(
            (settings) -> {
              model.fromEntity(settings);
              view.loadSettings(settings);
            },
            (throwable) -> System.out.println("Settings couldn't be loaded."));
  }

  /** Create or fetch a minefield and create the Game Frame. */
  private void onStartButtonPushed() {
    try {
      final var mode = model.getMode();
      if (mode == GameMode.SINGLEPLAYER) {
        final var level = model.getLevel();
        final CompletableFuture<Minefield> minefield =
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
                });

        SwingUtilities.windowForComponent(view).dispose();
        SwingUtilities.invokeLater(
            () -> {
              try {
                gameComponentProvider
                    .get()
                    .minefield(minefield.get())
                    .player(new Player(model.getPlayerName()))
                    .updateTiles(Observable.empty())
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

                  SwingUtilities.windowForComponent(view).dispose();
                  SwingUtilities.invokeLater(
                      () ->
                          gameComponentProvider
                              .get()
                              .minefield(minefield)
                              .player(new Player(model.getPlayerName()))
                              .updateTiles(result.tiles)
                              .build()
                              .gameFrame());
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
    private final Provider<Builder> mainComponentProvider;

    @Inject
    public Factory(
        Lazy<Connect> connect,
        Lazy<LoadSettings> loadSettings,
        Lazy<SaveSettings> saveSettings,
        Lazy<DeleteSettings> deleteSettings,
        Lazy<FetchAllSettingsName> fetchAllSettingsName,
        Provider<Builder> mainComponentProvider) {
      this.connect = connect;
      this.loadSettings = loadSettings;
      this.saveSettings = saveSettings;
      this.deleteSettings = deleteSettings;
      this.fetchAllSettingsName = fetchAllSettingsName;
      this.mainComponentProvider = mainComponentProvider;
    }

    public GameCreationController create(GameCreationModel model, GameCreationView view) {
      return new GameCreationController(
          connect,
          loadSettings,
          saveSettings,
          deleteSettings,
          fetchAllSettingsName,
          mainComponentProvider,
          model,
          view);
    }
  }
}
