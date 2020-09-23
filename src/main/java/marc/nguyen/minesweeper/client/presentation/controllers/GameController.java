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
import marc.nguyen.minesweeper.client.presentation.models.GameModel;
import marc.nguyen.minesweeper.client.presentation.views.GameView;
import marc.nguyen.minesweeper.client.presentation.widgets.MineButton;
import marc.nguyen.minesweeper.common.data.models.Tile;

/**
 * The Game Controller.
 *
 * <p>Mutate the game model (local minefield, server's minefield, score...) and notify the game
 * view.
 *
 * <p>Factory should be used as the primary constructor. The Frame should assist the injection to
 * avoid using Singletons with the presentation dependencies (model and view must NOT be
 * singletons). (Note that we could have used member injection using a setter or a builder.)
 *
 * @see GameModel
 * @see GameView
 * @see Factory
 */
public class GameController implements MouseListener, Controller<GameModel, GameView> {

  private final GameModel model;
  private final GameView view;
  private final Lazy<UpdateServerTile> updateMinefield;

  public GameController(Lazy<UpdateServerTile> updateMinefield, GameModel model, GameView view) {
    this.model = model;
    this.view = view;
    this.updateMinefield = updateMinefield;

    // Initial update
    update();

    // Listen to remote changes
    this.model.tiles.subscribe(
        (tile) -> {
          this.model.minefield.expose(tile);
          update();
        });

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

  /**
   * Flag a tile on right click. Discover a tile on left click.
   *
   * @param e A mouse event.
   */
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
                updateMinefield.get().execute(tile).subscribe();

                if (tile instanceof Tile.Empty) {
                  model.player.incrementScore();
                } else if (tile instanceof Tile.Mine) {
                  model.player.decrementScore();
                }
              }
            }

            view.displayPanel.updatePlayerScore(model.player.getScore());
            update();
          }
        },
        IO.executor);
  }

  private void update() {
    updateBombLeft();
    view.gamePanel.updateField(model.minefield);
    checkIfEndGame();
  }

  @Override
  public void mouseEntered(MouseEvent e) {}

  @Override
  public void mouseExited(MouseEvent e) {}

  /** Factory used for assisted dependencies injection. */
  public static final class Factory {

    final Lazy<UpdateServerTile> updateMinefield;

    @Inject
    public Factory(Lazy<UpdateServerTile> updateMinefield) {
      this.updateMinefield = updateMinefield;
    }

    public GameController create(GameModel model, GameView view) {
      return new GameController(updateMinefield, model, view);
    }
  }
}
