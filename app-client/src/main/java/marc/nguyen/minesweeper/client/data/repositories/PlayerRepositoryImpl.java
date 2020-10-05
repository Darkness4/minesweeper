package marc.nguyen.minesweeper.client.data.repositories;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import marc.nguyen.minesweeper.client.data.devices.ServerSocketDevice;
import marc.nguyen.minesweeper.client.domain.repositories.PlayerRepository;
import marc.nguyen.minesweeper.common.data.models.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerRepositoryImpl implements PlayerRepository {

  private final Lazy<ServerSocketDevice> serverSocketDevice;

  @Inject
  public PlayerRepositoryImpl(Lazy<ServerSocketDevice> serverSocketDevice) {
    this.serverSocketDevice = serverSocketDevice;
  }

  @Override
  public @NotNull Completable update(@NotNull Player player) {
    return Completable.fromAction(() -> serverSocketDevice.get().write(player));
  }

  @Override
  public @NotNull Observable<List<Player>> fetchServerPlayerList() {
    final var observable = serverSocketDevice.get().getObservable();
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
  }
}
