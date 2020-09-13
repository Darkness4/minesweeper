package marc.nguyen.minesweeper.client.data.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import marc.nguyen.minesweeper.client.domain.entities.Settings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SettingsDB {

  final Map<String, Settings> _db = new HashMap<>();

  public void delete(@NotNull String key) {
    _db.remove(key);
  }

  public void insert(@NotNull Settings settings) {
    _db.put(settings.name, settings);
  }

  public List<Settings> findAll() {
    return List.copyOf(_db.values());
  }

  @Nullable
  public Settings find(@NotNull String key) {
    return _db.get(key);
  }
}
