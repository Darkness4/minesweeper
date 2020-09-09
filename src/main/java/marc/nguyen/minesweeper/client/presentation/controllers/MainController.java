package marc.nguyen.minesweeper.client.presentation.controllers;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.ExecutionException;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import marc.nguyen.minesweeper.client.presentation.models.MainModel;
import marc.nguyen.minesweeper.client.presentation.views.MainView;
import marc.nguyen.minesweeper.client.presentation.widgets.MineButton;

public class MainController implements MouseListener {

  private final MainModel _model;
  private final MainView _view;

  public MainController(MainModel model, MainView view) {
    _model = model;
    _view = view;

    _view.gamePanel.addButtonListener(this);
  }

  public void updateBombLeft() {
    new SwingWorker<Long, Void>() {
      @Override
      protected Long doInBackground() {
        return _model.minefield.countMinesOnField()
            - _model.minefield.countFlagsAndVisibleBombsOnField();
      }

      protected void done() {
        try {
          _view.displayPanel.bombLeftText.setText(String.valueOf(get()));
        } catch (InterruptedException | ExecutionException e) {
          e.printStackTrace();
        }
      }
    }.execute();
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    new Thread(
            () -> {
              final var source = e.getSource();
              if (source instanceof MineButton) {
                if (SwingUtilities.isRightMouseButton(e)) {
                  _model.minefield.flag(((MineButton) source).x, ((MineButton) source).y);
                  updateBombLeft();
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                  _model.minefield.expose(((MineButton) source).x, ((MineButton) source).y);
                  // TODO: Use UpdateMinefield
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

    @Inject
    public Factory() {}

    public MainController create(MainModel model, MainView view) {
      return new MainController(model, view);
    }
  }
}
