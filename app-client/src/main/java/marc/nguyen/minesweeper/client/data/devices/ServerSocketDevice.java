package marc.nguyen.minesweeper.client.data.devices;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.IO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * ServerSocketDevice handles the communication between the client and the server.
 *
 * <p>The communication can be started using <code>connect(address, port)</code>. This will open a
 * socket and a Subject.
 *
 * <p>This class is following the Observer Pattern. You should use <code>getObservable()
 * </code> to be able to listen to incoming packets. Note that it is nullable based on the result of
 * <code>connect</code>.
 *
 * <p>To fire a message use <code>write(object)</code>.
 *
 * <p>Do not forget to <code>close()</code>.
 */
@Singleton
public class ServerSocketDevice {

  @Nullable private ObjectOutputStream output = null;

  @Nullable private Socket serverSocket = null;

  @Nullable private Subject<Object> publisher = null;

  @Inject
  public ServerSocketDevice() {}

  /** @return Incoming Packets. Is null if socket is closed. */
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
      serverSocket = new Socket(address, port);
      output = new ObjectOutputStream(serverSocket.getOutputStream());
      publisher = BehaviorSubject.create();
      IO.executor.execute(new ServerWorkerRunnable(serverSocket, publisher));
    } catch (IOException e) {
      close();
      throw e;
    }
  }

  /**
   * Send a packet.
   *
   * <p>The packet should be serializable, including members of that packet.
   *
   * @param object A packet to be sent.
   */
  public void write(Serializable object) {
    try {
      if (serverSocket != null && serverSocket.isConnected()) {
        if (output != null) {
          output.writeObject(object);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      close();
    }
  }

  /** Close socket, listener and thread. */
  public void close() {
    if (output != null) {
      try {
        output.close();
        output = null;
        System.out.println("Output stream closed.");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    if (serverSocket != null) {
      try {
        serverSocket.close();
        serverSocket = null;
        System.out.println("Server socket closed.");
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
