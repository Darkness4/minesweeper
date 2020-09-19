package marc.nguyen.minesweeper.client.domain.usecases;

import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import org.jetbrains.annotations.NotNull;

/** A user should be to update the minefield of the server. */
@Singleton
public class UpdateMinefield implements UseCase<Minefield, Void> {

  @Inject
  public UpdateMinefield() {}

  @Override
  public Void execute(@NotNull Minefield params) {
    // TODO: implements
    return null;
  }
}
