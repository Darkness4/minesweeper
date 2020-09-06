package marc.nguyen.minesweeper.client;

import com.google.inject.Guice;
import marc.nguyen.minesweeper.client.di.ClientModule;

public class Client {
  public static void main(String[] args) {
    final var injector = Guice.createInjector(new ClientModule());
    throw new UnsupportedOperationException("Not implemented yet.");
  }
}
