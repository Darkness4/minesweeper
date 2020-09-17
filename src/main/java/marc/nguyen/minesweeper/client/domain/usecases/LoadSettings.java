package marc.nguyen.minesweeper.client.domain.usecases;

import dagger.Lazy;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.domain.entities.Settings;
import marc.nguyen.minesweeper.client.domain.repositories.SettingsRepository;
import org.jetbrains.annotations.NotNull;

@Singleton
public class LoadSettings implements UseCase<String, Settings> {

  final Lazy<SettingsRepository> repository;

  @Inject
  public LoadSettings(Lazy<SettingsRepository> repository) {
    this.repository = repository;
  }

  @Override
  public @NotNull Settings execute(@NotNull String name) {
    return repository.get().findByKey(name);
  }
}
