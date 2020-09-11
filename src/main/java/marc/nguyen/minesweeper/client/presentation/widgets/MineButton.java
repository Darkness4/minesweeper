package marc.nguyen.minesweeper.client.presentation.widgets;

import java.awt.Dimension;
import java.awt.Insets;
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

    setText(" ");
    setName("Button_" + x + '_' + y);
    setPreferredSize(new Dimension(32, 32));
    setMargin(new Insets(0, 0, 0, 0));
    setFocusable(false);
  }
}
