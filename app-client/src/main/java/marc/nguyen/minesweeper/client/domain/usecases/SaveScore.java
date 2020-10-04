package marc.nguyen.minesweeper.client.domain.usecases;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;
import marc.nguyen.minesweeper.client.core.IO;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.domain.entities.HighScore;
import marc.nguyen.minesweeper.client.domain.repositories.HighScoreRepository;
import org.jetbrains.annotations.NotNull;

public class SaveScore implements UseCase<HighScore, Completable> {
  private final Lazy<HighScoreRepository> repository;

  @Inject
  public SaveScore(Lazy<HighScoreRepository> repository) {
    this.repository = repository;
  }

  @Override
  @NotNull
  public Completable execute(@NotNull HighScore params) {
    return repository.get().save(params).observeOn(Schedulers.from(IO.executor));
  }
}
