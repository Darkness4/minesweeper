package marc.nguyen.minesweeper.client.domain.usecases;

import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.domain.repositories.SettingsRepository;
import org.jetbrains.annotations.NotNull;

public class FetchAllSettingsName implements UseCase<Void, List<String>> {
  final SettingsRepository repository;

  @Inject
  public FetchAllSettingsName(SettingsRepository repository) {
    this.repository = repository;
  }

  @Override
  public @NotNull List<String> execute(Void params) {

    return repository.findAll().stream()
        .map(settings -> settings.name)
        .collect(Collectors.toUnmodifiableList());
  }
}
