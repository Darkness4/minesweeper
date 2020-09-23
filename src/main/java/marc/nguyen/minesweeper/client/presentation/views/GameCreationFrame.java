package marc.nguyen.minesweeper.client.presentation.views;

import javax.inject.Inject;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.presentation.controllers.GameCreationController;
import marc.nguyen.minesweeper.client.presentation.models.GameCreationModel;
import marc.nguyen.minesweeper.client.presentation.utils.ResourcesLoader;

/** The Game Creation Frame. */
public class GameCreationFrame extends JFrame {

  @Inject
  public GameCreationFrame(
      GameCreationView view,
      GameCreationModel model,
      GameCreationController.Factory gameCreationControllerFactory,
      ResourcesLoader resourcesLoader) {
    assert SwingUtilities.isEventDispatchThread() : "View is running on unsafe thread!";
    gameCreationControllerFactory.create(model, view);

    setContentPane(view);

    pack();
    setLocationRelativeTo(null); // Center of the screen
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    setIconImage(resourcesLoader.softwareLogo);
    setTitle("Minesweeper");
  }
}
