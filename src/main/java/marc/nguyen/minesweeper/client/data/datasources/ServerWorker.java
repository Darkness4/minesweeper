package marc.nguyen.minesweeper.client.data.datasources;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.common.data.models.Message;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Tile;

@Singleton
public class ServerWorker extends Thread {

  private final Socket serverSocket;

  public ServerWorker(Socket serverSocket) {
    this.serverSocket = serverSocket;
  }

  @Override
  public void run() {
    try {
      final ObjectInputStream input = new ObjectInputStream(serverSocket.getInputStream());
      final ObjectOutputStream output = new ObjectOutputStream(serverSocket.getOutputStream());

      output.writeObject(new Message("Hello server !"));
      output.flush();
      while (!interrupted()) {
        try {
          final var packet = input.readObject();
          handle(packet);
        } catch (IOException | ClassNotFoundException e) {
          // Server disconnected
          e.printStackTrace();
          interrupt();
        }
      }

      output.close();
      input.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  void handle(Object packet) {
    if (packet instanceof Tile) {
      // TODO: Handle
    } else if (packet instanceof Minefield) {

    } else if (packet instanceof Message) {
      System.out.printf("Server said: %s\n", packet);
    } else {
      System.out.format("Packet %s not handled\n", packet);
    }
  }
}
