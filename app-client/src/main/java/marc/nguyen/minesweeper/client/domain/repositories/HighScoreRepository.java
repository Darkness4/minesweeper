package marc.nguyen.minesweeper.client.domain.repositories;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import marc.nguyen.minesweeper.client.domain.entities.HighScore;
import org.jetbrains.annotations.NotNull;

/** HighScore Repository. */
public interface HighScoreRepository {

  /**
   * Save a HighScore into the local database.
   *
   * @param highScore A HighScore.
   * @return A completable.
   */
  @NotNull
  Completable save(@NotNull HighScore highScore);

  /**
   * Get all the HighScore of the local database.
   *
   * @return A Single List of HighScore.
   */
  @NotNull
  Single<List<HighScore>> fetchAll();
}
