package marc.nguyen.minesweeper.client.domain.repositories;

import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Tile;
import org.jetbrains.annotations.NotNull;

public interface MinefieldClientRepository {
  Minefield fetch();

  void update(@NotNull Minefield minefield);

  void editTile(int x, int y, @NotNull Tile tile);
}
