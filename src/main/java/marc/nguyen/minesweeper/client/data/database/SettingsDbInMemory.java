package marc.nguyen.minesweeper.client.data.database;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.inject.Inject;
import marc.nguyen.minesweeper.client.domain.entities.Settings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SettingsDbInMemory implements SettingsDb {

  @Inject
  public SettingsDbInMemory() {}

  final Map<String, Settings> _db = new ConcurrentHashMap<>();

  @Override
  public void delete(@NotNull String key) {
    _db.remove(key);
  }

  @Override
  public void insert(@NotNull Settings settings) {
    _db.put(settings.name, settings);
  }

  @Override
  public List<Settings> findAll() {
    return _db.values().stream()
        .sorted(Comparator.comparing(a -> a.name.toUpperCase()))
        .collect(Collectors.toUnmodifiableList());
  }

  @Nullable
  @Override
  public Settings find(@NotNull String key) {
    return _db.get(key);
  }
}
