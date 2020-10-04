package marc.nguyen.minesweeper.client.data.repositories;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import javax.inject.Inject;
import marc.nguyen.minesweeper.client.data.devices.ServerSocketDevice;
import marc.nguyen.minesweeper.client.domain.repositories.MinefieldRepository;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Position;
import org.jetbrains.annotations.NotNull;

/** Implementation of the MinefieldRepository. */
public class MinefieldRepositoryImpl implements MinefieldRepository {

  private final Lazy<ServerSocketDevice> serverSocketDevice;

  @Inject
  public MinefieldRepositoryImpl(Lazy<ServerSocketDevice> serverSocketDevice) {
    this.serverSocketDevice = serverSocketDevice;
  }

  /**
   * {@inheritDoc}
   *
   * @return Maybe a Minefield.
   */
  @Override
  public Maybe<Minefield> fetch() {
    final var observable = serverSocketDevice.get().getObservable();
    if (observable != null) {
      return Maybe.fromObservable(
          observable.filter((e) -> e instanceof Minefield).map((e) -> (Minefield) e));
    } else {
      return Maybe.empty();
    }
  }

  /**
   * {@inheritDoc}
   *
   * @return Stream of tiles from the server.
   */
  @Override
  public Observable<Position> watchTiles() {
    final var observable = serverSocketDevice.get().getObservable();
    if (observable != null) {
      return observable.filter((e) -> e instanceof Position).map((e) -> (Position) e);
    } else {
      return Observable.empty();
    }
  }

  /**
   * {@inheritDoc}
   *
   * @param position New Position.
   * @return A completable.
   */
  @Override
  public Completable updateTile(@NotNull Position position) {
    return Completable.fromAction(() -> serverSocketDevice.get().write(position));
  }
}
