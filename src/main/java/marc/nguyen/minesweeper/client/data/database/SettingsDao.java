package marc.nguyen.minesweeper.client.data.database;

import java.util.List;
import marc.nguyen.minesweeper.client.domain.entities.Settings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SettingsDao {

  void deleteByName(@NotNull String name);

  void insert(@NotNull Settings settings);

  List<Settings> findAll();

  @Nullable
  Settings findByName(@NotNull String key);
}
