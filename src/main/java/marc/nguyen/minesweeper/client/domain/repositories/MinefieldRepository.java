package marc.nguyen.minesweeper.client.domain.repositories;

import java.util.Optional;
import java.util.stream.Stream;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Tile;
import org.jetbrains.annotations.NotNull;

public interface MinefieldRepository {

  Optional<Minefield> fetch();

  Stream<Tile> watchTiles();

  void updateTile(@NotNull Tile tile);
}
