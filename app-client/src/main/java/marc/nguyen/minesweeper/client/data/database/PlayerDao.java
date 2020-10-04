package marc.nguyen.minesweeper.client.data.database;

import java.util.List;
import marc.nguyen.minesweeper.common.data.models.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PlayerDao {

  /**
   * Insert a Player in the DB.
   *
   * @param player Player to be inserted.
   */
  void insert(@NotNull Player player);

  /**
   * Find all the Player.
   *
   * @return A List of Player. Empty if not found.
   */
  @NotNull
  List<Player> findAll();

  /**
   * @param name Name of the player.
   * @return A Player. If not found, is null.
   */
  @Nullable
  Player findByName(@NotNull String name);
}
