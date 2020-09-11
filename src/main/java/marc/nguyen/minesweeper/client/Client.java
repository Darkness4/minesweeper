package marc.nguyen.minesweeper.client;

import com.google.inject.Guice;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import marc.nguyen.minesweeper.client.di.PresentationModule;
import marc.nguyen.minesweeper.client.presentation.controllers.MainController;
import marc.nguyen.minesweeper.client.presentation.models.MainModel;
import marc.nguyen.minesweeper.client.presentation.views.MainView;
import marc.nguyen.minesweeper.client.presentation.widgets.MainMenuBar;
import marc.nguyen.minesweeper.common.data.models.Level;
import marc.nguyen.minesweeper.common.data.models.Minefield;

public final class Client extends JFrame {
  final MainView view;
  final MainModel model;
  final MainController controller;

  private Client() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }

    final var injector = Guice.createInjector(new PresentationModule());

    final var minefield = new Minefield(Level.EASY);
    model = new MainModel(minefield);
    view = new MainView(model);
    final var mainControllerFactory = injector.getInstance(MainController.Factory.class);
    controller = mainControllerFactory.create(model, view);

    setJMenuBar(new MainMenuBar());
    setContentPane(view);

    pack();
    setLocationRelativeTo(null); // Center of the screen
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(Client::new);
  }
}
