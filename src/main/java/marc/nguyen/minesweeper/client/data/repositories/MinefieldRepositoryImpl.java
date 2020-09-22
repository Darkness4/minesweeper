package marc.nguyen.minesweeper.client.data.repositories;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import javax.inject.Inject;
import marc.nguyen.minesweeper.client.data.devices.ServerSocketDevice;
import marc.nguyen.minesweeper.client.domain.repositories.MinefieldRepository;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Tile;
import org.jetbrains.annotations.NotNull;

public class MinefieldRepositoryImpl implements MinefieldRepository {

  private final ServerSocketDevice serverSocketDevice;

  @Inject
  public MinefieldRepositoryImpl(ServerSocketDevice serverSocketDevice) {
    this.serverSocketDevice = serverSocketDevice;
  }

  @Override
  public Maybe<Minefield> fetch() {
    final var observable = serverSocketDevice.getObservable();
    if (observable != null) {
      return Maybe.fromObservable(
          observable.filter((e) -> e instanceof Minefield).map((e) -> (Minefield) e));
    } else {
      return Maybe.empty();
    }
  }

  @Override
  public Observable<Tile> watchTiles() {
    final var observable = serverSocketDevice.getObservable();
    if (observable != null) {
      return observable.filter((e) -> e instanceof Tile).map((e) -> (Tile) e);
    } else {
      return Observable.empty();
    }
  }

  @Override
  public Completable updateTile(@NotNull Tile tile) {
    return Completable.fromAction(() -> serverSocketDevice.write(tile));
  }
}
