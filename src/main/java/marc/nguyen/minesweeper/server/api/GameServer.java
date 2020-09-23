package marc.nguyen.minesweeper.server.api;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import marc.nguyen.minesweeper.server.api.workers.ClientWorkerRunnable;
import marc.nguyen.minesweeper.server.core.IO;
import org.jetbrains.annotations.Nullable;

/** Implementation of the Server. */
public class GameServer {

  private final AtomicBoolean isStopped = new AtomicBoolean(false);
  private final ConcurrentLinkedQueue<ObjectOutputStream> outputStreams =
      new ConcurrentLinkedQueue<>();
  private @Nullable ServerSocket serverSocket = null;

  /**
   * Start the server at the desired port.
   *
   * @param port TCP Port.
   */
  public void start(int port) {
    open(port);

    System.out.format("Server is running on port %d\n", port);

    while (!isStopped.get()) {
      try {
        assert serverSocket != null;
        final var clientSocket = serverSocket.accept();
        System.out.printf(
            "Client %s accepted.\n", clientSocket.getInetAddress().getCanonicalHostName());
        CompletableFuture.runAsync(
            new ClientWorkerRunnable(clientSocket, outputStreams), IO.executor);
      } catch (IOException e) {
        if (isStopped.get()) {
          System.out.println("Server Stopped.");
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

    if (serverSocket != null) {
      try {
        serverSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    outputStreams.parallelStream()
        .forEach(
            s -> {
              try {
                s.close();
              } catch (IOException ioException) {
                ioException.printStackTrace();
              }
            });
  }
}
