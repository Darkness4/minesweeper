package marc.nguyen.minesweeper.client.domain.usecases;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.data.datasources.ServerWorkerRunnable;
import org.jetbrains.annotations.NotNull;

/** A user should be able to connect to a TCP server based on a port and an IP address. */
@Singleton
public class Connect implements UseCase<Connect.Params, Void> {

  @Inject
  public Connect() {}

  @Override
  public Void execute(@NotNull Params params) {
    try {
      // TODO: Use Factory
      // TODO: May want to add a Writer thread. Or return the output stream.
      new Thread(new ServerWorkerRunnable(new Socket(params.address, params.port))).start();
    } catch (IOException e) {
      e.printStackTrace();
    }
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
