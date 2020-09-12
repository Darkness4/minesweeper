package marc.nguyen.minesweeper.client.presentation.widgets;

import javax.inject.Inject;
import javax.swing.JFrame;
import javax.swing.UIManager;
import marc.nguyen.minesweeper.client.presentation.controllers.MainController;
import marc.nguyen.minesweeper.client.presentation.models.MainModel;
import marc.nguyen.minesweeper.client.presentation.views.MainView;

public final class MainFrame extends JFrame {
  final MainController controller;

  @Inject
  public MainFrame(MainModel model, MainController.Factory mainControllerFactory) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }

    final MainView view = new MainView(model.minefield);
    controller = mainControllerFactory.create(model, view);

    setJMenuBar(new MainMenuBar());
    setContentPane(view);

    pack();
    setLocationRelativeTo(null); // Center of the screen
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
}
