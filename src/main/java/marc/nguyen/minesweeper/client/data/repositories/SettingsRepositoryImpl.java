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

  final Lazy<SettingsDao> dao;

  @Inject
  public SettingsRepositoryImpl(Lazy<SettingsDao> dao) {
    this.dao = dao;
  }

  @Override
  public void save(@NotNull Settings settings) {
    dao.get().insert(settings);
  }

  @Override
  public @NotNull Settings findByKey(@NotNull String name) {
    return Objects.requireNonNull(dao.get().findByName(name));
  }

  @Override
  public @NotNull List<Settings> findAll() {
    return dao.get().findAll();
  }

  @Override
  public void delete(@NotNull String name) {
    dao.get().deleteByName(name);
  }
}
