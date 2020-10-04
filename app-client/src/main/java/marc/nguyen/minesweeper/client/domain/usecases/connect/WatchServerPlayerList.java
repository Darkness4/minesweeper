package marc.nguyen.minesweeper.client.domain.usecases.connect;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.IO;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.domain.repositories.PlayerRepository;
import marc.nguyen.minesweeper.common.data.models.Player;
import org.jetbrains.annotations.NotNull;

/** A User should be able to watch the other players score. */
@Singleton
public class WatchServerPlayerList implements UseCase<Void, Observable<List<Player>>> {

  private final Lazy<PlayerRepository> repository;

  @Inject
  public WatchServerPlayerList(Lazy<PlayerRepository> repository) {
    this.repository = repository;
  }

  @Override
  @NotNull
  public Observable<List<Player>> execute(Void params) {
    return repository.get().fetchServerPlayerList().observeOn(Schedulers.from(IO.executor));
  }
}
