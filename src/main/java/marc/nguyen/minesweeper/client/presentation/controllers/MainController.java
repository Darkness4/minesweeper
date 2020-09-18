package marc.nguyen.minesweeper.client.presentation.controllers;

import dagger.Lazy;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.ExecutionException;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import marc.nguyen.minesweeper.client.core.mvc.Controller;
import marc.nguyen.minesweeper.client.domain.usecases.UpdateMinefield;
import marc.nguyen.minesweeper.client.presentation.models.MainModel;
import marc.nguyen.minesweeper.client.presentation.views.MainView;
import marc.nguyen.minesweeper.client.presentation.widgets.MineButton;

public class MainController implements MouseListener, Controller<MainModel, MainView> {

  private final MainModel _model;
  private final MainView _view;
  private final Lazy<UpdateMinefield> _updateMinefield;

  public MainController(Lazy<UpdateMinefield> updateMinefield, MainModel model, MainView view) {
    _model = model;
    _view = view;
    _updateMinefield = updateMinefield;

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
              final var mineButton = _view.gamePanel.mineButtons[i][j];
              mineButton.updateValueFromTile(tile);
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
                final var mineButton = (MineButton) source;
                final var tile = _model.minefield.get(mineButton.x, mineButton.y);
                if (SwingUtilities.isRightMouseButton(e)) {
                  _model.minefield.flag(tile);
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                  _model.minefield.expose(tile);
                }

                updateBombLeft();
                updateField();
                _updateMinefield.get().execute(_model.minefield);
              }
            })
        .start();
  }

  @Override
  public void mouseEntered(MouseEvent e) {}

  @Override
  public void mouseExited(MouseEvent e) {}

  public static final class Factory {

    final Lazy<UpdateMinefield> updateMinefield;

    @Inject
    public Factory(Lazy<UpdateMinefield> updateMinefield) {
      this.updateMinefield = updateMinefield;
    }

    public MainController create(MainModel model, MainView view) {
      return new MainController(updateMinefield, model, view);
    }
  }
}
