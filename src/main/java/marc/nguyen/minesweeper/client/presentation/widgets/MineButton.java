package marc.nguyen.minesweeper.client.presentation.widgets;

import javax.swing.JButton;

public class MineButton extends JButton {
  public final int x;
  public final int y;

  public MineButton(int x, int y) {
    this.x = x;
    this.y = y;
  }
}
