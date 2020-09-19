package marc.nguyen.minesweeper.client.domain.usecases;

import dagger.Lazy;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.domain.repositories.SettingsRepository;
import org.jetbrains.annotations.NotNull;

/** A user should be able to see all the settings available. */
@Singleton
public class FetchAllSettingsName implements UseCase<Void, List<String>> {
  final Lazy<SettingsRepository> repository;

  @Inject
  public FetchAllSettingsName(Lazy<SettingsRepository> repository) {
    this.repository = repository;
  }

  @Override
  public @NotNull List<String> execute(Void params) {

    return repository.get().findAll().stream()
        .map(settings -> settings.name)
        .collect(Collectors.toUnmodifiableList());
  }
}
