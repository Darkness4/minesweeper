package marc.nguyen.minesweeper.client;

import javax.inject.Inject;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import marc.nguyen.minesweeper.client.di.components.DaggerMainComponent;
import marc.nguyen.minesweeper.client.presentation.controllers.MainController;
import marc.nguyen.minesweeper.client.presentation.models.MainModel;
import marc.nguyen.minesweeper.client.presentation.views.MainView;
import marc.nguyen.minesweeper.client.presentation.widgets.MainMenuBar;
import marc.nguyen.minesweeper.common.data.models.Level;
import marc.nguyen.minesweeper.common.data.models.Minefield;

public final class Client extends JFrame {
  final MainController controller;

  @Inject
  public Client(MainModel model, MainController.Factory mainControllerFactory) {
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

  public static void main(String[] args) {
    final var minefield = new Minefield(Level.HARD);
    SwingUtilities.invokeLater(
        () -> DaggerMainComponent.builder().minefield(minefield).build().client());
  }
}
