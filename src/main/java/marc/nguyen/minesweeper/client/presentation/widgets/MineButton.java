package marc.nguyen.minesweeper.client.presentation.widgets;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

public class MineButton extends JButton {
  public final int x;
  public final int y;

  public MineButton(int x, int y) {
    if (!SwingUtilities.isEventDispatchThread()) {
      throw new RuntimeException("View is running on unsafe thread!");
    }

    this.x = x;
    this.y = y;
  }
}
