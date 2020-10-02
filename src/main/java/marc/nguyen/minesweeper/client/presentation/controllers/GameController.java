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
import marc.nguyen.minesweeper.client.domain.usecases.Quit;
import marc.nguyen.minesweeper.client.domain.usecases.SaveScore;
import marc.nguyen.minesweeper.client.domain.usecases.UpdateServerPlayer;
import marc.nguyen.minesweeper.client.domain.usecases.UpdateServerTile;
import marc.nguyen.minesweeper.client.domain.usecases.WatchEndGameMessages;
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
  private final Timer timer;
  private final Lazy<SaveScore> saveScore;
  private final Lazy<Quit> quit;
  private final Disposable updateTilesListener;
  private final Disposable watchEndGameMessagesListener;
  private final Disposable updateServerPlayerListener;

  public GameController(
      Lazy<UpdateServerTile> updateMinefield,
      Lazy<UpdateServerPlayer> updateServerPlayer,
      Lazy<SaveScore> saveScore,
      Lazy<Quit> quit,
      Lazy<WatchEndGameMessages> watchEndGameMessages,
      GameModel model,
      GameView view) {
    this.model = model;
    this.view = view;
    this.updateMinefield = updateMinefield;
    this.saveScore = saveScore;
    this.quit = quit;

    // Initial update
    update();
    updateServerPlayerListener = updateServerPlayer.get().execute(this.model.player).subscribe();
    watchEndGameMessagesListener =
        watchEndGameMessages
            .get()
            .execute(null)
            .subscribe(
                m -> {
                  onEndGame();
                  this.view.invokeGameEndedDialog(m.toString());
                  onExitButtonPushed();
                  System.out.println("Game has ended.");
                });

    // Listen to remote changes
    updateTilesListener =
        this.model.tiles.subscribe(
            tile -> {
              this.model.minefield.expose(tile);
              update();
            });

    this.view.gamePanel.addButtonListener(this);

    // Visible clock
    this.timer =
        new Timer(
            1000,
            e -> {
              this.model.incrementTime();
              this.view.displayPanel.updateTimeLeft(this.model.getTime());
            });
  }

  /** Closes every listeners */
  public void dispose() {
    updateServerPlayerListener.dispose();
    watchEndGameMessagesListener.dispose();
    updateTilesListener.dispose();
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
    this.saveScore.get().execute(this.model.player).subscribe();

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
            final var tile = model.minefield.get(mineButton.x, mineButton.y);
            if (SwingUtilities.isRightMouseButton(e)) {
              model.minefield.flag(tile);
            } else if (SwingUtilities.isLeftMouseButton(e)) {
              if (tile.getState() != Tile.State.FLAG) {
                if (!timer.isRunning()) {
                  timer.start();
                }
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

    final Lazy<UpdateServerTile> updateMinefield;
    final Lazy<UpdateServerPlayer> updateServerPlayer;
    final Lazy<SaveScore> saveScore;
    final Lazy<Quit> quit;
    final Lazy<WatchEndGameMessages> watchEndGameMessages;

    @Inject
    public Factory(
        Lazy<UpdateServerTile> updateMinefield,
        Lazy<UpdateServerPlayer> updateServerPlayer,
        Lazy<SaveScore> saveScore,
        Lazy<Quit> quit,
        Lazy<WatchEndGameMessages> watchEndGameMessages) {
      this.updateMinefield = updateMinefield;
      this.updateServerPlayer = updateServerPlayer;
      this.saveScore = saveScore;
      this.quit = quit;
      this.watchEndGameMessages = watchEndGameMessages;
    }

    public GameController create(GameModel model, GameView view) {
      return new GameController(
          updateMinefield, updateServerPlayer, saveScore, quit, watchEndGameMessages, model, view);
    }
  }
}
