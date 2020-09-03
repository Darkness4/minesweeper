package marc.nguyen.minesweeper;

import marc.nguyen.minesweeper.models.Minefield;

class Main {

  public static void main(String[] args) {
    final Minefield minefield = new Minefield(32, 5);

    minefield.placeMines();

    System.out.println(minefield);
  }
}
