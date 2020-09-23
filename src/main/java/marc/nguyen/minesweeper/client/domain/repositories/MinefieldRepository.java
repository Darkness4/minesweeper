package marc.nguyen.minesweeper.client.domain.repositories;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Tile;
import org.jetbrains.annotations.NotNull;

/** Minefield Repository. */
public interface MinefieldRepository {

  /** @return Maybe get a Minefield. */
  Maybe<Minefield> fetch();

  /** @return Stream of tiles from the server. */
  Observable<Tile> watchTiles();

  /**
   * Send a tile to the server.
   *
   * @param tile New Tile.
   * @return A completable.
   */
  Completable updateTile(@NotNull Tile tile);
}
