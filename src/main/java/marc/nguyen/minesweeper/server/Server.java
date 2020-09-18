package marc.nguyen.minesweeper.server;

import java.io.IOException;
import marc.nguyen.minesweeper.common.data.models.Level;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.server.api.GameServer;

public final class Server {

  private Server() {}

  public static void main(String[] args) throws IOException {
    final int port = Integer.parseInt(args[0]);
    final var minefield = new Minefield(Level.HARD);

    final var server = new GameServer();
    server.start(port);
  }
}
