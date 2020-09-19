package marc.nguyen.minesweeper.client.domain.usecases;

import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;

/** A user should be to exit the current server. */
@Singleton
public class Quit implements UseCase<Void, Void> {

  @Inject
  public Quit() {}

  @Override
  public Void execute(Void params) {
    // TODO: implements
    return null;
  }
}
