package marc.nguyen.minesweeper.client.presentation.views;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.core.mvc.View;
import marc.nguyen.minesweeper.client.presentation.views.main.DisplayPanel;
import marc.nguyen.minesweeper.client.presentation.views.main.GamePanel;
import marc.nguyen.minesweeper.common.data.models.Minefield;

public class MainView extends JPanel implements View {
  public final GamePanel gamePanel;
  public final DisplayPanel displayPanel;

  public MainView(Minefield minefield) {
    if (!SwingUtilities.isEventDispatchThread()) {
      throw new RuntimeException("View is running on unsafe thread!");
    }

    setLayout(new BorderLayout());

    displayPanel = new DisplayPanel(minefield);
    add(displayPanel, BorderLayout.PAGE_START);

    gamePanel = new GamePanel(minefield);
    add(gamePanel, BorderLayout.CENTER);
  }
}
