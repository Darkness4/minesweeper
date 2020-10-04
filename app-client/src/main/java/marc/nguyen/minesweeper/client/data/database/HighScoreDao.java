package marc.nguyen.minesweeper.client.data.database;

import java.util.List;
import marc.nguyen.minesweeper.client.domain.entities.HighScore;
import org.jetbrains.annotations.NotNull;

public interface HighScoreDao {

  /**
   * Insert a HighScore in the DB.
   *
   * @param highScore HighScore to be inserted.
   */
  void insert(@NotNull HighScore highScore);

  /**
   * Find all the HighScore.
   *
   * @return A List of HighScore. Empty if not found.
   */
  @NotNull
  List<HighScore> findAll();
}
