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
import marc.nguyen.minesweeper.client.di.components.DaggerGameCreationComponent;
import marc.nguyen.minesweeper.client.domain.usecases.Quit;

public class MainMenuBar extends JMenuBar {

  public final JMenu menu;

  @Inject
  public MainMenuBar(Lazy<Quit> quit) {
    assert SwingUtilities.isEventDispatchThread() : "View is running on unsafe thread!";

    menu = new JMenu("Game");
    menu.setMnemonic(KeyEvent.VK_G);
    menu.getAccessibleContext().setAccessibleDescription("Menu for setting up a game.");
    menu.setToolTipText("Menu for setting up a game.");
    add(menu);

    final var newGameItem = new JMenuItem("New Game", KeyEvent.VK_N);
    newGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
    newGameItem.getAccessibleContext().setAccessibleDescription("Create a new game.");
    newGameItem.setToolTipText("Create a new game.");
    newGameItem.addActionListener(
        (e) -> {
          quit.get().execute(null).blockingAwait();
          SwingUtilities.windowForComponent(this).dispose();
          DaggerGameCreationComponent.builder().build().gameCreationDialog();
        });
    menu.add(newGameItem);

    menu.addSeparator();

    final var quitItem = new JMenuItem("Quit", KeyEvent.VK_Q);
    quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
    quitItem.getAccessibleContext().setAccessibleDescription("Quit the program.");
    quitItem.setToolTipText("Quit the program.");
    quitItem.addActionListener((e) -> SwingUtilities.windowForComponent(this).dispose());
    menu.add(quitItem);
  }
}
