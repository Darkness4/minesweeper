package marc.nguyen.minesweeper.client.domain.usecases;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.IO;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.data.devices.ServerSocketDevice;
import marc.nguyen.minesweeper.common.data.models.EndGameMessage;
import org.jetbrains.annotations.NotNull;

/** A User should be able to listen to changes from the server. */
@Singleton
public class WatchEndGameMessages implements UseCase<Void, Observable<EndGameMessage>> {

  private final Lazy<ServerSocketDevice> device;

  @Inject
  public WatchEndGameMessages(Lazy<ServerSocketDevice> device) {
    this.device = device;
  }

  @Override
  @NotNull
  public Observable<EndGameMessage> execute(Void params) {
    final var observable = device.get().getObservable();
    if (observable != null) {
      return observable
          .filter(e -> e instanceof EndGameMessage)
          .map(e -> (EndGameMessage) e)
          .observeOn(Schedulers.from(IO.executor));
    } else {
      return Observable.empty();
    }
  }
}
