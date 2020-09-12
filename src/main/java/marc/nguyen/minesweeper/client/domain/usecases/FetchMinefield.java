package marc.nguyen.minesweeper.client.domain.usecases;

import javax.inject.Inject;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import org.jetbrains.annotations.NotNull;

public class FetchMinefield implements UseCase<Void, Minefield> {

  @Inject
  public FetchMinefield() {}

  @Override
  @NotNull
  public Minefield execute(Void params) {
    // TODO: implements
    return null;
  }
}
