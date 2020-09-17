package marc.nguyen.minesweeper.client.presentation.views.gamecreation;

import java.awt.Component;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.presentation.controllers.GameCreationController;
import marc.nguyen.minesweeper.client.presentation.views.gamecreation.settings.GameSettingsPanel;
import marc.nguyen.minesweeper.client.presentation.views.gamecreation.settings.NetworkSettingsPanel;

public class EditSettingsPanel extends JPanel {

  public final GameSettingsPanel gameSettingsPanel;
  public final NetworkSettingsPanel networkSettingsPanel;
  public final JButton startButton;

  public EditSettingsPanel() {
    if (!SwingUtilities.isEventDispatchThread()) {
      throw new RuntimeException("View is running on unsafe thread!");
    }

    final var layout = new BoxLayout(this, BoxLayout.Y_AXIS);
    setLayout(layout);

    networkSettingsPanel = new NetworkSettingsPanel();
    add(networkSettingsPanel);

    add(new JSeparator(SwingConstants.HORIZONTAL));

    gameSettingsPanel = new GameSettingsPanel();
    add(gameSettingsPanel);

    startButton = new JButton("Create game !");
    startButton.setActionCommand("start");
    startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    add(startButton);

    setOpaque(true);
  }

  public void addListener(GameCreationController listener) {
    networkSettingsPanel.addListener(listener);
    gameSettingsPanel.addListener(listener);
    startButton.addActionListener(listener);
  }
}
