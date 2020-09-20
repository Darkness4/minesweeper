package marc.nguyen.minesweeper.client.domain.usecases;

import dagger.Lazy;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.domain.repositories.MinefieldRepository;
import marc.nguyen.minesweeper.common.data.models.Tile;
import org.jetbrains.annotations.NotNull;

/** A user should be to update the minefield of the server. */
@Singleton
public class UpdateServerTile implements UseCase<Tile, Void> {
  private final Lazy<MinefieldRepository> repository;

  @Inject
  public UpdateServerTile(Lazy<MinefieldRepository> repository) {
    this.repository = repository;
  }

  @Override
  public Void execute(@NotNull Tile params) {
    repository.get().updateTile(params);
    return null;
  }
}
