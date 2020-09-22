package marc.nguyen.minesweeper.client.data.devices;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.IO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
public class ServerSocketDevice {

  @Nullable private ObjectOutputStream output = null;

  @Nullable private Socket serverSocket = null;

  @Nullable private PublishSubject<Object> publisher = null;

  @Inject
  public ServerSocketDevice() {}

  @Nullable
  public Observable<Object> getObservable() {
    return publisher;
  }

  /**
   * @param address TCP IP Address
   * @param port TCP Port
   * @throws IOException Throws when connection error.
   */
  public void connect(@NotNull InetAddress address, int port) throws IOException {
    try {
      final var socket = new Socket(address, port);
      this.serverSocket = socket;
      output = new ObjectOutputStream(socket.getOutputStream());
      publisher = PublishSubject.create();
      CompletableFuture.runAsync(new ServerWorkerRunnable(socket, publisher), IO.executor);
    } catch (IOException e) {
      close();
      throw e;
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

    if (publisher != null) {
      publisher.onComplete();
      publisher = null;
    }
  }
}
