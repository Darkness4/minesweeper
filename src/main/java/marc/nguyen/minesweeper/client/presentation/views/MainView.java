package marc.nguyen.minesweeper.client.presentation.views;

import com.google.inject.Inject;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.core.mvc.View;
import marc.nguyen.minesweeper.client.presentation.models.MainModel;
import marc.nguyen.minesweeper.client.presentation.views.main.DisplayPanel;
import marc.nguyen.minesweeper.client.presentation.views.main.GamePanel;

public class MainView extends JPanel implements View {

  final MainModel _model;
  public final GamePanel gamePanel;
  public final DisplayPanel displayPanel;

  @Inject
  public MainView(MainModel model) {
    if (!SwingUtilities.isEventDispatchThread()) {
      throw new RuntimeException("View is running on unsafe thread!");
    }

    this._model = model;
    setLayout(new BorderLayout());

    displayPanel = new DisplayPanel(_model.minefield);
    add(displayPanel, BorderLayout.PAGE_START);

    gamePanel = new GamePanel(_model.minefield);
    add(gamePanel, BorderLayout.CENTER);
  }
}
