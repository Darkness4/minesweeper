package marc.nguyen.minesweeper.client.data.repositories;

import dagger.Lazy;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.data.database.SettingsDao;
import marc.nguyen.minesweeper.client.domain.entities.Settings;
import marc.nguyen.minesweeper.client.domain.repositories.SettingsRepository;
import org.jetbrains.annotations.NotNull;

@Singleton
public class SettingsRepositoryImpl implements SettingsRepository {

  final Lazy<SettingsDao> db;

  @Inject
  public SettingsRepositoryImpl(Lazy<SettingsDao> db) {
    this.db = db;
  }

  @Override
  public void save(@NotNull Settings settings) {
    db.get().insert(settings);
  }

  @Override
  public @NotNull Settings findByKey(@NotNull String name) {
    return Objects.requireNonNull(db.get().findByName(name));
  }

  @Override
  public @NotNull List<Settings> findAll() {
    return db.get().findAll();
  }

  @Override
  public void delete(@NotNull String name) {
    db.get().deleteByName(name);
  }
}
