package marc.nguyen.minesweeper.server.api.workers;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import marc.nguyen.minesweeper.common.data.models.Message;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Player;
import marc.nguyen.minesweeper.common.data.models.Position;
import marc.nguyen.minesweeper.common.data.models.StartGame;
import marc.nguyen.minesweeper.common.data.models.Tile;
import marc.nguyen.minesweeper.server.api.CommunicationHandler;
import marc.nguyen.minesweeper.server.models.ClientModel;

/**
 * ServerWorkerRunnable handles the input from the client.
 *
 * <p>This Runnable should be called in a new Thread or in a Thread Pool. Communication made between
 * Client-Server follow Request-Response model.
 */
public class ClientWorkerRunnable implements Runnable {

  private final ClientModel clientModel;
  private final CommunicationHandler communicationHandler;
  private final AtomicBoolean isStopped = new AtomicBoolean(false);
  private final Minefield minefield;
  private final int maxPlayers;

  public ClientWorkerRunnable(
      ClientModel clientModel,
      CommunicationHandler communicationHandler,
      Minefield minefield,
      int maxPlayers) {
    this.clientModel = clientModel;
    this.minefield = minefield;
    this.communicationHandler = communicationHandler;
    this.maxPlayers = maxPlayers;
  }

  @Override
  public void run() {
    try (final var input = clientModel.getInputStream();
        final var output = clientModel.getOutputStream()) {

      // Send initial minefield
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
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      communicationHandler.kick(clientModel);
    }
  }

  private void handle(Object packet) {
    final var player = clientModel.getPlayer();

    if (packet instanceof Position) {
      minefield.expose((Position) packet);
      final var tile = minefield.get((Position) packet);
      if (player != null) {
        if (tile instanceof Tile.Mine) {
          clientModel.setPlayer(player.copyWith(player.getScore() - 1));
        } else if (tile instanceof Tile.Empty) {
          clientModel.setPlayer(player.copyWith(player.getScore() + 1));
        }
      }

      communicationHandler.broadcastExcluding(c -> c != clientModel, packet);
      communicationHandler.broadcastListOfPlayer();

      if (minefield.hasEnded()) {
        communicationHandler.broadcastEndGame();
        minefield.rebuild();
        communicationHandler.kickEveryone();
      }
      System.out.printf("[DEBUG] %s\n", packet);

    } else if (packet instanceof Player) {
      this.clientModel.setPlayer((Player) packet);
      System.out.format("Client is identified as %s!\n", ((Player) packet).name);
      communicationHandler.broadcast(
          new Message(String.format("%s logged in the server.\n", ((Player) packet).name)));
      communicationHandler.broadcastListOfPlayer();

      if (communicationHandler.getPlayers().size() == maxPlayers) {
        startGame();
      }
    } else if (packet instanceof Message) {
      System.out.printf("Client said: %s\n", packet);
    } else {
      System.out.format("Packet %s not handled\n", packet);
    }
  }

  private void startGame() {
    System.out.println("Game start !");
    communicationHandler.broadcast(new StartGame());
    communicationHandler.broadcastListOfPlayer();
  }
}
