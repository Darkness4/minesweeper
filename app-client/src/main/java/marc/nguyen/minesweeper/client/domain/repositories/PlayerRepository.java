package marc.nguyen.minesweeper.client.domain.repositories;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import marc.nguyen.minesweeper.common.data.models.Player;
import org.jetbrains.annotations.NotNull;

public interface PlayerRepository {
  /**
   * Send a Player to the server.
   *
   * @param player New player model.
   * @return A completable.
   */
  @NotNull
  Completable update(@NotNull Player player);

  /**
   * Save a player into the local database.
   *
   * @param player A player.
   * @return A completable.
   */
  @NotNull
  Completable save(@NotNull Player player);

  /**
   * Get all the player of the local database.
   *
   * @return A Single List of Players.
   */
  @NotNull
  Single<List<Player>> fetchAll();

  /**
   * Get a player of the local database by name.
   *
   * @param name Name of the player.
   * @return Maybe a Player.
   */
  @NotNull
  Maybe<Player> fetchByName(@NotNull String name);

  @NotNull
  Observable<List<Player>> fetchServerPlayerList();
}
