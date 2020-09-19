package marc.nguyen.minesweeper.client.domain.usecases;

import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import org.jetbrains.annotations.NotNull;

/** A user should be able to download the minefield of the server. */
@Singleton
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
