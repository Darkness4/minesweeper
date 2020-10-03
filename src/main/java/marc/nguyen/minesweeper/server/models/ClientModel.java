package marc.nguyen.minesweeper.server.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import marc.nguyen.minesweeper.common.data.models.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ClientModel {
  private final Socket clientSocket;
  private @Nullable Player player;
  private @Nullable ObjectOutputStream outputStream;
  private @Nullable ObjectInputStream inputStream;

  public ClientModel(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

  public @NotNull ObjectOutputStream getOutputStream() throws IOException {
    if (outputStream == null) {
      outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
    }
    return outputStream;
  }

  public @NotNull ObjectInputStream getInputStream() throws IOException {
    if (inputStream == null) {
      inputStream = new ObjectInputStream(clientSocket.getInputStream());
    }
    return inputStream;
  }

  public synchronized void close() {
    try {
      clientSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public synchronized @Nullable Player getPlayer() {
    return player;
  }

  public synchronized void setPlayer(@Nullable Player player) {
    this.player = player;
  }
}
