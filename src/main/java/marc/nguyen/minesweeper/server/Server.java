package marc.nguyen.minesweeper.server;

import marc.nguyen.minesweeper.common.data.models.Minefield;

public final class Server {

  private Server() {}

  public static void main(String[] args) {
    final var minefield = new Minefield(10, 10);

    minefield.placeMines(20);

    System.out.println(minefield);
  }
}
