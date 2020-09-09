package marc.nguyen.minesweeper.client.domain.repositories;

import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Tile;

public interface MinefieldClientRepository {
  Minefield get();

  void editTile(int x, int y, Tile tile);
}
