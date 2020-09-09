package marc.nguyen.minesweeper.client.presentation.views.main;

import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import marc.nguyen.minesweeper.common.data.models.Minefield;

public class DisplayPanel extends JPanel {
  public final Minefield minefield;

  public DisplayPanel(Minefield minefield) {
    this.minefield = minefield;

    setLayout(new FlowLayout());

    add(new JLabel(String.valueOf(minefield.countMinesOnField())));
  }
}
