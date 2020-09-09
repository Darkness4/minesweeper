package marc.nguyen.minesweeper.client.presentation.views;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import marc.nguyen.minesweeper.client.presentation.models.MainModel;
import marc.nguyen.minesweeper.client.presentation.views.main.DisplayPanel;
import marc.nguyen.minesweeper.client.presentation.views.main.GamePanel;

public class MainView extends JPanel {

  final MainModel _model;
  public final GamePanel gamePanel;

  public MainView(MainModel model) {
    this._model = model;
    setLayout(new BorderLayout());

    add(new DisplayPanel(_model.minefield), BorderLayout.PAGE_START);
    gamePanel = new GamePanel(_model.minefield);
    add(gamePanel, BorderLayout.CENTER);
  }
}
