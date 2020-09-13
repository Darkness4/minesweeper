package marc.nguyen.minesweeper.client.data.database;

import java.util.List;
import marc.nguyen.minesweeper.client.domain.entities.Settings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SettingsDb {

  void delete(@NotNull String key);

  void insert(@NotNull Settings settings);

  List<Settings> findAll();

  @Nullable
  Settings find(@NotNull String key);
}
