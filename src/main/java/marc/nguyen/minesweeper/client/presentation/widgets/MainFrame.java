package marc.nguyen.minesweeper.client.presentation.widgets;

import javax.inject.Inject;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.presentation.controllers.MainController;
import marc.nguyen.minesweeper.client.presentation.models.MainModel;
import marc.nguyen.minesweeper.client.presentation.utils.ResourcesLoader;
import marc.nguyen.minesweeper.client.presentation.views.MainView;

public final class MainFrame extends JFrame {

  final MainController controller;

  @Inject
  public MainFrame(
      ResourcesLoader resourcesLoader,
      MainMenuBar mainMenuBar,
      MainView view,
      MainModel model,
      MainController.Factory mainControllerFactory) {
    assert SwingUtilities.isEventDispatchThread() : "View is running on unsafe thread!";
    controller = mainControllerFactory.create(model, view);

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
