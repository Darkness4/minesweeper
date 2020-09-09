package marc.nguyen.minesweeper.client.presentation.views.main;

import java.awt.GridLayout;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.presentation.widgets.MineButton;
import marc.nguyen.minesweeper.common.data.models.Minefield;

public class GamePanel extends JPanel {
  final Minefield _minefield;
  final JButton[][] _buttons;

  public GamePanel(Minefield minefield) {
    if (!SwingUtilities.isEventDispatchThread()) {
      throw new RuntimeException("View is running on unsafe thread!");
    }

    _minefield = minefield;

    setLayout(new GridLayout(_minefield.getLength(), _minefield.getHeight()));

    _buttons = new JButton[_minefield.getLength()][_minefield.getHeight()];

    for (int i = 0; i < _minefield.getLength(); i++) {
      for (int j = 0; j < _minefield.getHeight(); j++) {
        final var button = new MineButton(i, j);
        final String id = Integer.toString(i) + '_' + j;
        button.setText(" ");
        button.setName("Button_" + id);
        add(button);
        _buttons[i][j] = button;
      }
    }
  }

  public void addButtonListener(MouseListener listener) {
    for (JButton[] button : _buttons) {
      for (int j = 0; j < _buttons[0].length; j++) {
        button[j].addMouseListener(listener);
      }
    }
  }
}
