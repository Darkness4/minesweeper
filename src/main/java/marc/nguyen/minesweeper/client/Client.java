package marc.nguyen.minesweeper.client;

import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.di.components.DaggerMainComponent;
import marc.nguyen.minesweeper.common.data.models.Level;
import marc.nguyen.minesweeper.common.data.models.Minefield;

public final class Client {
  public static void main(String[] args) {
    final var minefield = new Minefield(Level.HARD);
    SwingUtilities.invokeLater(
        () -> DaggerMainComponent.builder().minefield(minefield).build().mainFrame());
  }
}
