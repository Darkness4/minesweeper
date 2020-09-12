package marc.nguyen.minesweeper.client.domain.usecases;

import javax.inject.Inject;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;

public class Connect implements UseCase<Void, Void> {

  @Inject
  public Connect() {}

  @Override
  public Void execute(Void params) {
    // TODO: implements
    return null;
  }
}
