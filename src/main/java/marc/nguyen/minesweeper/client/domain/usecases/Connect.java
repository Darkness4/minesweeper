package marc.nguyen.minesweeper.client.domain.usecases;

import java.net.InetAddress;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import org.jetbrains.annotations.NotNull;

@Singleton
public class Connect implements UseCase<Connect.Params, Void> {

  @Inject
  public Connect() {}

  @Override
  public Void execute(@NotNull Params params) {
    // TODO: implements
    return null;
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
