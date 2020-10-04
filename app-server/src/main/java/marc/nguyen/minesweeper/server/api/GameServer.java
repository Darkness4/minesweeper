package marc.nguyen.minesweeper.server.api;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import marc.nguyen.minesweeper.common.data.models.Message;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.StartGame;
import marc.nguyen.minesweeper.server.api.workers.ClientWorkerRunnable;
import marc.nguyen.minesweeper.server.core.IO;
import marc.nguyen.minesweeper.server.models.ClientModel;
import org.jetbrains.annotations.Nullable;

/** Implementation of the Server. */
public class GameServer {

  private final AtomicBoolean isStopped = new AtomicBoolean(false);
  private final CommunicationHandler communicationHandler = new CommunicationHandler();
  private final Minefield minefield;
  private final int maxPlayers;
  private final long timeout;
  private @Nullable ServerSocket serverSocket = null;
  private @Nullable Timer timeoutTimer = null;

  public GameServer(Minefield minefield, int maxPlayers, long timeout) {
    this.minefield = minefield;
    this.maxPlayers = maxPlayers;
    this.timeout = timeout * 1000; // Convert seconds to millis
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

        // (Blocking IO) Waiting for players
        final var clientSocket = serverSocket.accept();

        final var clientModel = new ClientModel(clientSocket);

        if (communicationHandler.size() >= maxPlayers) {
          System.out.printf(
              "Client %s refused: Max players reached.\n",
              clientSocket.getInetAddress().getCanonicalHostName());
          clientModel.getInputStream();
          clientModel.getOutputStream().writeObject(new Message("Max players reached."));
          clientModel.close();
          continue;
        }

        System.out.printf(
            "Client %s accepted.\n", clientSocket.getInetAddress().getCanonicalHostName());

        communicationHandler.add(clientModel);

        // On first person connected.
        if (communicationHandler.size() == 1) {
          launchTimeoutTimer();
          minefield.rebuild();
        }

        IO.executor.execute(
            new ClientWorkerRunnable(clientModel, communicationHandler, minefield, maxPlayers));

        // Stop timeout if enough players
        if (communicationHandler.size() == maxPlayers) {
          if (timeoutTimer != null) {
            timeoutTimer.cancel();
            timeoutTimer = null;
          }
        }

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

  private void startGame() {
    System.out.println("Game start !");
    communicationHandler.broadcast(new StartGame());
    communicationHandler.broadcastListOfPlayer();
  }

  private void launchTimeoutTimer() {
    if (timeoutTimer != null) {
      timeoutTimer.cancel();
      timeoutTimer = null;
    }
    timeoutTimer = new Timer("Timeout in x seconds timer");
    timeoutTimer.schedule(
        new TimerTask() {
          @Override
          public void run() {
            startGame();
          }
        },
        timeout);
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

    if (timeoutTimer != null) {
      timeoutTimer.cancel();
      timeoutTimer = null;
    }

    communicationHandler.broadcastClose();
  }
}
