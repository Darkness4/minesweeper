package marc.nguyen.minesweeper.client.data.datasources;

import java.io.IOException;
import java.io.ObjectInputStream;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Tile;

public class ServerWorker extends Thread {
  private final ObjectInputStream inputStream;

  public ServerWorker(ObjectInputStream inputStream) {
    this.inputStream = inputStream;
  }

  @Override
  public void run() {
    while (!interrupted()) {
      try {
        final var packet = this.inputStream.readObject();
        handle(packet);
      } catch (IOException | ClassNotFoundException e) {
        // Server disconnected
        e.printStackTrace();
        interrupt();
      }
    }
  }

  void handle(Object packet) {
    if (packet instanceof Tile) {
      // TODO: Handle
    } else if (packet instanceof Minefield) {

    } else {
      System.out.format("Packet %s not handled\n", packet);
    }
  }
}
