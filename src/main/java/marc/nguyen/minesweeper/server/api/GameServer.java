package marc.nguyen.minesweeper.server.api;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import marc.nguyen.minesweeper.server.api.workers.ClientWorkerRunnable;

/** Implementation of the Server. */
public class GameServer {
  private ServerSocket serverSocket = null;
  private boolean isStopped = false;
  private final ExecutorService threadPool = Executors.newFixedThreadPool(10);

  /**
   * Start the server at the desired port.
   *
   * @param port TCP Port.
   */
  public void start(int port) {
    open(port);

    System.out.format("Server is running on port %d\n", port);

    while (!isStopped()) {
      try {
        final var clientSocket = serverSocket.accept();
        System.out.printf(
            "Client %s accepted.\n", clientSocket.getInetAddress().getCanonicalHostName());
        this.threadPool.execute(new ClientWorkerRunnable(clientSocket));
      } catch (IOException e) {
        if (isStopped()) {
          System.out.println("Server Stopped.");
          break;
        }
        throw new RuntimeException("Error accepting client connection", e);
      }
    }
    this.threadPool.shutdown();
    System.out.println("Server Stopped.");
  }

  private synchronized boolean isStopped() {
    return isStopped;
  }

  /** Close the server. */
  public synchronized void close() {
    try {
      if (serverSocket != null) {
        isStopped = true;
        serverSocket.close();
        serverSocket = null;
      }
    } catch (IOException e) {
      throw new RuntimeException("Cannot stop server. Cannot close port.", e);
    }
  }

  private void open(int port) {
    try {
      serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      throw new RuntimeException("Cannot open port.", e);
    }
  }
}
