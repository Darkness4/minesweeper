package marc.nguyen.minesweeper.server.models;

import java.net.Socket;
import marc.nguyen.minesweeper.common.data.models.Player;
import org.jetbrains.annotations.Nullable;

public class ClientModel {

  private @Nullable Socket clientSocket;
  private @Nullable Player player;

  public @Nullable Socket getClientSocket() {
    return clientSocket;
  }

  public void setClientSocket(@Nullable Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

  public @Nullable Player getPlayer() {
    return player;
  }

  public void setPlayer(@Nullable Player player) {
    this.player = player;
  }
}
