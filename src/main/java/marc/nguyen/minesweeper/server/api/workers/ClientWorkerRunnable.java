package marc.nguyen.minesweeper.server.api.workers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import marc.nguyen.minesweeper.common.data.models.Level;
import marc.nguyen.minesweeper.common.data.models.Message;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Tile;
import org.jetbrains.annotations.Nullable;

/**
 * ServerWorkerRunnable handles the input from the client.
 *
 * <p>This Runnable should be called in a new Thread or in a Thread Pool.
 */
public class ClientWorkerRunnable implements Runnable {

  private final Socket clientSocket;
  private boolean isStopped = false;
  private @Nullable ObjectOutputStream output = null;
  private final ConcurrentLinkedQueue<ObjectOutputStream> outputStreams;

  public ClientWorkerRunnable(
      Socket clientSocket, ConcurrentLinkedQueue<ObjectOutputStream> outputStreams) {
    this.clientSocket = clientSocket;
    this.outputStreams = outputStreams;
  }

  @Override
  public void run() {
    try (final var input = new ObjectInputStream(clientSocket.getInputStream());
        final var output = new ObjectOutputStream(clientSocket.getOutputStream())) {

      this.output = output;
      outputStreams.add(output);

      output.writeObject(new Message("Hello client !"));
      output.writeObject(new Minefield(Level.EASY, false));
      while (!isStopped()) {
        try {
          final var packet = input.readObject();
          handle(packet);
        } catch (IOException | ClassNotFoundException e) {
          // Client disconnected
          System.out.println(e);
          isStopped = true;
        }
      }

      outputStreams.remove(output);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private synchronized boolean isStopped() {
    return isStopped;
  }

  void handle(Object packet) {
    if (packet instanceof Tile) {
      outputStreams.parallelStream()
          .filter(s -> s != output)
          .forEach(
              s -> {
                try {
                  s.writeObject(packet);
                } catch (IOException e) {
                  e.printStackTrace();
                }
              });
      System.out.println(packet);
    } else if (packet instanceof Message) {
      System.out.printf("Client said: %s\n", packet);
    } else {
      System.out.format("Packet %s not handled\n", packet);
    }
  }
}
