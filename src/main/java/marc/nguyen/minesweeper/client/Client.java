package marc.nguyen.minesweeper.client;

import com.google.inject.Guice;
import javax.swing.JFrame;
import javax.swing.UIManager;
import marc.nguyen.minesweeper.client.di.ClientModule;
import marc.nguyen.minesweeper.client.presentation.controllers.MainController;
import marc.nguyen.minesweeper.client.presentation.models.MainModel;
import marc.nguyen.minesweeper.client.presentation.views.MainView;
import marc.nguyen.minesweeper.client.presentation.widgets.MainMenuBar;
import marc.nguyen.minesweeper.common.data.models.Minefield;

public final class Client extends JFrame {
  private Client() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }

    final Minefield minefield = new Minefield(5, 5);
    minefield.placeMines(10);

    setJMenuBar(new MainMenuBar());

    final var model = new MainModel(minefield);
    final var view = new MainView(model);
    final var controller = new MainController(model, view);
    setContentPane(view);

    pack();
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }

  public static void main(String[] args) {
    final var injector = Guice.createInjector(new ClientModule());
    final JFrame frame = new Client();
  }
}
