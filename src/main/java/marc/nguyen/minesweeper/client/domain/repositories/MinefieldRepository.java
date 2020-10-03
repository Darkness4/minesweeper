package marc.nguyen.minesweeper.client.domain.repositories;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Position;
import org.jetbrains.annotations.NotNull;

/** Minefield Repository. */
public interface MinefieldRepository {

  /**
   * Fetch a minefield.
   *
   * @return Maybe a Minefield.
   */
  Maybe<Minefield> fetch();

  /**
   * Watch the incoming tiles updates.
   *
   * @return Stream of tiles from the server.
   */
  Observable<Position> watchTiles();

  /**
   * Send a position to the server.
   *
   * @param position New position.
   * @return A completable.
   */
  Completable updateTile(@NotNull Position position);
}
