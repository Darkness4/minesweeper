package marc.nguyen.minesweeper.client.presentation.views;

import javax.inject.Inject;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.presentation.controllers.GameController;
import marc.nguyen.minesweeper.client.presentation.models.GameModel;
import marc.nguyen.minesweeper.client.presentation.utils.ResourcesLoader;
import marc.nguyen.minesweeper.client.presentation.widgets.MainMenuBar;

/** The Game Frame. */
public final class GameFrame extends JFrame {

  final GameController controller;

  @Inject
  public GameFrame(
      ResourcesLoader resourcesLoader,
      MainMenuBar mainMenuBar,
      GameView view,
      GameModel model,
      GameController.Factory gameControllerFactory) {
    assert SwingUtilities.isEventDispatchThread() : "View is running on unsafe thread!";
    controller = gameControllerFactory.create(model, view);

    setJMenuBar(mainMenuBar);
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
