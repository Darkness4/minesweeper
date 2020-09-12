package marc.nguyen.minesweeper.client.presentation.widgets;

import javax.swing.JDialog;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;

public class Dialog extends JDialog {

  final JPanel _child;

  public Dialog(@NotNull JPanel child) {
    _child = child;
    setContentPane(_child);

    pack();
    setLocationRelativeTo(null); // Center of the screen
    setVisible(true);
  }
}
