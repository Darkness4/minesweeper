package marc.nguyen.minesweeper.server;

import java.io.IOException;
import marc.nguyen.minesweeper.server.api.GameServer;

public final class Server {

  private Server() {}

  public static void main(String[] args) throws IOException {
    // TODO: Better args handling
    // TODO: Docs --help
    // TODO: Game generation parameter in yaml
    final int port = Integer.parseInt(args[0]);

    final var server = new GameServer();
    server.start(port);
  }
}
