package marc.nguyen.minesweeper.client;

import com.google.inject.Guice;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import marc.nguyen.minesweeper.client.di.ClientModule;
import marc.nguyen.minesweeper.client.di.PresentationModule;
import marc.nguyen.minesweeper.client.presentation.controllers.MainController;
import marc.nguyen.minesweeper.client.presentation.models.MainModel;
import marc.nguyen.minesweeper.client.presentation.views.MainView;
import marc.nguyen.minesweeper.client.presentation.widgets.MainMenuBar;
import marc.nguyen.minesweeper.common.data.models.Level;
import marc.nguyen.minesweeper.common.data.models.Minefield;

public final class Client extends JFrame {
  private Client() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Use a DI instead of factory pattern
    final var injector = Guice.createInjector(new ClientModule(), new PresentationModule());

    final var minefield = new Minefield(Level.EASY);
    final var model = new MainModel(minefield);
    final var view = new MainView(model);
    final var mainControllerFactory = injector.getInstance(MainController.Factory.class);
    final var controller = mainControllerFactory.create(model, view);

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
