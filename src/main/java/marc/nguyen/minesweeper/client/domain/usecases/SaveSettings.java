package marc.nguyen.minesweeper.client.domain.usecases;

import javax.inject.Inject;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.domain.entities.Settings;
import marc.nguyen.minesweeper.client.domain.repositories.SettingsRepository;
import org.jetbrains.annotations.NotNull;

public class SaveSettings implements UseCase<Settings, Void> {
  final SettingsRepository repository;

  @Inject
  public SaveSettings(SettingsRepository repository) {
    this.repository = repository;
  }

  @Override
  public Void execute(@NotNull Settings params) {
    repository.save(params);
    return null;
  }
}
