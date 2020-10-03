package marc.nguyen.minesweeper.client.domain.usecases.connect;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.IO;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.domain.repositories.MinefieldRepository;
import marc.nguyen.minesweeper.common.data.models.Position;
import org.jetbrains.annotations.NotNull;

/** A User should be able to listen to changes from the server. */
@Singleton
public class WatchServerTiles implements UseCase<Void, Observable<Position>> {

  private final Lazy<MinefieldRepository> repository;

  @Inject
  public WatchServerTiles(Lazy<MinefieldRepository> repository) {
    this.repository = repository;
  }

  @Override
  @NotNull
  public Observable<Position> execute(Void params) {
    return repository.get().watchTiles().observeOn(Schedulers.from(IO.executor));
  }
}
