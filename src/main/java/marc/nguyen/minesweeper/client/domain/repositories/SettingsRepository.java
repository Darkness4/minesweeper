package marc.nguyen.minesweeper.client.domain.repositories;

import java.util.List;
import java.util.Optional;
import marc.nguyen.minesweeper.client.domain.entities.Settings;
import org.jetbrains.annotations.NotNull;

public interface SettingsRepository {

  void save(@NotNull Settings settings);

  @NotNull
  Optional<Settings> findByKey(@NotNull String name);

  @NotNull
  List<Settings> findAll();

  void delete(@NotNull String name);
}
