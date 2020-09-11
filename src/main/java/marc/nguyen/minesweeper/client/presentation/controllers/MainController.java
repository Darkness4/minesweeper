package marc.nguyen.minesweeper.client.presentation.controllers;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.ExecutionException;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import marc.nguyen.minesweeper.client.core.mvc.Controller;
import marc.nguyen.minesweeper.client.presentation.models.MainModel;
import marc.nguyen.minesweeper.client.presentation.views.MainView;
import marc.nguyen.minesweeper.client.presentation.widgets.MineButton;
import marc.nguyen.minesweeper.common.data.models.Tile.Empty;
import marc.nguyen.minesweeper.common.data.models.Tile.State;

public class MainController implements MouseListener, Controller<MainModel, MainView> {

  private final MainModel _model;
  private final MainView _view;

  public MainController(MainModel model, MainView view) {
    _model = model;
    _view = view;

    _view.gamePanel.addButtonListener(this);
  }

  private void updateBombLeft() {
    new SwingWorker<Long, Void>() {
      @Override
      protected Long doInBackground() {
        return _model.minefield.countMinesOnField()
            - _model.minefield.countFlagsAndVisibleBombsOnField();
      }

      @Override
      protected void done() {
        try {
          _view.displayPanel.bombLeftText.setText(String.valueOf(get()));
        } catch (InterruptedException | ExecutionException e) {
          e.printStackTrace();
        }
      }
    }.execute();
  }

  private void updateField() {
    SwingUtilities.invokeLater(
        () -> {
          for (int i = 0; i < _model.minefield.getLength(); i++) {
            for (int j = 0; j < _model.minefield.getHeight(); j++) {
              final var tile = _model.minefield.get(i, j);
              final var state = tile.getState();
              if (state == State.BLANK) {
                _view.gamePanel.mineButtons[i][j].setBackground(Color.WHITE);
              } else if (state == State.EXPOSED) {
                _view.gamePanel.mineButtons[i][j].setBackground(Color.GRAY);
                if (tile instanceof Empty) {
                  final var neighbors = ((Empty) tile).getNeighborMinesCount();
                  if (neighbors != 0) {
                    _view.gamePanel.mineButtons[i][j].setText(String.valueOf(neighbors));
                  }
                  _view.gamePanel.mineButtons[i][j].setEnabled(false);
                }
              } else if (state == State.FLAG) {
                _view.gamePanel.mineButtons[i][j].setBackground(Color.CYAN);
              } else if (state == State.HIT_MINE) {
                _view.gamePanel.mineButtons[i][j].setBackground(Color.RED);
                _view.gamePanel.mineButtons[i][j].setEnabled(false);
              }
            }
          }
        });
  }

  @Override
  public void mouseClicked(MouseEvent e) {}

  @Override
  public void mousePressed(MouseEvent e) {}

  @Override
  public void mouseReleased(MouseEvent e) {
    new Thread(
            () -> {
              final var source = e.getSource();
              if (source instanceof MineButton && ((MineButton) source).isEnabled()) {
                if (SwingUtilities.isRightMouseButton(e)) {
                  _model.minefield.flag(((MineButton) source).x, ((MineButton) source).y);
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                  _model.minefield.expose(((MineButton) source).x, ((MineButton) source).y);
                }

                updateBombLeft();
                updateField();
              }
            })
        .start();
  }

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
