package marc.nguyen.minesweeper.client.data.devices;

import io.reactivex.rxjava3.subjects.PublishSubject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
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
  private final PublishSubject<Object> publisher;
  private final AtomicBoolean isStopped = new AtomicBoolean(false);

  public ServerWorkerRunnable(Socket serverSocket, PublishSubject<Object> publisher) {
    this.serverSocket = serverSocket;
    this.publisher = publisher;
  }

  @Override
  public void run() {
    try (final var input = new ObjectInputStream(serverSocket.getInputStream())) {

      while (!isStopped.get()) {
        try {
          final var packet = input.readObject();
          // TODO : Move handler somewhere else and publish here
          publisher.onNext(packet);
          handle(packet);
        } catch (IOException | ClassNotFoundException e) {
          // Server disconnected
          e.printStackTrace();
          isStopped.set(true);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    publisher.onComplete();
    System.out.println("Server listener stopped.");
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
