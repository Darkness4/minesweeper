package marc.nguyen.minesweeper.server.api.workers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import marc.nguyen.minesweeper.common.data.models.Message;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Tile;
import org.jetbrains.annotations.Nullable;

/**
 * ServerWorkerRunnable handles the input from the client.
 *
 * <p>This Runnable should be called in a new Thread or in a Thread Pool. Communication made between
 * Client-Server follow Request-Response model.
 */
public class ClientWorkerRunnable implements Runnable {

  private final Socket clientSocket;
  private final ConcurrentLinkedQueue<ObjectOutputStream> outputStreams;
  private final AtomicBoolean isStopped = new AtomicBoolean(false);
  private final Minefield minefield;
  private @Nullable ObjectOutputStream output = null;

  public ClientWorkerRunnable(
      Socket clientSocket,
      ConcurrentLinkedQueue<ObjectOutputStream> outputStreams,
      Minefield minefield) {
    this.clientSocket = clientSocket;
    this.outputStreams = outputStreams;
    this.minefield = minefield;
  }

  @Override
  public void run() {
    try (final var input = new ObjectInputStream(clientSocket.getInputStream());
        final var output = new ObjectOutputStream(clientSocket.getOutputStream())) {

      this.output = output;
      outputStreams.add(output);

      output.writeObject(new Message("Hello client !"));
      output.writeObject(minefield);
      while (!isStopped.get()) {
        try {
          final var packet = input.readObject();
          handle(packet);
        } catch (IOException | ClassNotFoundException e) {
          // Client disconnected
          System.out.println(e);
          isStopped.set(true);
        }
      }

      outputStreams.remove(output);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  void handle(Object packet) {
    if (packet instanceof Tile) {
      minefield.expose((Tile) packet);
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
