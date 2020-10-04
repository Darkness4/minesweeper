package marc.nguyen.minesweeper.client.data.devices;

import io.reactivex.rxjava3.subjects.Subject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;
import marc.nguyen.minesweeper.common.data.models.Message;

/**
 * ServerWorkerRunnable handles the input from the server.
 *
 * <p>This Runnable should be called in a new Thread or in a Thread Pool.
 */
public class ServerWorkerRunnable implements Runnable {

  private final Socket serverSocket;
  private final Subject<Object> publisher;
  private final AtomicBoolean isStopped = new AtomicBoolean(false);

  public ServerWorkerRunnable(Socket serverSocket, Subject<Object> publisher) {
    this.serverSocket = serverSocket;
    this.publisher = publisher;
  }

  /** Read the input and publish the packets in the Publisher. */
  @Override
  public void run() {
    try (final var input = new ObjectInputStream(serverSocket.getInputStream())) {
      while (!isStopped.get()) {
        try {
          final var packet = input.readObject();

          // Publish packet
          publisher.onNext(packet);

          if (packet instanceof Message) {
            System.out.printf("Server said: %s\n", packet);
          }

        } catch (IOException | ClassNotFoundException e) {
          // Server disconnected
          System.out.println(e);
          isStopped.set(true);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    publisher.onComplete();
    System.out.println("Server listener stopped.");
  }
}
