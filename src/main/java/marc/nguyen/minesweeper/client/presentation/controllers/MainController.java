package marc.nguyen.minesweeper.client.presentation.controllers;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.presentation.models.MainModel;
import marc.nguyen.minesweeper.client.presentation.views.MainView;
import marc.nguyen.minesweeper.client.presentation.widgets.MineButton;

public class MainController implements MouseListener {
  private final MainModel _model;

  public MainController(MainModel model, MainView view) {
    _model = model;

    view.gamePanel.addButtonListener(this);
  }

  public void discover(int x, int y) {
    _model.minefield.expose(x, y);
  }

  public void flag(int x, int y) {
    _model.minefield.flag(x, y);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    // Work on IO Thread
    new Thread(
            () -> {
              final var source = e.getSource();
              if (source instanceof MineButton) {
                if (SwingUtilities.isRightMouseButton(e)) {
                  flag(((MineButton) source).x, ((MineButton) source).y);

                } else if (SwingUtilities.isLeftMouseButton(e)) {
                  discover(((MineButton) source).x, ((MineButton) source).y);
                }
                System.out.println(
                    _model.minefield.get(((MineButton) source).x, ((MineButton) source).y));
              }
            })
        .start();
  }

  @Override
  public void mousePressed(MouseEvent e) {}

  @Override
  public void mouseReleased(MouseEvent e) {}

  @Override
  public void mouseEntered(MouseEvent e) {}

  @Override
  public void mouseExited(MouseEvent e) {}

  public static class Factory {
    public MainController create(MainModel model, MainView view) {
      return new MainController(model, view);
    }
  }
}
