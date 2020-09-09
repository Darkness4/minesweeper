package marc.nguyen.minesweeper.client.presentation.views.main;

import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.common.data.models.Minefield;

public class DisplayPanel extends JPanel {
  private final Minefield minefield;
  public final JLabel bombLeftText;

  public DisplayPanel(Minefield minefield) {
    if (!SwingUtilities.isEventDispatchThread()) {
      throw new RuntimeException("View is running on unsafe thread!");
    }

    this.minefield = minefield;

    setLayout(new FlowLayout());

    bombLeftText = new JLabel(String.valueOf(minefield.countMinesOnField()));
    add(bombLeftText);
  }
}
