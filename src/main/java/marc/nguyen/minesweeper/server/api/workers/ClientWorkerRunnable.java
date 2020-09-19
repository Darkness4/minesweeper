package marc.nguyen.minesweeper.server.api.workers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import marc.nguyen.minesweeper.common.data.models.Message;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Tile;

public class ClientWorkerRunnable implements Runnable {

  private final Socket clientSocket;
  private boolean isStopped = false;

  public ClientWorkerRunnable(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

  @Override
  public void run() {
    try {
      final ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
      final ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());

      while (!isStopped()) {
        try {
          final var packet = input.readObject();
          handle(packet);
        } catch (IOException | ClassNotFoundException e) {
          // Server disconnected
          e.printStackTrace();
          isStopped = true;
        }
      }

      output.close();
      input.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private synchronized boolean isStopped() {
    return isStopped;
  }

  void handle(Object packet) {
    if (packet instanceof Tile) {
      // TODO: Handle
    } else if (packet instanceof Minefield) {

    } else if (packet instanceof Message) {
      System.out.printf("Client said: %s\n", packet);
    } else {
      System.out.format("Packet %s not handled\n", packet);
    }
  }
}
