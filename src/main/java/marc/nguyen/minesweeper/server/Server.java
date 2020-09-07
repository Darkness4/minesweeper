package marc.nguyen.minesweeper.server;

import com.google.inject.Guice;
import marc.nguyen.minesweeper.common.models.Minefield;
import marc.nguyen.minesweeper.server.di.ServerModule;

public class Server {

  public static void main(String[] args) {
    final var injector = Guice.createInjector(new ServerModule());
    final var minefield = new Minefield(32, 5);

    minefield.placeMines(20);

    System.out.println(minefield);
  }
}
