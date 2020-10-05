package marc.nguyen.minesweeper.client.data.repositories;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import javax.inject.Inject;
import marc.nguyen.minesweeper.client.data.database.HighScoreDao;
import marc.nguyen.minesweeper.client.domain.entities.HighScore;
import marc.nguyen.minesweeper.client.domain.repositories.HighScoreRepository;
import org.jetbrains.annotations.NotNull;

public class HighScoreRepositoryImpl implements HighScoreRepository {

  private final Lazy<HighScoreDao> highScoreDao;

  @Inject
  public HighScoreRepositoryImpl(Lazy<HighScoreDao> highScoreDao) {
    this.highScoreDao = highScoreDao;
  }

  @Override
  public @NotNull Completable save(@NotNull HighScore highScore) {
    return Completable.fromAction(() -> highScoreDao.get().insert(highScore));
  }

  @Override
  public @NotNull Single<List<HighScore>> findAll() {
    return Single.fromCallable(() -> highScoreDao.get().findAll());
  }
}
