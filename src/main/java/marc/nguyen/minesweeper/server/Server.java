package marc.nguyen.minesweeper.server;

import com.google.inject.Guice;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.server.di.ServerModule;

public final class Server {

  private Server() {}

  public static void main(String[] args) {
    final var injector = Guice.createInjector(new ServerModule());
    final var minefield = new Minefield(10, 10);

    minefield.placeMines(20);

    System.out.println(minefield);
  }
}
