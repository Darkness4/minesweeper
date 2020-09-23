package marc.nguyen.minesweeper.client.domain.usecases;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.net.InetAddress;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.IO;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.data.devices.ServerSocketDevice;
import marc.nguyen.minesweeper.client.domain.usecases.Connect.Result;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Tile;
import org.jetbrains.annotations.NotNull;

/**
 * A user should be able to connect to a Minesweeper server based on a port and an IP address.
 *
 * <p>The result should be :
 *
 * <ul>
 *   <li>The minefield of the server.
 *   <li>An observable to listen changes.
 * </ul>
 */
@Singleton
public class Connect implements UseCase<Connect.Params, Maybe<Result>> {

  private final Lazy<ServerSocketDevice> deviceLazy;
  private final Lazy<WatchServerTiles> watchServerTilesLazy;
  private final Lazy<FetchMinefield> fetchMinefieldLazy;

  @Inject
  public Connect(
      Lazy<ServerSocketDevice> deviceLazy,
      Lazy<WatchServerTiles> watchServerTilesLazy,
      Lazy<FetchMinefield> fetchMinefieldLazy) {
    this.deviceLazy = deviceLazy;
    this.watchServerTilesLazy = watchServerTilesLazy;
    this.fetchMinefieldLazy = fetchMinefieldLazy;
  }

  @Override
  @NotNull
  public Maybe<Result> execute(@NotNull Params params) {
    return Maybe.fromCallable(
            () -> {
              deviceLazy.get().connect(params.address, params.port);
              final var minefield = fetchMinefieldLazy.get().execute(null);
              final var updateStream = watchServerTilesLazy.get().execute(null);

              return new Result(updateStream, Objects.requireNonNull(minefield.blockingGet()));
            })
        .observeOn(Schedulers.from(IO.executor));
  }

  public static class Result {

    @NotNull public final Observable<Tile> tiles;
    @NotNull public final Minefield initialMinefield;

    public Result(@NotNull Observable<Tile> tiles, @NotNull Minefield initialMinefield) {
      this.tiles = tiles;
      this.initialMinefield = initialMinefield;
    }
  }

  public static class Params {

    @NotNull final InetAddress address;
    final int port;

    public Params(@NotNull InetAddress address, int port) {
      this.address = address;
      this.port = port;
    }
  }
}
