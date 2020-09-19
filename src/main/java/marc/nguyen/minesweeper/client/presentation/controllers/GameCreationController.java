package marc.nguyen.minesweeper.client.presentation.controllers;

import dagger.Lazy;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import marc.nguyen.minesweeper.client.di.components.DaggerMainComponent;
import marc.nguyen.minesweeper.client.domain.entities.Settings;
import marc.nguyen.minesweeper.client.domain.usecases.Connect;
import marc.nguyen.minesweeper.client.domain.usecases.DeleteSettings;
import marc.nguyen.minesweeper.client.domain.usecases.FetchAllSettingsName;
import marc.nguyen.minesweeper.client.domain.usecases.LoadSettings;
import marc.nguyen.minesweeper.client.domain.usecases.SaveSettings;
import marc.nguyen.minesweeper.client.presentation.models.GameCreationModel;
import marc.nguyen.minesweeper.client.presentation.views.GameCreationView;
import marc.nguyen.minesweeper.common.data.models.Level;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Player;

public class GameCreationController
    implements ActionListener,
        ItemListener,
        ChangeListener,
        DocumentListener,
        ListSelectionListener {

  private final GameCreationModel _model;
  private final GameCreationView _view;
  private final DefaultListModel<String> listModel;
  private final Lazy<Connect> _connect;
  private final Lazy<LoadSettings> _loadSettings;
  private final Lazy<SaveSettings> _saveSettings;
  private final Lazy<DeleteSettings> _deleteSettings;
  private final Lazy<FetchAllSettingsName> _fetchAllSettingsName;

  public GameCreationController(
      Lazy<Connect> connect,
      Lazy<LoadSettings> loadSettings,
      Lazy<SaveSettings> saveSettings,
      Lazy<DeleteSettings> deleteSettings,
      Lazy<FetchAllSettingsName> fetchAllSettingsName,
      GameCreationModel model,
      GameCreationView view) {
    _model = model;
    _view = view;
    _connect = connect;
    _loadSettings = loadSettings;
    _saveSettings = saveSettings;
    _deleteSettings = deleteSettings;
    _fetchAllSettingsName = fetchAllSettingsName;

    listModel = new DefaultListModel<>();
    _view.savedSettingsPanel.settingsList.setModel(listModel);
    _view.editSettingsPanel.addListener(this);
    _view.savedSettingsPanel.addListener(this);

    final List<String> settings = _fetchAllSettingsName.get().execute(null);
    settings.forEach(listModel::addElement);
    _view.editSettingsPanel.networkSettingsPanel.ipTextField.setText(_model.getAddress());
    _view.savedSettingsPanel.settingsNameTextField.setText(_model.getSettingsName());
  }

  /**
   * Handles JButtons events.
   *
   * @param e Events
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    new Thread(
            () -> {
              final String action = e.getActionCommand();

              if (action != null) {
                switch (action) {
                  case "start":
                    onStartButtonPushed();
                    break;
                  case "load_settings":
                    onLoadSettingsPushed();
                    break;
                  case "save_settings":
                    onSaveSettingsPushed();
                    break;
                  case "delete_settings":
                    onDeleteSettingsPushed();
                    break;
                }
              }
            })
        .start();
  }

  private void onDeleteSettingsPushed() {
    _deleteSettings.get().execute(_model.getSettingsName());
    refreshListModel();
  }

  private void onSaveSettingsPushed() {
    try {
      _saveSettings.get().execute(_model.toEntity());
      refreshListModel();
    } catch (UnknownHostException unknownHostException) {
      SwingUtilities.invokeLater(
          () ->
              JOptionPane.showMessageDialog(
                  null,
                  String.format("Error: Incorrect Internet Address %s", _model.getAddress()),
                  "Error Message",
                  JOptionPane.ERROR_MESSAGE));
    }
  }

  private void refreshListModel() {
    final List<String> settings = _fetchAllSettingsName.get().execute(null);
    listModel.clear();
    settings.forEach(listModel::addElement);
  }

  private void onLoadSettingsPushed() {
    final Optional<Settings> settingsOptional =
        _loadSettings.get().execute(_view.savedSettingsPanel.settingsNameTextField.getText());

    settingsOptional.ifPresentOrElse(
        (settings) -> {
          _model.setHeight(settings.height);
          _model.setLength(settings.length);
          _model.setPort(settings.port);
          _model.setMines(settings.mines);
          _model.setLevel(settings.level);
          _model.setAddress(settings.address.getHostAddress());
          SwingUtilities.invokeLater(
              () -> {
                _view.savedSettingsPanel.settingsNameTextField.setText(settings.name);
                _view.editSettingsPanel.networkSettingsPanel.ipTextField.setText(
                    settings.address.getHostAddress());
                _view.editSettingsPanel.networkSettingsPanel.portSpinner.setValue(settings.port);
                _view.editSettingsPanel.gameSettingsPanel.lengthSpinner.setValue(settings.length);
                _view.editSettingsPanel.gameSettingsPanel.heightSpinner.setValue(settings.height);
                _view.editSettingsPanel.gameSettingsPanel.minesSpinner.setValue(settings.mines);
                _view.editSettingsPanel.gameSettingsPanel.levelComboBox.setSelectedItem(
                    settings.level);
              });
        },
        () -> {
          System.out.println("Settings couldn't be loaded.");
        });
  }

  private void onStartButtonPushed() {
    // TODO: Connect
    try {
      _connect.get().execute(new Connect.Params(_model.getInetAddress(), _model.getPort()));
      final var level = _model.getLevel();

      final Minefield minefield;
      if (level == Level.CUSTOM) {
        minefield = new Minefield(_model.getLength(), _model.getHeight());
        minefield.placeMines(_model.getMines());
      } else {
        minefield = new Minefield(level);
      }

      SwingUtilities.windowForComponent(_view).dispose();
      SwingUtilities.invokeLater(
          () ->
              DaggerMainComponent.builder()
                  .minefield(minefield)
                  .player(new Player())
                  .build()
                  .mainFrame());
    } catch (UnknownHostException e) {
      SwingUtilities.invokeLater(
          () ->
              JOptionPane.showMessageDialog(
                  null,
                  String.format("Error: Incorrect Internet Address %s", _model.getAddress()),
                  "Error Message",
                  JOptionPane.ERROR_MESSAGE));
    }
  }

  /**
   * Handles Level Combo Box events
   *
   * @param e Events
   */
  @Override
  public void itemStateChanged(ItemEvent e) {
    final var item = e.getItem();
    if (item instanceof Level) {
      final Level level = (Level) item;
      _model.setLevel(level);
      if (level == Level.CUSTOM) {
        SwingUtilities.invokeLater(_view.editSettingsPanel.gameSettingsPanel::enableCustomSettings);
      } else {
        SwingUtilities.invokeLater(
            () -> {
              _view.editSettingsPanel.gameSettingsPanel.disableCustomSettings();
              _view.editSettingsPanel.gameSettingsPanel.lengthSpinner.setValue(level.length);
              _view.editSettingsPanel.gameSettingsPanel.heightSpinner.setValue(level.height);
              _view.editSettingsPanel.gameSettingsPanel.minesSpinner.setValue(level.mines);
            });
      }
    }
  }

  /**
   * Handles spinners.
   *
   * @param e Events
   */
  @Override
  public void stateChanged(ChangeEvent e) {
    final var source = e.getSource();
    if (source == _view.editSettingsPanel.gameSettingsPanel.lengthSpinner) {
      _model.setLength(
          (Integer) _view.editSettingsPanel.gameSettingsPanel.lengthSpinner.getValue());
    } else if (source == _view.editSettingsPanel.gameSettingsPanel.heightSpinner) {
      _model.setHeight(
          (Integer) _view.editSettingsPanel.gameSettingsPanel.heightSpinner.getValue());
    } else if (source == _view.editSettingsPanel.gameSettingsPanel.minesSpinner) {
      _model.setMines((Integer) _view.editSettingsPanel.gameSettingsPanel.minesSpinner.getValue());
    } else if (source == _view.editSettingsPanel.networkSettingsPanel.portSpinner) {
      _model.setPort((Integer) _view.editSettingsPanel.networkSettingsPanel.portSpinner.getValue());
    }
  }

  /**
   * Handles TextField insert events.
   *
   * @param e Events.
   */
  @Override
  public void insertUpdate(DocumentEvent e) {
    final var doc = e.getDocument();
    if (doc == _view.editSettingsPanel.networkSettingsPanel.ipTextField.getDocument()) {
      onIpAddressInput();
    } else if (doc == _view.savedSettingsPanel.settingsNameTextField.getDocument()) {
      onNameSettingsInput();
    }
  }

  /**
   * Handles TextField insert events.
   *
   * @param e Events.
   */
  @Override
  public void removeUpdate(DocumentEvent e) {
    final var doc = e.getDocument();
    if (doc == _view.editSettingsPanel.networkSettingsPanel.ipTextField.getDocument()) {
      onIpAddressInput();
    } else if (doc == _view.savedSettingsPanel.settingsNameTextField.getDocument()) {
      onNameSettingsInput();
    }
  }

  /**
   * Handles TextField insert events.
   *
   * @param e Events.
   */
  @Override
  public void changedUpdate(DocumentEvent e) {
    final var doc = e.getDocument();
    if (doc == _view.editSettingsPanel.networkSettingsPanel.ipTextField.getDocument()) {
      onIpAddressInput();
    } else if (doc == _view.savedSettingsPanel.settingsNameTextField.getDocument()) {
      onNameSettingsInput();
    }
  }

  private void onIpAddressInput() {
    _model.setAddress(_view.editSettingsPanel.networkSettingsPanel.ipTextField.getText());
  }

  private void onNameSettingsInput() {
    new Thread(
            () -> {
              final var text = _view.savedSettingsPanel.settingsNameTextField.getText();
              _model.setSettingsName(text);
              final var isValid = _model.isSettingsNameValid();
              SwingUtilities.invokeLater(
                  () -> {
                    _view.savedSettingsPanel.loadButton.setEnabled(listModel.contains(text));
                    _view.savedSettingsPanel.deleteButton.setEnabled(listModel.contains(text));
                    _view.savedSettingsPanel.saveButton.setEnabled(isValid);
                    _view.savedSettingsPanel.settingsNameTextField.setBackground(
                        isValid ? Color.WHITE : Color.RED);
                  });
            })
        .start();
  }

  @Override
  public void valueChanged(ListSelectionEvent e) {
    if (e.getSource() == _view.savedSettingsPanel.settingsList) {
      final var value = _view.savedSettingsPanel.settingsList.getSelectedValue();
      if (value != null) {
        _model.setSettingsName(value);
        SwingUtilities.invokeLater(
            () -> _view.savedSettingsPanel.settingsNameTextField.setText(value));
      }
    }
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
