package marc.nguyen.minesweeper.client.domain.usecases;

import dagger.Lazy;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.domain.repositories.MinefieldRepository;
import marc.nguyen.minesweeper.common.data.models.Tile;
import org.jetbrains.annotations.NotNull;

@Singleton
public class WatchServerTiles implements UseCase<Void, Stream<Tile>> {
  private final Lazy<MinefieldRepository> repository;

  @Inject
  public WatchServerTiles(Lazy<MinefieldRepository> repository) {
    this.repository = repository;
  }

  @Override
  @NotNull
  public Stream<Tile> execute(Void params) {
    return repository.get().watchTiles();
  }
}
