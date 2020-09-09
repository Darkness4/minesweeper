package marc.nguyen.minesweeper.client.presentation.widgets;

import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

public class MainMenuBar extends JMenuBar {
  final JMenu menu;

  public MainMenuBar() {
    if (!SwingUtilities.isEventDispatchThread()) {
      throw new RuntimeException("View is running on unsafe thread!");
    }

    menu = new JMenu("Game");
    add(menu);

    final var menuItem = new JMenuItem("Start", KeyEvent.VK_T);
    menu.add(menuItem);
  }
}
