package marc.nguyen.minesweeper.client.presentation.widgets;

import javax.inject.Inject;
import javax.swing.JFrame;
import marc.nguyen.minesweeper.client.presentation.controllers.MainController;
import marc.nguyen.minesweeper.client.presentation.models.MainModel;
import marc.nguyen.minesweeper.client.presentation.views.MainView;

public final class MainFrame extends JFrame {

  final MainController controller;

  @Inject
  public MainFrame(
      MainMenuBar mainMenuBar,
      MainView view,
      MainModel model,
      MainController.Factory mainControllerFactory) {
    controller = mainControllerFactory.create(model, view);

    setJMenuBar(mainMenuBar);
    setContentPane(view);

    pack();
    setLocationRelativeTo(null); // Center of the screen
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
}
