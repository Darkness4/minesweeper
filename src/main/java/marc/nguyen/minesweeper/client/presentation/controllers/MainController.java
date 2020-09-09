package marc.nguyen.minesweeper.client.presentation.controllers;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.presentation.models.MainModel;
import marc.nguyen.minesweeper.client.presentation.views.MainView;
import marc.nguyen.minesweeper.client.presentation.widgets.MineButton;

public class MainController implements MouseListener {
  private final MainView _view;
  private final MainModel _model;

  public MainController(MainModel model, MainView view) {
    _view = view;
    _model = model;

    _view.gamePanel.addButtonListener(this);
  }

  public void discover(int x, int y) {
    _model.minefield.expose(x, y);
  }

  public void flag(int x, int y) {
    _model.minefield.flag(x, y);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    if (SwingUtilities.isRightMouseButton(e) || e.isControlDown()) {
      final var source = e.getSource();
      if (source instanceof MineButton) {
        flag(((MineButton) source).x, ((MineButton) source).y);
        System.out.println(_model.minefield.get(((MineButton) source).x, ((MineButton) source).y));
      }
    } else if (SwingUtilities.isLeftMouseButton(e) || e.isControlDown()) {
      final var source = e.getSource();
      if (source instanceof MineButton) {
        discover(((MineButton) source).x, ((MineButton) source).y);
        System.out.println(_model.minefield.get(((MineButton) source).x, ((MineButton) source).y));
      }
    }
  }

  @Override
  public void mousePressed(MouseEvent e) {}

  @Override
  public void mouseReleased(MouseEvent e) {}

  @Override
  public void mouseEntered(MouseEvent e) {}

  @Override
  public void mouseExited(MouseEvent e) {}
}
