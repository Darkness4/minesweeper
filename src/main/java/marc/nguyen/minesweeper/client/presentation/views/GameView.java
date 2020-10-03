package marc.nguyen.minesweeper.client.presentation.views;

import java.awt.BorderLayout;
import java.lang.reflect.InvocationTargetException;
import javax.inject.Inject;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.core.mvc.View;
import marc.nguyen.minesweeper.client.presentation.utils.ResourcesLoader;
import marc.nguyen.minesweeper.client.presentation.views.game.DisplayPanel;
import marc.nguyen.minesweeper.client.presentation.views.game.GamePanel;
import marc.nguyen.minesweeper.client.presentation.views.game.PlayerListPanel;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import org.jetbrains.annotations.Nullable;

/**
 * The Game View.
 *
 * @see marc.nguyen.minesweeper.client.presentation.models.GameModel
 * @see marc.nguyen.minesweeper.client.presentation.controllers.GameController
 */
public final class GameView extends JPanel implements View {

  public final GamePanel gamePanel;
  public final DisplayPanel displayPanel;
  public @Nullable final PlayerListPanel playerListPanel;

  @Inject
  public GameView(Minefield minefield, ResourcesLoader resourcesLoader) {
    assert SwingUtilities.isEventDispatchThread() : "View is running on unsafe thread!";

    setLayout(new BorderLayout());

    displayPanel = new DisplayPanel(minefield);
    add(displayPanel, BorderLayout.PAGE_START);

    final var panel = new JPanel();
    gamePanel = new GamePanel(minefield, resourcesLoader);
    panel.add(gamePanel);
    add(panel, BorderLayout.CENTER);

    if (!minefield.isSinglePlayer) {
      playerListPanel = new PlayerListPanel();
      add(playerListPanel, BorderLayout.LINE_END);
    } else {
      playerListPanel = null;
    }
  }

  public void invokeGameEndedDialog() {
    try {
      SwingUtilities.invokeAndWait(
          () ->
              JOptionPane.showMessageDialog(
                  null, "Game ended.", "Thank you for playing !", JOptionPane.INFORMATION_MESSAGE));
    } catch (InterruptedException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  public void invokeGameEndedDialog(String message) {
    try {
      SwingUtilities.invokeAndWait(
          () ->
              JOptionPane.showMessageDialog(
                  null, message, "Thank you for playing !", JOptionPane.INFORMATION_MESSAGE));
    } catch (InterruptedException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }
}
