package marc.nguyen.minesweeper.client.domain.usecases;

import javax.inject.Inject;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.domain.repositories.SettingsRepository;
import org.jetbrains.annotations.NotNull;

public class DeleteSettings implements UseCase<String, Void> {

  final SettingsRepository repository;

  @Inject
  public DeleteSettings(SettingsRepository repository) {
    this.repository = repository;
  }

  @Override
  public Void execute(@NotNull String name) {
    repository.delete(name);
    return null;
  }
}
