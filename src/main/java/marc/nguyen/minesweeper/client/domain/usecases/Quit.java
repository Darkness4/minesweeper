package marc.nguyen.minesweeper.client.domain.usecases;

import dagger.Lazy;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.data.devices.ServerSocketDevice;

/** A user should be to exit the current server. */
@Singleton
public class Quit implements UseCase<Void, Void> {
  private final Lazy<ServerSocketDevice> deviceLazy;

  @Inject
  public Quit(Lazy<ServerSocketDevice> deviceLazy) {
    this.deviceLazy = deviceLazy;
  }

  @Override
  public Void execute(Void params) {
    deviceLazy.get().close();
    return null;
  }
}
