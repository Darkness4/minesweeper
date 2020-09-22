package marc.nguyen.minesweeper.client.domain.usecases;

import dagger.Lazy;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.data.devices.ServerSocketDevice;
import org.jetbrains.annotations.NotNull;

/** A user should be to exit the current server. */
@Singleton
public class Quit implements UseCase<Void, Void> {

  private final Lazy<ServerSocketDevice> deviceLazy;

  @Inject
  public Quit(Lazy<ServerSocketDevice> deviceLazy) {
    this.deviceLazy = deviceLazy;
  }

  // TODO: May want to use future
  @Override
  @NotNull
  public Void execute(Void params) {
    assert !SwingUtilities.isEventDispatchThread() : "Domain method executed on UI Thread !";

    deviceLazy.get().close();
    return null;
  }
}
