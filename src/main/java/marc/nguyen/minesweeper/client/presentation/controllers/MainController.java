package marc.nguyen.minesweeper.client.presentation.controllers;

import dagger.Lazy;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.core.IO;
import marc.nguyen.minesweeper.client.core.mvc.Controller;
import marc.nguyen.minesweeper.client.domain.usecases.UpdateServerTile;
import marc.nguyen.minesweeper.client.presentation.models.MainModel;
import marc.nguyen.minesweeper.client.presentation.views.MainView;
import marc.nguyen.minesweeper.client.presentation.widgets.MineButton;
import marc.nguyen.minesweeper.common.data.models.Tile;

// TODO : Threading is broken. Use executor service.
public class MainController implements MouseListener, Controller<MainModel, MainView> {

  private final MainModel model;
  private final MainView view;
  private final Lazy<UpdateServerTile> updateMinefield;

  public MainController(Lazy<UpdateServerTile> updateMinefield, MainModel model, MainView view) {
    this.model = model;
    this.view = view;
    this.updateMinefield = updateMinefield;

    this.view.gamePanel.addButtonListener(this);
  }

  private void updateBombLeft() {
    final var minesLeft =
        model.minefield.getMinesOnField() - model.minefield.countFlagsAndVisibleBombsOnField();
    view.displayPanel.updateMinesLeft(minesLeft);
  }

  private void checkIfEndGame() {
    if (model.minefield.hasEnded()) {
      System.out.println("Game has ended.");
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {}

  @Override
  public void mousePressed(MouseEvent e) {}

  @Override
  public void mouseReleased(MouseEvent e) {
    CompletableFuture.runAsync(
        () -> {
          final var source = e.getSource();
          if (source instanceof MineButton && ((MineButton) source).isEnabled()) {
            final var mineButton = (MineButton) source;
            final var tile = model.minefield.get(mineButton.x, mineButton.y);
            if (SwingUtilities.isRightMouseButton(e)) {
              model.minefield.flag(tile);
            } else if (SwingUtilities.isLeftMouseButton(e)) {
              if (tile.getState() != Tile.State.FLAG) {
                model.minefield.expose(tile);

                if (tile instanceof Tile.Empty) {
                  model.player.incrementScore();
                } else {
                  model.player.decrementScore();
                }
              }
            }

            updateBombLeft();
            view.displayPanel.updatePlayerScore(model.player.getScore());
            view.gamePanel.updateField(model.minefield);
            checkIfEndGame();
            updateMinefield.get().execute(tile).blockingAwait();
          }
        },
        IO.executor);
  }

  @Override
  public void mouseEntered(MouseEvent e) {}

  @Override
  public void mouseExited(MouseEvent e) {}

  public static final class Factory {

    final Lazy<UpdateServerTile> updateMinefield;

    @Inject
    public Factory(Lazy<UpdateServerTile> updateMinefield) {
      this.updateMinefield = updateMinefield;
    }

    public MainController create(MainModel model, MainView view) {
      return new MainController(updateMinefield, model, view);
    }
  }
}
