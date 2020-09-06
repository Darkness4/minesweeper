package marc.nguyen.minesweeper.server;

import marc.nguyen.minesweeper.common.models.Minefield;

public class Server {

  public static void main(String[] args) {
    final Minefield minefield = new Minefield(32, 5);

    minefield.placeMines(20);

    System.out.println(minefield);
  }
}
