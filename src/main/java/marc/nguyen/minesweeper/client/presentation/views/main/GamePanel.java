package marc.nguyen.minesweeper.client.presentation.views.main;

import java.awt.GridLayout;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.presentation.utils.ResourcesLoader;
import marc.nguyen.minesweeper.client.presentation.widgets.MineButton;
import marc.nguyen.minesweeper.common.data.models.Minefield;

public class GamePanel extends JPanel {
  final Minefield _minefield;
  public final MineButton[][] mineButtons;

  public GamePanel(Minefield minefield, ResourcesLoader resourcesLoader) {
    if (!SwingUtilities.isEventDispatchThread()) {
      throw new RuntimeException("View is running on unsafe thread!");
    }

    _minefield = minefield;
    mineButtons = new MineButton[_minefield.getLength()][_minefield.getHeight()];
    final var mineButtonFactory = new MineButton.Factory(resourcesLoader);

    setLayout(new GridLayout(_minefield.getLength(), _minefield.getHeight()));

    for (int i = 0; i < _minefield.getLength(); i++) {
      for (int j = 0; j < _minefield.getHeight(); j++) {
        final var button = mineButtonFactory.create(i, j);
        add(button);
        mineButtons[i][j] = button;
      }
    }
  }

  public void addButtonListener(MouseListener listener) {
    for (MineButton[] button : mineButtons) {
      for (int j = 0; j < mineButtons[0].length; j++) {
        button[j].addMouseListener(listener);
      }
    }
  }
}
