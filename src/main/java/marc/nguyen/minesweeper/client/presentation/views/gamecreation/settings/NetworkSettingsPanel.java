package marc.nguyen.minesweeper.client.presentation.views.gamecreation.settings;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.presentation.utils.SpringUtilities;

public class NetworkSettingsPanel extends JPanel {

  public final JTextField ipTextField;
  public final JSpinner portSpinner;

  public NetworkSettingsPanel() {
    assert SwingUtilities.isEventDispatchThread() : "View is running on unsafe thread!";

    setLayout(new SpringLayout());
    setAlignmentX(Component.CENTER_ALIGNMENT);

    /* IP settings */
    final var ipLabel = new JLabel("IP Address");
    ipTextField = new JTextField();
    add(ipLabel);
    add(ipTextField);

    /* Port settings */
    final var portLabel = new JLabel("Port");
    portSpinner = new JSpinner(new SpinnerNumberModel(12345, 0, 65535, 1));
    add(portLabel);
    add(portSpinner);

    SpringUtilities.makeCompactGrid(this, 2, 2, 6, 6, 6, 6);
  }
}
