package marc.nguyen.minesweeper.client.domain.usecases;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;
import javax.inject.Inject;
import marc.nguyen.minesweeper.client.core.IO;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.domain.entities.HighScore;
import marc.nguyen.minesweeper.client.domain.repositories.HighScoreRepository;
import org.jetbrains.annotations.NotNull;

public class FetchAllScores implements UseCase<Void, Single<List<HighScore>>> {
  private final Lazy<HighScoreRepository> repository;

  @Inject
  public FetchAllScores(Lazy<HighScoreRepository> repository) {
    this.repository = repository;
  }

  @Override
  @NotNull
  public Single<List<HighScore>> execute(Void params) {
    return repository.get().findAll().observeOn(Schedulers.from(IO.executor));
  }
}
