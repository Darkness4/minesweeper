package marc.nguyen.minesweeper.client.presentation.controllers;

import dagger.Lazy;
import io.reactivex.rxjava3.disposables.Disposable;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import marc.nguyen.minesweeper.client.core.IO;
import marc.nguyen.minesweeper.client.core.mvc.Controller;
import marc.nguyen.minesweeper.client.di.components.DaggerGameCreationComponent;
import marc.nguyen.minesweeper.client.domain.entities.HighScore;
import marc.nguyen.minesweeper.client.domain.usecases.Quit;
import marc.nguyen.minesweeper.client.domain.usecases.SaveScore;
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
  private final Lazy<UpdateServerTile> updateServerTile;
  private final Lazy<SaveScore> saveScore;
  private final Lazy<Quit> quit;
  private final Disposable updateTilesListener;
  private final Disposable endGameMessagesListener;
  private final Timer timer;

  public GameController(
      Lazy<UpdateServerTile> updateServerTile,
      Lazy<SaveScore> saveScore,
      Lazy<Quit> quit,
      GameModel model,
      GameView view) {
    this.model = model;
    this.view = view;
    this.updateServerTile = updateServerTile;
    this.saveScore = saveScore;
    this.quit = quit;

    // Initial update
    update();

    // Visible clock
    this.timer =
        new Timer(
            1000,
            e -> {
              this.model.incrementTime();
              this.view.displayPanel.updateTimeLeft(this.model.getTime());
            });

    // Listen to remote changes
    updateTilesListener =
        this.model.position$.subscribe(
            tile -> {
              this.model.minefield.expose(tile);
              if (!timer.isRunning()) {
                timer.start();
              }
              update();
            },
            e -> {
              System.err.println("[ERROR] updateTilesListener error.");
              e.printStackTrace();
            });
    endGameMessagesListener =
        this.model.endGameMessage$.subscribe(
            m -> {
              onEndGame();
              this.view.invokeGameEndedDialog(m.toString());
              onExitButtonPushed();
              System.out.println("Game has ended.");
            },
            e -> {
              System.err.println("[ERROR] endGameMessagesListener error.");
              e.printStackTrace();
            });

    if (this.view.playerListPanel != null) {
      this.view.playerListPanel.playerTable.setModel(this.model.playerTableModel);
    }

    this.view.gamePanel.addButtonListener(this);
  }

  /** Closes every listeners */
  public void dispose() {
    endGameMessagesListener.dispose();
    updateTilesListener.dispose();
    this.model.playerTableModel.dispose();
  }

  private void updateBombLeft() {
    final var minesLeft =
        model.minefield.getMinesOnField() - model.minefield.countFlagsAndVisibleBombsOnField();
    view.displayPanel.updateMinesLeft(minesLeft);
  }

  private void checkIfEndGame() {
    if (model.minefield.hasEnded()) {
      onEndGame();
      this.view.invokeGameEndedDialog();
      onExitButtonPushed();
      System.out.println("Game has ended.");
    }
  }

  private void onEndGame() {
    // Stop the timer
    this.timer.stop();

    // Save the personal score
    this.saveScore
        .get()
        .execute(new HighScore(this.model.player, this.model.minefield))
        .doOnError(
            e -> {
              System.err.println("[ERROR] saveScore error.");
              e.printStackTrace();
            })
        .subscribe();

    // Expose all the mines (show the solution)
    this.model.minefield.exposeAllMines();
    view.gamePanel.updateField(model.minefield);
  }

  private void onExitButtonPushed() {
    this.quit.get().execute(null).blockingAwait(); // Will free every threads.
    SwingUtilities.windowForComponent(this.view).dispose();
    SwingUtilities.invokeLater(
        () -> DaggerGameCreationComponent.builder().build().gameCreationDialog());
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
            final var tile = model.minefield.get(mineButton.position);
            if (SwingUtilities.isRightMouseButton(e)) {
              model.minefield.flag(tile);
            } else if (SwingUtilities.isLeftMouseButton(e)) {
              if (tile.getState() != Tile.State.FLAG) {
                if (!timer.isRunning()) {
                  timer.start();
                }
                final var numberOfTileDiscovered = model.minefield.expose(tile.position);
                updateServerTile
                    .get()
                    .execute(tile.position)
                    .doOnError(
                        t -> {
                          System.err.println("[ERROR] updateMinefield error.");
                          t.printStackTrace();
                        })
                    .subscribe();

                if (tile instanceof Tile.Empty) {
                  model.player.addScore(numberOfTileDiscovered);
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

    // Only check end game if is singleplayer
    if (this.model.minefield.isSinglePlayer) {
      checkIfEndGame();
    }
  }

  @Override
  public void mouseEntered(MouseEvent e) {}

  @Override
  public void mouseExited(MouseEvent e) {}

  /** Factory used for assisted dependencies injection. */
  public static final class Factory {

    final Lazy<UpdateServerTile> updateServerTile;
    final Lazy<SaveScore> saveScore;
    final Lazy<Quit> quit;

    @Inject
    public Factory(
        Lazy<UpdateServerTile> updateServerTile, Lazy<SaveScore> saveScore, Lazy<Quit> quit) {
      this.updateServerTile = updateServerTile;
      this.saveScore = saveScore;
      this.quit = quit;
    }

    public GameController create(GameModel model, GameView view) {
      return new GameController(updateServerTile, saveScore, quit, model, view);
    }
  }
}
