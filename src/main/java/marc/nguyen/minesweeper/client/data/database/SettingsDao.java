package marc.nguyen.minesweeper.client.data.database;

import java.util.List;
import java.util.Optional;
import marc.nguyen.minesweeper.client.domain.entities.Settings;
import org.jetbrains.annotations.NotNull;

public interface SettingsDao {

  void deleteByName(@NotNull String name);

  void insert(@NotNull Settings settings);

  List<Settings> findAll();

  @NotNull
  Optional<Settings> findByName(@NotNull String key);
}
