package marc.nguyen.minesweeper.client.domain.usecases;

import javax.inject.Inject;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;

public class Quit implements UseCase<Void, Void> {

  @Inject
  public Quit() {}

  @Override
  public Void execute(Void params) {
    // TODO: implements
    return null;
  }
}
