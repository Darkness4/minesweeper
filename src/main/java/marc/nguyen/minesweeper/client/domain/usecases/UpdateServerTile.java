package marc.nguyen.minesweeper.client.domain.usecases;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.IO;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.domain.repositories.MinefieldRepository;
import marc.nguyen.minesweeper.common.data.models.Tile;
import org.jetbrains.annotations.NotNull;

/** A user should be to update the minefield of the server. */
@Singleton
public class UpdateServerTile implements UseCase<Tile, Completable> {

  private final Lazy<MinefieldRepository> repository;

  @Inject
  public UpdateServerTile(Lazy<MinefieldRepository> repository) {
    this.repository = repository;
  }

  // TODO: May want to use future
  @Override
  public Completable execute(@NotNull Tile params) {
    return repository.get().updateTile(params).observeOn(Schedulers.from(IO.executor));
  }
}
