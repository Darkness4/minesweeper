package marc.nguyen.minesweeper.client.domain.usecases;

import javax.inject.Inject;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import org.jetbrains.annotations.NotNull;

public class UpdateMinefield implements UseCase<Minefield, Void> {

  @Inject
  public UpdateMinefield() {}

  @Override
  public Void execute(@NotNull Minefield params) {
    // TODO: implements
    return null;
  }
}
