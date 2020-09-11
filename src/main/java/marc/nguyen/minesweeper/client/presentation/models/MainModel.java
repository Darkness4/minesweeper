package marc.nguyen.minesweeper.client.presentation.models;

import marc.nguyen.minesweeper.client.core.mvc.Model;
import marc.nguyen.minesweeper.common.data.models.Minefield;

public class MainModel implements Model {
  public final Minefield minefield;

  public MainModel(Minefield minefield) {
    this.minefield = minefield;
  }
}
