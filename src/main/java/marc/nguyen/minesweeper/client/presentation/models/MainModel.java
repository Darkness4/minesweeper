package marc.nguyen.minesweeper.client.presentation.models;

import io.reactivex.rxjava3.core.Observable;
import javax.inject.Inject;
import marc.nguyen.minesweeper.client.core.mvc.Model;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Player;
import marc.nguyen.minesweeper.common.data.models.Tile;

public class MainModel implements Model {

  public final Minefield minefield;
  public final Player player;
  public final Observable<Tile> tiles;

  @Inject
  public MainModel(Minefield minefield, Player player, Observable<Tile> tiles) {
    this.minefield = minefield;
    this.player = player;
    this.tiles = tiles;
  }
}
