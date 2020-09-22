package marc.nguyen.minesweeper.client.domain.usecases;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.IO;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.domain.repositories.MinefieldRepository;
import marc.nguyen.minesweeper.common.data.models.Tile;
import org.jetbrains.annotations.NotNull;

@Singleton
public class WatchServerTiles implements UseCase<Void, Observable<Tile>> {

  private final Lazy<MinefieldRepository> repository;

  @Inject
  public WatchServerTiles(Lazy<MinefieldRepository> repository) {
    this.repository = repository;
  }

  @Override
  @NotNull
  public Observable<Tile> execute(Void params) {
    return repository.get().watchTiles().observeOn(Schedulers.from(IO.executor));
  }
}
