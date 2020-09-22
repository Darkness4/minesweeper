package marc.nguyen.minesweeper.client.domain.usecases;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.IO;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.domain.repositories.SettingsRepository;
import org.jetbrains.annotations.NotNull;

/** A user should be able to see all the settings available. */
@Singleton
public class FetchAllSettingsName implements UseCase<Void, Single<List<String>>> {

  private final Lazy<SettingsRepository> repository;

  @Inject
  public FetchAllSettingsName(Lazy<SettingsRepository> repository) {
    this.repository = repository;
  }

  @Override
  @NotNull
  public Single<List<String>> execute(Void params) {
    return repository
        .get()
        .findAll()
        .map(
            settingsList ->
                settingsList.stream()
                    .map(settings -> settings.name)
                    .collect(Collectors.toUnmodifiableList()))
        .observeOn(Schedulers.from(IO.executor));
  }
}
