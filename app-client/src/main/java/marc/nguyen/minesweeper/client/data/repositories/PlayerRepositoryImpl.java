package marc.nguyen.minesweeper.client.data.repositories;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import marc.nguyen.minesweeper.client.data.database.PlayerDao;
import marc.nguyen.minesweeper.client.data.devices.ServerSocketDevice;
import marc.nguyen.minesweeper.client.domain.repositories.PlayerRepository;
import marc.nguyen.minesweeper.common.data.models.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerRepositoryImpl implements PlayerRepository {

  private final Lazy<ServerSocketDevice> serverSocketDevice;
  private final Lazy<PlayerDao> playerDao;

  @Inject
  public PlayerRepositoryImpl(
      Lazy<ServerSocketDevice> serverSocketDevice, Lazy<PlayerDao> playerDao) {
    this.serverSocketDevice = serverSocketDevice;
    this.playerDao = playerDao;
  }

  @Override
  public @NotNull Completable update(@NotNull Player player) {
    return Completable.fromAction(() -> serverSocketDevice.get().write(player));
  }

  @Override
  public @NotNull Completable save(@NotNull Player player) {
    return Completable.fromAction(() -> playerDao.get().insert(player));
  }

  @Override
  public @NotNull Single<List<Player>> fetchAll() {
    return Single.fromCallable(() -> playerDao.get().findAll());
  }

  @Override
  public @NotNull Maybe<Player> fetchByName(@NotNull String name) {
    return Maybe.just(playerDao.get())
        .flatMap(
            (dao) -> {
              final var result = dao.findByName(name);
              return Maybe.fromOptional(Optional.ofNullable(result));
            });
  }

  @Override
  public @NotNull Observable<List<Player>> fetchServerPlayerList() {
    final var observable = serverSocketDevice.get().getObservable();
    if (observable != null) {
      return observable
          .filter(o -> o instanceof List<?>)
          .map(
              o -> {
                final var list = (List<?>) o;
                return list.stream()
                    .filter(e -> e instanceof Player)
                    .map(e -> (Player) e)
                    .collect(Collectors.toUnmodifiableList());
              });
    } else {
      return Observable.empty();
    }
  }
}
