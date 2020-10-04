package marc.nguyen.minesweeper.client.domain.usecases;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.IO;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.data.devices.ServerSocketDevice;
import org.jetbrains.annotations.NotNull;

/** A user should be to exit the current server. */
@Singleton
public class Quit implements UseCase<Void, Completable> {

  private final Lazy<ServerSocketDevice> deviceLazy;

  @Inject
  public Quit(Lazy<ServerSocketDevice> deviceLazy) {
    this.deviceLazy = deviceLazy;
  }

  @Override
  @NotNull
  public Completable execute(Void params) {
    return Completable.fromAction(() -> deviceLazy.get().close())
        .observeOn(Schedulers.from(IO.executor));
  }
}
