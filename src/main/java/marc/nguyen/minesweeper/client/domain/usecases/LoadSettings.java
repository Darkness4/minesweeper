package marc.nguyen.minesweeper.client.domain.usecases;

import javax.inject.Inject;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.domain.entities.Settings;
import marc.nguyen.minesweeper.client.domain.repositories.SettingsRepository;
import org.jetbrains.annotations.NotNull;

public class LoadSettings implements UseCase<String, Settings> {

  final SettingsRepository repository;

  @Inject
  public LoadSettings(SettingsRepository repository) {
    this.repository = repository;
  }

  @Override
  public @NotNull Settings execute(@NotNull String name) {
    return repository.findByKey(name);
  }
}
