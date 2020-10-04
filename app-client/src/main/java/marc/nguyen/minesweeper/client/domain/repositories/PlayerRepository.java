package marc.nguyen.minesweeper.client.domain.repositories;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
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

  @NotNull
  Observable<List<Player>> fetchServerPlayerList();
}
