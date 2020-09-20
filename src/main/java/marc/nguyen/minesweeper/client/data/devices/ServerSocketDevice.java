package marc.nguyen.minesweeper.client.data.devices;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
public class ServerSocketDevice {

  @Nullable ObjectOutputStream output = null;

  @Nullable Socket serverSocket = null;

  @Nullable Thread serverWorker = null;

  @Inject
  public ServerSocketDevice() {}

  public void connect(@NotNull InetAddress address, int port) {
    try {
      final var socket = new Socket(address, port);
      this.serverSocket = socket;
      output = new ObjectOutputStream(socket.getOutputStream());
      serverWorker = new Thread(new ServerWorkerRunnable(socket));
      serverWorker.start();
    } catch (IOException e) {
      e.printStackTrace();
      close();
    }
  }

  public void write(Object object) {
    try {
      if (output != null) {
        output.writeObject(object);
      }
    } catch (IOException e) {
      e.printStackTrace();
      close();
    }
  }

  public void close() {
    if (output != null) {
      try {
        output.close();
        output = null;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    if (serverSocket != null) {
      try {
        serverSocket.close();
        serverSocket = null;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
