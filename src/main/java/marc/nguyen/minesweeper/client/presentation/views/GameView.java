package marc.nguyen.minesweeper.client.presentation.views;

import java.awt.BorderLayout;
import javax.inject.Inject;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.core.mvc.View;
import marc.nguyen.minesweeper.client.presentation.utils.ResourcesLoader;
import marc.nguyen.minesweeper.client.presentation.views.game.DisplayPanel;
import marc.nguyen.minesweeper.client.presentation.views.game.GamePanel;
import marc.nguyen.minesweeper.common.data.models.Minefield;

/**
 * The Game View.
 *
 * @see marc.nguyen.minesweeper.client.presentation.models.GameModel
 * @see marc.nguyen.minesweeper.client.presentation.controllers.GameController
 */
public final class GameView extends JPanel implements View {

  public final GamePanel gamePanel;
  public final DisplayPanel displayPanel;

  @Inject
  public GameView(Minefield minefield, ResourcesLoader resourcesLoader) {
    assert SwingUtilities.isEventDispatchThread() : "View is running on unsafe thread!";

    setLayout(new BorderLayout());

    displayPanel = new DisplayPanel(minefield);
    add(displayPanel, BorderLayout.PAGE_START);

    gamePanel = new GamePanel(minefield, resourcesLoader);
    add(gamePanel, BorderLayout.CENTER);
  }
}
