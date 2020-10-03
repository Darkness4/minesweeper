package marc.nguyen.minesweeper.client.presentation.views;

import io.reactivex.rxjava3.core.Observable;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import javax.inject.Inject;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.core.mvc.View;
import marc.nguyen.minesweeper.client.domain.entities.Settings;
import marc.nguyen.minesweeper.client.presentation.views.gamecreation.EditSettingsPanel;
import marc.nguyen.minesweeper.client.presentation.views.gamecreation.SavedSettingsPanel;
import marc.nguyen.minesweeper.client.presentation.widgets.WaitingForPlayersDialog;
import marc.nguyen.minesweeper.common.data.models.StartGame;

/**
 * The Game Creation View.
 *
 * @see marc.nguyen.minesweeper.client.presentation.models.GameCreationModel
 * @see marc.nguyen.minesweeper.client.presentation.controllers.GameCreationController
 */
public final class GameCreationView extends JPanel implements View {

  public final EditSettingsPanel editSettingsPanel;
  public final SavedSettingsPanel savedSettingsPanel;

  @Inject
  public GameCreationView() {
    assert SwingUtilities.isEventDispatchThread() : "View is running on unsafe thread!";

    setLayout(new BorderLayout());

    editSettingsPanel = new EditSettingsPanel();
    add(editSettingsPanel, BorderLayout.CENTER);

    savedSettingsPanel = new SavedSettingsPanel();
    add(savedSettingsPanel, BorderLayout.EAST);
  }

  public void loadSettings(Settings settings) {
    SwingUtilities.invokeLater(
        () -> {
          savedSettingsPanel.settingsNameTextField.setText(settings.name);
          editSettingsPanel.gameModeComboBox.setSelectedItem(settings.mode);
          editSettingsPanel.networkSettingsPanel.ipTextField.setText(
              settings.address.getHostAddress());
          editSettingsPanel.networkSettingsPanel.portSpinner.setValue(settings.port);
          editSettingsPanel.gameSettingsPanel.lengthSpinner.setValue(settings.length);
          editSettingsPanel.gameSettingsPanel.heightSpinner.setValue(settings.height);
          editSettingsPanel.gameSettingsPanel.minesSpinner.setValue(settings.mines);
          editSettingsPanel.gameSettingsPanel.levelComboBox.setSelectedItem(settings.level);
          editSettingsPanel.playerTextField.setText(settings.playerName);
        });
  }

  public void invokeErrorDialog(String message) {
    SwingUtilities.invokeLater(
        () ->
            JOptionPane.showMessageDialog(
                null, message, "Error Message", JOptionPane.ERROR_MESSAGE));
  }

  /** Invoke a waiting dialog. */
  public void invokeGameWaitingToStartDialog(
      WindowAdapter windowAdapter, Observable<StartGame> startGame$) {
    SwingUtilities.invokeLater(() -> new WaitingForPlayersDialog(windowAdapter, startGame$));
  }
}
