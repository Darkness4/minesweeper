package marc.nguyen.minesweeper.client.presentation.views;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.inject.Inject;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.presentation.controllers.GameController;
import marc.nguyen.minesweeper.client.presentation.models.GameModel;
import marc.nguyen.minesweeper.client.presentation.utils.ResourcesLoader;
import marc.nguyen.minesweeper.client.presentation.widgets.GameMenuBar;

/** The Game Frame. */
public final class GameFrame extends JFrame {

  final GameController controller;

  @Inject
  public GameFrame(
      ResourcesLoader resourcesLoader,
      GameMenuBar gameMenuBar,
      GameView view,
      GameModel model,
      GameController.Factory gameControllerFactory) {
    assert SwingUtilities.isEventDispatchThread() : "View is running on unsafe thread!";
    controller = gameControllerFactory.create(model, view);

    addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosed(WindowEvent windowEvent) {
            controller.dispose();
          }
        });

    setJMenuBar(gameMenuBar);
    setContentPane(view);

    pack();
    setLocationRelativeTo(null); // Center of the screen
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);
    setVisible(true);
    setIconImage(resourcesLoader.softwareLogo);
    setTitle("Minesweeper");
  }
}
