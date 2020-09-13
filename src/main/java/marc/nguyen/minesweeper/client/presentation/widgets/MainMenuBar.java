package marc.nguyen.minesweeper.client.presentation.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.inject.Inject;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.di.components.DaggerGameCreationComponent;

public class MainMenuBar extends JMenuBar implements ActionListener {

  final JMenu menu;

  enum Action {
    CREATE,
    QUIT
  }

  @Inject
  public MainMenuBar() {
    if (!SwingUtilities.isEventDispatchThread()) {
      throw new RuntimeException("View is running on unsafe thread!");
    }

    menu = new JMenu("Game");
    menu.setMnemonic(KeyEvent.VK_G);
    menu.getAccessibleContext().setAccessibleDescription("Menu for setting up a game.");
    add(menu);

    final var newGameItem = new JMenuItem("New Game", KeyEvent.VK_N);
    newGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
    newGameItem.getAccessibleContext().setAccessibleDescription("Create a new game.");
    newGameItem.setActionCommand(Action.CREATE.name());
    newGameItem.addActionListener(this);
    menu.add(newGameItem);

    menu.addSeparator();

    final var quitItem = new JMenuItem("Quit", KeyEvent.VK_Q);
    quitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
    quitItem.getAccessibleContext().setAccessibleDescription("Quit the program.");
    quitItem.setActionCommand(Action.QUIT.name());
    quitItem.addActionListener(this);
    menu.add(quitItem);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    final var action = e.getActionCommand();
    if (e.getActionCommand() != null) {
      if (action.equals(Action.CREATE.name())) {
        SwingUtilities.windowForComponent(this).dispose();
        DaggerGameCreationComponent.builder().build().gameCreationDialog();
      } else if (action.equals(Action.QUIT.name())) {
        SwingUtilities.windowForComponent(this).dispose();
      }
    }
  }
}
