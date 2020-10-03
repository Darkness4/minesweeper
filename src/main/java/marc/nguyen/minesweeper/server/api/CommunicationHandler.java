package marc.nguyen.minesweeper.server.api;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import marc.nguyen.minesweeper.common.data.models.EndGameMessage;
import marc.nguyen.minesweeper.common.data.models.Message;
import marc.nguyen.minesweeper.common.data.models.Player;
import marc.nguyen.minesweeper.server.models.ClientModel;

public class CommunicationHandler extends ConcurrentLinkedQueue<ClientModel> {

  public synchronized void broadcast(Object packet) {
    this.parallelStream()
        .forEach(
            clientModel -> {
              try {
                clientModel.getOutputStream().writeObject(packet);
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
  }

  public synchronized List<Player> getPlayers() {
    return this.stream()
        .filter(c -> c.getPlayer() != null)
        .map(ClientModel::getPlayer)
        .collect(Collectors.toUnmodifiableList());
  }

  public synchronized void broadcastClose() {
    this.parallelStream()
        .forEach(
            clientModel -> {
              clientModel.close();
              System.out.println("A thread has been closed.");
            });
  }

  public synchronized void broadcastEndGame() {
    fetchBestPlayer()
        .ifPresentOrElse(
            (bestPlayer) ->
                broadcast(
                    new EndGameMessage(
                        "Game has ended. Best player was "
                            + bestPlayer.name
                            + " with "
                            + bestPlayer.getScore()
                            + " score.")),
            () -> broadcast(new Message("Game has ended.")));
  }

  public synchronized void broadcastListOfPlayer() {
    final List<Player> players =
        this.stream()
            .filter(c -> c.getPlayer() != null)
            .map(ClientModel::getPlayer)
            .collect(Collectors.toUnmodifiableList());
    broadcast(players);
  }

  public synchronized void broadcastExcluding(
      Predicate<? super ClientModel> predicate, Object packet) {
    this.parallelStream()
        .filter(predicate)
        .forEach(
            c -> {
              try {
                c.getOutputStream().writeObject(packet);
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
  }

  public synchronized Optional<Player> fetchBestPlayer() {
    return this.stream()
        .map(ClientModel::getPlayer)
        .filter(Objects::nonNull)
        .min((a, b) -> b.getScore() - a.getScore());
  }

  public synchronized void kick(ClientModel clientModel) {
    try {
      clientModel.getOutputStream().close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.remove(clientModel);
  }

  public synchronized void kickEveryone() {
    this.forEach(ClientModel::close);
    this.clear();
  }
}
