package marc.nguyen.minesweeper.client.presentation.models;

import io.reactivex.rxjava3.core.Observable;
import java.time.LocalTime;
import java.util.List;
import javax.inject.Inject;
import marc.nguyen.minesweeper.client.core.mvc.Model;
import marc.nguyen.minesweeper.client.presentation.models.game.PlayerListTableModel;
import marc.nguyen.minesweeper.common.data.models.EndGameMessage;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Player;
import marc.nguyen.minesweeper.common.data.models.Position;

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
  public final Observable<Position> position$;
  /** Stream of end game message. */
  public final Observable<EndGameMessage> endGameMessage$;
  /** Player list */
  public final PlayerListTableModel playerListTableModel;

  private LocalTime time;

  @Inject
  public GameModel(
      Minefield minefield,
      Player player,
      Observable<Position> position$,
      Observable<EndGameMessage> endGameMessage$,
      Observable<List<Player>> playerList$) {
    this.minefield = minefield;
    this.player = player;
    this.position$ = position$;
    this.endGameMessage$ = endGameMessage$;
    playerListTableModel = new PlayerListTableModel(playerList$);
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
