package marc.nguyen.minesweeper.client.data.repositories;

import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import marc.nguyen.minesweeper.client.data.database.SettingsDB;
import marc.nguyen.minesweeper.client.domain.entities.Settings;
import marc.nguyen.minesweeper.client.domain.repositories.SettingsRepository;
import org.jetbrains.annotations.NotNull;

public class SettingsRepositoryImpl implements SettingsRepository {
  final SettingsDB db;

  @Inject
  public SettingsRepositoryImpl(SettingsDB db) {
    this.db = db;
  }

  @Override
  public void save(@NotNull Settings settings) {
    db.insert(settings);
  }

  @Override
  public @NotNull Settings findByKey(@NotNull String name) {
    return Objects.requireNonNull(db.find(name));
  }

  @Override
  public @NotNull List<Settings> findAll() {
    return db.findAll();
  }

  @Override
  public void delete(@NotNull String name) {
    db.delete(name);
  }
}
