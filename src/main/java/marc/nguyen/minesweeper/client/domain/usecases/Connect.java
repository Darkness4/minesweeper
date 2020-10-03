package marc.nguyen.minesweeper.client.domain.usecases;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.net.InetAddress;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.IO;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.data.devices.ServerSocketDevice;
import marc.nguyen.minesweeper.client.domain.usecases.Connect.Result;
import marc.nguyen.minesweeper.client.domain.usecases.connect.FetchMinefield;
import marc.nguyen.minesweeper.client.domain.usecases.connect.WatchEndGameMessages;
import marc.nguyen.minesweeper.client.domain.usecases.connect.WatchServerPlayerList;
import marc.nguyen.minesweeper.client.domain.usecases.connect.WatchServerStartGame;
import marc.nguyen.minesweeper.client.domain.usecases.connect.WatchServerTiles;
import marc.nguyen.minesweeper.common.data.models.EndGameMessage;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Player;
import marc.nguyen.minesweeper.common.data.models.Position;
import marc.nguyen.minesweeper.common.data.models.StartGame;
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
  private final Lazy<WatchServerStartGame> watchServerStartGame;
  private final Lazy<WatchEndGameMessages> watchEndGameMessages;
  private final Lazy<WatchServerPlayerList> watchServerPlayerList;

  @Inject
  public Connect(
      Lazy<ServerSocketDevice> deviceLazy,
      Lazy<WatchServerTiles> watchServerTilesLazy,
      Lazy<FetchMinefield> fetchMinefieldLazy,
      Lazy<WatchServerStartGame> watchServerStartGame,
      Lazy<WatchEndGameMessages> watchEndGameMessages,
      Lazy<WatchServerPlayerList> watchServerPlayerList) {
    this.deviceLazy = deviceLazy;
    this.watchServerTilesLazy = watchServerTilesLazy;
    this.fetchMinefieldLazy = fetchMinefieldLazy;
    this.watchServerStartGame = watchServerStartGame;
    this.watchEndGameMessages = watchEndGameMessages;
    this.watchServerPlayerList = watchServerPlayerList;
  }

  @Override
  @NotNull
  public Maybe<Result> execute(@NotNull Params params) {
    return Maybe.fromCallable(
            () -> {
              deviceLazy.get().connect(params.address, params.port);
              final var minefield = fetchMinefieldLazy.get().execute(null);
              final var updateStream = watchServerTilesLazy.get().execute(null);
              final var startGameStream = watchServerStartGame.get().execute(null);
              final var endGameMessages = watchEndGameMessages.get().execute(null);
              final var playerList = watchServerPlayerList.get().execute(null);

              return new Result(
                  Objects.requireNonNull(minefield.blockingGet()),
                  updateStream,
                  startGameStream,
                  endGameMessages,
                  playerList);
            })
        .observeOn(Schedulers.from(IO.executor));
  }

  public static class Result {

    @NotNull public final Observable<Position> position$;
    @NotNull public final Minefield initialMinefield;
    @NotNull public final Observable<StartGame> startGame$;
    @NotNull public final Observable<EndGameMessage> endGameMessage$;
    @NotNull public final Observable<List<Player>> playerList$;

    public Result(
        @NotNull Minefield initialMinefield,
        @NotNull Observable<Position> position$,
        @NotNull Observable<StartGame> startGame$,
        @NotNull Observable<EndGameMessage> endGameMessage$,
        @NotNull Observable<List<Player>> playerList$) {
      this.position$ = position$;
      this.initialMinefield = initialMinefield;
      this.startGame$ = startGame$;
      this.endGameMessage$ = endGameMessage$;
      this.playerList$ = playerList$;
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
