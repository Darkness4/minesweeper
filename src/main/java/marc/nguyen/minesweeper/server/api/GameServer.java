package marc.nguyen.minesweeper.server.api;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import marc.nguyen.minesweeper.common.data.models.Level;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.server.api.workers.ClientWorkerRunnable;
import marc.nguyen.minesweeper.server.core.IO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Implementation of the Server. */
public class GameServer {

  private final AtomicBoolean isStopped = new AtomicBoolean(false);
  private final ConcurrentLinkedQueue<ObjectOutputStream> outputStreams =
      new ConcurrentLinkedQueue<>();
  private @Nullable ServerSocket serverSocket = null;

  private final Minefield minefield;

  public GameServer(int length, int height, int mines) {
    minefield = new Minefield(length, height, mines, false);
  }

  public GameServer(@NotNull Level level) {
    minefield = new Minefield(level, false);
  }

  /**
   * Start the server at the desired port.
   *
   * @param port TCP Port.
   */
  public void start(int port) {
    open(port);

    System.out.format("Server is running on port %d\n", port);
    System.out.printf(
        "Settings is : %d x %d with %s mines\n",
        minefield.getLength(), minefield.getHeight(), minefield.getMinesOnField());

    while (!isStopped.get()) {
      try {
        assert serverSocket != null;
        final var clientSocket = serverSocket.accept();
        System.out.printf(
            "Client %s accepted.\n", clientSocket.getInetAddress().getCanonicalHostName());
        CompletableFuture.runAsync(
            new ClientWorkerRunnable(clientSocket, outputStreams, minefield), IO.executor);
      } catch (IOException e) {
        if (isStopped.get()) {
          break;
        }
        throw new RuntimeException("Error accepting client connection", e);
      }
    }
    close();
    System.out.println("Server Stopped.");
  }

  private void open(int port) {
    try {
      serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      throw new RuntimeException("Cannot open port.", e);
    }
  }

  private synchronized void close() {
    IO.executor.shutdown();
    System.out.println("Thread pool has been shut down.");

    if (serverSocket != null) {
      try {
        serverSocket.close();
        System.out.println("Server socket has closed.");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    outputStreams.parallelStream()
        .forEach(
            s -> {
              try {
                s.close();
                System.out.println("A thread has been closed.");
              } catch (IOException ioException) {
                ioException.printStackTrace();
              }
            });
  }
}
