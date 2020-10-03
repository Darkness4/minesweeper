package marc.nguyen.minesweeper.client.domain.usecases.connect;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.IO;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.data.devices.ServerSocketDevice;
import marc.nguyen.minesweeper.common.data.models.StartGame;
import org.jetbrains.annotations.NotNull;

/** A User should be able to listen to start game from the server. */
@Singleton
public class WatchServerStartGame implements UseCase<Void, Observable<StartGame>> {

  private final Lazy<ServerSocketDevice> device;

  @Inject
  public WatchServerStartGame(Lazy<ServerSocketDevice> device) {
    this.device = device;
  }

  @Override
  @NotNull
  public Observable<StartGame> execute(Void params) {
    final var observable = device.get().getObservable();
    if (observable != null) {
      return observable
          .filter(e -> e instanceof StartGame)
          .map(e -> (StartGame) e)
          .observeOn(Schedulers.from(IO.executor));
    } else {
      return Observable.empty();
    }
  }
}
