package marc.nguyen.minesweeper.server.api.workers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import marc.nguyen.minesweeper.common.data.models.Message;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Player;
import marc.nguyen.minesweeper.common.data.models.Tile;
import marc.nguyen.minesweeper.server.core.IO;
import marc.nguyen.minesweeper.server.models.ClientModel;
import org.jetbrains.annotations.Nullable;

/**
 * ServerWorkerRunnable handles the input from the client.
 *
 * <p>This Runnable should be called in a new Thread or in a Thread Pool. Communication made between
 * Client-Server follow Request-Response model.
 */
public class ClientWorkerRunnable implements Runnable {

  private final ClientModel clientModel = new ClientModel();
  private final ConcurrentLinkedQueue<ObjectOutputStream> outputStreams;
  private final AtomicBoolean isStopped = new AtomicBoolean(false);
  private final Minefield minefield;
  private @Nullable ObjectOutputStream output = null;

  public ClientWorkerRunnable(
      Socket clientSocket,
      ConcurrentLinkedQueue<ObjectOutputStream> outputStreams,
      Minefield minefield) {
    this.clientModel.setClientSocket(clientSocket);
    this.outputStreams = outputStreams;
    this.minefield = minefield;
  }

  @Override
  public void run() {
    final var clientSocket = this.clientModel.getClientSocket();
    if (clientSocket != null) {
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
  }

  private void handle(Object packet) {
    if (packet instanceof Tile) {
      minefield.expose((Tile) packet);
      IO.executor.execute(
          () -> {
            if (minefield.hasEnded()) {
              broadcast(new Message("Game has ended")); // TODO: Share score + handle player.
            }
          });
      broadcastExcluding(packet);
      System.out.println(packet);
    } else if (packet instanceof Player) {
      System.out.format("Client is identified as %s!\n", ((Player) packet).name);
      this.clientModel.setPlayer((Player) packet);
    } else if (packet instanceof Message) {
      System.out.printf("Client said: %s\n", packet);
    } else {
      System.out.format("Packet %s not handled\n", packet);
    }
  }

  private void broadcast(Object packet) {
    outputStreams.parallelStream()
        .forEach(
            s -> {
              try {
                s.writeObject(packet);
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
  }

  private void broadcastExcluding(Object packet) {
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
  }
}
