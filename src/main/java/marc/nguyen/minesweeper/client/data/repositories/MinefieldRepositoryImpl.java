package marc.nguyen.minesweeper.client.data.repositories;

import java.util.Optional;
import java.util.stream.Stream;
import javax.inject.Inject;
import marc.nguyen.minesweeper.client.data.devices.ServerSocketDevice;
import marc.nguyen.minesweeper.client.domain.repositories.MinefieldRepository;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Tile;
import org.jetbrains.annotations.NotNull;

public class MinefieldRepositoryImpl implements MinefieldRepository {

  final ServerSocketDevice serverSocketDevice;

  @Inject
  public MinefieldRepositoryImpl(ServerSocketDevice serverSocketDevice) {
    this.serverSocketDevice = serverSocketDevice;
  }

  @Override
  public Optional<Minefield> fetch() {
    // TODO: Not implemented yet.
    // TODO: Consume here
    return null;
  }

  @Override
  public Stream<Tile> watchTiles() {
    // TODO: Not implemented yet.
    return null;
  }

  @Override
  public void updateTile(@NotNull Tile tile) {
    serverSocketDevice.write(tile);
  }
}
