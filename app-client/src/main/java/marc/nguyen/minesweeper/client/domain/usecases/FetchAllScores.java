package marc.nguyen.minesweeper.client.domain.usecases;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;
import javax.inject.Inject;
import marc.nguyen.minesweeper.client.core.IO;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.domain.repositories.PlayerRepository;
import marc.nguyen.minesweeper.common.data.models.Player;
import org.jetbrains.annotations.NotNull;

public class FetchAllScores implements UseCase<Void, Single<List<Player>>> {
  private final Lazy<PlayerRepository> repository;

  @Inject
  public FetchAllScores(Lazy<PlayerRepository> repository) {
    this.repository = repository;
  }

  @Override
  @NotNull
  public Single<List<Player>> execute(Void params) {
    return repository.get().fetchAll().observeOn(Schedulers.from(IO.executor));
  }
}
