package marc.nguyen.minesweeper.client.data.devices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import marc.nguyen.minesweeper.common.data.models.Message;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Tile;

/**
 * ServerWorkerRunnable handles the input from the server.
 *
 * <p>This Runnable should be called in a new Thread or in a Thread Pool.
 */
public class ServerWorkerRunnable implements Runnable {

  private final Socket serverSocket;
  private boolean isStopped = false;

  // TODO : Add publisher
  public ServerWorkerRunnable(Socket serverSocket) {
    this.serverSocket = serverSocket;
  }

  @Override
  public void run() {
    try (final var input = new ObjectInputStream(serverSocket.getInputStream())) {

      while (!isStopped()) {
        try {
          final var packet = input.readObject();
          // TODO : Move handler somewhere else and publish here
          handle(packet);
        } catch (IOException | ClassNotFoundException e) {
          // Server disconnected
          e.printStackTrace();
          isStopped = true;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Server listener stopped.");
  }

  private synchronized boolean isStopped() {
    return isStopped;
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
