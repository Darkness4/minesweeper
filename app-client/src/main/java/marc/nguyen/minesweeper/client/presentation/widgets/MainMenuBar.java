package marc.nguyen.minesweeper.client.presentation.widgets;

import dagger.Lazy;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.inject.Inject;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.di.components.DaggerLeaderboardComponent;
import marc.nguyen.minesweeper.client.domain.usecases.Quit;

public class MainMenuBar extends JMenuBar {

  public final JMenu menu;

  @Inject
  public MainMenuBar(Lazy<Quit> quit) {
    assert SwingUtilities.isEventDispatchThread() : "View is running on unsafe thread!";

    menu = new JMenu("Game");
    menu.setMnemonic(KeyEvent.VK_G);
    menu.getAccessibleContext().setAccessibleDescription("A menu.");
    menu.setToolTipText("Menu.");
    add(menu);

    final var leaderboardItem = new JMenuItem("Leaderboard", KeyEvent.VK_N);
    leaderboardItem.setAccelerator(
        KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));
    leaderboardItem.getAccessibleContext().setAccessibleDescription("The leaderboard.");
    leaderboardItem.setToolTipText("Open the leaderboard.");
    leaderboardItem.addActionListener(
        (e) -> {
          SwingUtilities.invokeLater(
              () -> DaggerLeaderboardComponent.builder().build().leaderboardDialog());
        });
    menu.add(leaderboardItem);

    menu.addSeparator();

    final var quitItem = new JMenuItem("Quit", KeyEvent.VK_Q);
    quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
    quitItem.getAccessibleContext().setAccessibleDescription("Quit the program.");
    quitItem.setToolTipText("Quit the program.");
    quitItem.addActionListener(
        (e) -> {
          quit.get().execute(null).blockingAwait(); // Will free every threads.
          SwingUtilities.windowForComponent(this).dispose();
          System.exit(0);
        });
    menu.add(quitItem);
  }
}
