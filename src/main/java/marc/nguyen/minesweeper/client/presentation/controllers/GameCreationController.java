package marc.nguyen.minesweeper.client.presentation.controllers;

import dagger.Lazy;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javax.inject.Inject;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import marc.nguyen.minesweeper.client.core.IO;
import marc.nguyen.minesweeper.client.di.components.DaggerMainComponent;
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

// TODO : SRP is broken. Split the listener.
public class GameCreationController {

  private final GameCreationModel model;
  private final GameCreationView view;
  private final DefaultListModel<String> listModel;
  private final Lazy<Connect> connect;
  private final Lazy<LoadSettings> loadSettings;
  private final Lazy<SaveSettings> saveSettings;
  private final Lazy<DeleteSettings> deleteSettings;
  private final Lazy<FetchAllSettingsName> fetchAllSettingsName;

  public GameCreationController(
      Lazy<Connect> connect,
      Lazy<LoadSettings> loadSettings,
      Lazy<SaveSettings> saveSettings,
      Lazy<DeleteSettings> deleteSettings,
      Lazy<FetchAllSettingsName> fetchAllSettingsName,
      GameCreationModel model,
      GameCreationView view) {
    this.model = model;
    this.view = view;
    this.connect = connect;
    this.loadSettings = loadSettings;
    this.saveSettings = saveSettings;
    this.deleteSettings = deleteSettings;
    this.fetchAllSettingsName = fetchAllSettingsName;

    listModel = new DefaultListModel<>();
    this.view.savedSettingsPanel.settingsList.setModel(listModel);

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
      SwingUtilities.invokeLater(
          () ->
              JOptionPane.showMessageDialog(
                  null,
                  String.format("Error: Incorrect Internet Address %s", model.getAddress()),
                  "Error Message",
                  JOptionPane.ERROR_MESSAGE));
    }
  }

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
              SwingUtilities.invokeLater(() -> view.loadSettings(settings));
            },
            (throwable) -> System.out.println("Settings couldn't be loaded."));
  }

  private void onStartButtonPushed() {
    try {
      connect
          .get()
          .execute(new Connect.Params(model.getInetAddress(), model.getPort()))
          .subscribe(
              result -> {
                result.updates.subscribe(); // TODO
                // TODO : Start a game here

                final var level = model.getLevel();

                final var minefield =
                    CompletableFuture.supplyAsync(
                        () -> {
                          if (level == Level.CUSTOM) {
                            return new Minefield(
                                model.getLength(), model.getHeight(), model.getMines());
                          } else {
                            return new Minefield(level);
                          }
                        },
                        IO.executor);

                SwingUtilities.windowForComponent(view).dispose();
                SwingUtilities.invokeLater(
                    () -> {
                      try {
                        DaggerMainComponent.builder()
                            .minefield(minefield.get())
                            .player(new Player())
                            .build()
                            .mainFrame();
                      } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                      }
                    });
              });

    } catch (UnknownHostException e) {
      SwingUtilities.invokeLater(
          () ->
              JOptionPane.showMessageDialog(
                  null,
                  String.format("Error: Incorrect Internet Address %s", model.getAddress()),
                  "Error Message",
                  JOptionPane.ERROR_MESSAGE));
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
        SwingUtilities.invokeLater(view.editSettingsPanel.gameSettingsPanel::enableCustomSettings);
      } else {
        SwingUtilities.invokeLater(
            () -> {
              view.editSettingsPanel.gameSettingsPanel.disableCustomSettings();
              view.editSettingsPanel.gameSettingsPanel.lengthSpinner.setValue(level.length);
              view.editSettingsPanel.gameSettingsPanel.heightSpinner.setValue(level.height);
              view.editSettingsPanel.gameSettingsPanel.minesSpinner.setValue(level.mines);
            });
      }
    }
  }

  private Void onIpAddressInput(DocumentEvent e) {
    model.setAddress(view.editSettingsPanel.networkSettingsPanel.ipTextField.getText());
    return null;
  }

  private Void onNameSettingsInput(DocumentEvent e) {
    CompletableFuture.runAsync(
        () -> {
          final var text = view.savedSettingsPanel.settingsNameTextField.getText();
          model.setSettingsName(text);
          final var isValid = model.isSettingsNameValid();
          SwingUtilities.invokeLater(
              () -> {
                view.savedSettingsPanel.loadButton.setEnabled(listModel.contains(text));
                view.savedSettingsPanel.deleteButton.setEnabled(listModel.contains(text));
                view.savedSettingsPanel.saveButton.setEnabled(isValid);
                view.savedSettingsPanel.settingsNameTextField.setBackground(
                    isValid ? Color.WHITE : Color.RED);
              });
        },
        IO.executor);
    return null;
  }

  public static final class Factory {

    private final Lazy<Connect> _connect;
    private final Lazy<LoadSettings> _loadSettings;
    private final Lazy<SaveSettings> _saveSettings;
    private final Lazy<DeleteSettings> _deleteSettings;
    private final Lazy<FetchAllSettingsName> _fetchAllSettingsName;

    @Inject
    public Factory(
        Lazy<Connect> connect,
        Lazy<LoadSettings> loadSettings,
        Lazy<SaveSettings> saveSettings,
        Lazy<DeleteSettings> deleteSettings,
        Lazy<FetchAllSettingsName> fetchAllSettingsName) {
      _connect = connect;
      _loadSettings = loadSettings;
      _saveSettings = saveSettings;
      _deleteSettings = deleteSettings;
      _fetchAllSettingsName = fetchAllSettingsName;
    }

    public GameCreationController create(GameCreationModel model, GameCreationView view) {
      return new GameCreationController(
          _connect,
          _loadSettings,
          _saveSettings,
          _deleteSettings,
          _fetchAllSettingsName,
          model,
          view);
    }
  }
}
