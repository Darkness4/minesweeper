package marc.nguyen.minesweeper.client.domain.usecases;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.IO;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.domain.repositories.PlayerRepository;
import marc.nguyen.minesweeper.common.data.models.Player;
import org.jetbrains.annotations.NotNull;

/** Update the Player model on the server */
@Singleton
public class SendPlayerToServer implements UseCase<Player, Completable> {

  private final Lazy<PlayerRepository> repository;

  @Inject
  public SendPlayerToServer(Lazy<PlayerRepository> repository) {
    this.repository = repository;
  }

  @Override
  @NotNull
  public Completable execute(@NotNull Player params) {
    return repository.get().update(params).observeOn(Schedulers.from(IO.executor));
  }
}
