package marc.nguyen.minesweeper.server.api.workers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientWorkerRunnable implements Runnable {
  protected Socket clientSocket;

  public ClientWorkerRunnable(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

  public void run() {
    try {
      final ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
      final ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());

      output.close();
      input.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
