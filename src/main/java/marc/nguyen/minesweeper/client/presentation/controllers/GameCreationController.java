package marc.nguyen.minesweeper.client.presentation.controllers;

import com.squareup.inject.assisted.Assisted;
import com.squareup.inject.assisted.AssistedInject;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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

public class GameCreationController
    implements ActionListener, ItemListener, ChangeListener, DocumentListener {

  private final GameCreationModel _model;
  private final GameCreationView _view;
  private final DefaultListModel<String> listModel;
  private final Connect _connect;
  private final LoadSettings _loadSettings;
  private final SaveSettings _saveSettings;
  private final DeleteSettings _deleteSettings;
  private final FetchAllSettingsName _fetchAllSettingsName;

  @AssistedInject
  public GameCreationController(
      Connect connect,
      LoadSettings loadSettings,
      SaveSettings saveSettings,
      DeleteSettings deleteSettings,
      @Assisted GameCreationModel model,
      @Assisted GameCreationView view,
      FetchAllSettingsName fetchAllSettingsName) {
    _model = model;
    _view = view;
    _connect = connect;
    _loadSettings = loadSettings;
    _saveSettings = saveSettings;
    _deleteSettings = deleteSettings;
    _fetchAllSettingsName = fetchAllSettingsName;

    listModel = new DefaultListModel<>();
    _view.savedSettingsPanel.settingsList.setModel(listModel);
    _view.settingsPanel.addListener(this);
    _view.savedSettingsPanel.addListener(this);

    final List<String> settings = _fetchAllSettingsName.execute(null);
    settings.forEach(listModel::addElement);
    _view.settingsPanel.ipTextField.setText(_model.getAddress());
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
                if (action.equals("start")) {
                  onStartButtonPushed();
                } else if (action.equals("load_settings")) {
                  onLoadSettingsPushed();
                } else if (action.equals("save_settings")) {
                  onSaveSettingsPushed();
                } else if (action.equals("delete_settings")) {
                  onDeleteSettingsPushed();
                }
              }
            })
        .start();
  }

  private void onDeleteSettingsPushed() {
    System.out.println("Delete");
    _deleteSettings.execute(_model.getSettingsName());
    refreshListModel();
  }

  private void onSaveSettingsPushed() {
    System.out.println("Save");
    try {
      _saveSettings.execute(
          new Settings(
              _model.getSettingsName(),
              InetAddress.getByName(_model.getAddress()),
              _model.getLength(),
              _model.getHeight(),
              _model.getMines(),
              _model.getLevel()));
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
    final List<String> settings = _fetchAllSettingsName.execute(null);
    System.out.println(settings);
    listModel.clear();
    settings.forEach(listModel::addElement);
  }

  private void onLoadSettingsPushed() {
    System.out.println("Load");
    final Settings settings =
        _loadSettings.execute(_view.savedSettingsPanel.settingsNameTextField.getText());
    _model.setHeight(settings.height);
    _model.setLength(settings.length);
    _model.setMines(settings.mines);
    _model.setLevel(settings.level);
    _model.setAddress(settings.address.getHostAddress());
    SwingUtilities.invokeLater(
        () -> {
          _view.savedSettingsPanel.settingsNameTextField.setText(settings.name);
          _view.settingsPanel.ipTextField.setText(settings.address.getHostAddress());
          _view.settingsPanel.lengthSpinner.setValue(settings.length);
          _view.settingsPanel.heightSpinner.setValue(settings.height);
          _view.settingsPanel.minesSpinner.setValue(settings.mines);
          _view.settingsPanel.levelComboBox.setSelectedItem(settings.level);
        });
  }

  private void onStartButtonPushed() {
    // TODO: Connect
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
        () -> DaggerMainComponent.builder().minefield(minefield).build().mainFrame());
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
        SwingUtilities.invokeLater(_view.settingsPanel::enableCustomSettings);
      } else {
        SwingUtilities.invokeLater(
            () -> {
              _view.settingsPanel.disableCustomSettings();
              _view.settingsPanel.lengthSpinner.setValue(level.length);
              _view.settingsPanel.heightSpinner.setValue(level.height);
              _view.settingsPanel.minesSpinner.setValue(level.mines);
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
    if (source == _view.settingsPanel.lengthSpinner) {
      _model.setLength((Integer) _view.settingsPanel.lengthSpinner.getValue());
    } else if (source == _view.settingsPanel.heightSpinner) {
      _model.setHeight((Integer) _view.settingsPanel.heightSpinner.getValue());
    } else if (source == _view.settingsPanel.minesSpinner) {
      _model.setMines((Integer) _view.settingsPanel.minesSpinner.getValue());
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
    if (doc == _view.settingsPanel.ipTextField.getDocument()) {
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
    if (doc == _view.settingsPanel.ipTextField.getDocument()) {
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
    if (doc == _view.settingsPanel.ipTextField.getDocument()) {
      onIpAddressInput();
    } else if (doc == _view.savedSettingsPanel.settingsNameTextField.getDocument()) {
      onNameSettingsInput();
    }
  }

  private void onIpAddressInput() {
    new Thread(
            () -> {
              try {
                final var address =
                    InetAddress.getByName(_view.settingsPanel.ipTextField.getText());
                _model.setAddress(address.getHostAddress());
                SwingUtilities.invokeLater(
                    () -> _view.settingsPanel.ipTextField.setBackground(Color.WHITE));
              } catch (UnknownHostException unknownHostException) {
                SwingUtilities.invokeLater(
                    () -> _view.settingsPanel.ipTextField.setBackground(Color.RED));
              }
            })
        .start();
  }

  private void onNameSettingsInput() {
    new Thread(
            () -> {
              final var text = _view.savedSettingsPanel.settingsNameTextField.getText();
              if (text == null || text.isEmpty()) {
                SwingUtilities.invokeLater(
                    () -> _view.savedSettingsPanel.settingsNameTextField.setBackground(Color.RED));
              } else {
                _model.setSettingsName(_view.savedSettingsPanel.settingsNameTextField.getText());
                SwingUtilities.invokeLater(
                    () ->
                        _view.savedSettingsPanel.settingsNameTextField.setBackground(Color.WHITE));
              }
            })
        .start();
  }

  @AssistedInject.Factory
  public interface Factory {

    GameCreationController create(GameCreationModel model, GameCreationView view);
  }
}
