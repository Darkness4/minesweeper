package marc.nguyen.minesweeper.client.presentation.models;

import io.reactivex.rxjava3.core.Observable;
import java.time.LocalTime;
import javax.inject.Inject;
import marc.nguyen.minesweeper.client.core.mvc.Model;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Player;
import marc.nguyen.minesweeper.common.data.models.Tile;

/**
 * The Game Model.
 *
 * <p>Immutable. The members, however, are either mutable or observable.
 */
public class GameModel implements Model {

  /** Current Minefield */
  public final Minefield minefield;

  /** Current Player */
  public final Player player;
  /** Stream of new tiles for minefield. */
  public final Observable<Tile> tiles;

  private LocalTime time;

  @Inject
  public GameModel(Minefield minefield, Player player, Observable<Tile> tiles) {
    this.minefield = minefield;
    this.player = player;
    this.tiles = tiles;
    time = LocalTime.MIN;
  }

  /** Increment the counter. */
  public void incrementTime() {
    this.time = time.plusSeconds(1);
  }

  /**
   * Get the time of the game.
   *
   * @return Time of the game.
   */
  public LocalTime getTime() {
    return time;
  }
}
