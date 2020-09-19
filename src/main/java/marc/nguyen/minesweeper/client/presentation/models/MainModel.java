package marc.nguyen.minesweeper.client.presentation.models;

import javax.inject.Inject;
import marc.nguyen.minesweeper.client.core.mvc.Model;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Player;

public class MainModel implements Model {

  public final Minefield minefield;
  public final Player player;

  @Inject
  public MainModel(Minefield minefield, Player player) {
    this.minefield = minefield;
    this.player = player;
  }
}
