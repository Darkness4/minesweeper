package marc.nguyen.minesweeper.client.domain.usecases;

import dagger.Lazy;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.domain.repositories.MinefieldRepository;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import org.jetbrains.annotations.NotNull;

/** A user should be able to download the minefield of the server. */
@Singleton
public class FetchMinefield implements UseCase<Void, Optional<Minefield>> {
  final Lazy<MinefieldRepository> repository;

  @Inject
  public FetchMinefield(Lazy<MinefieldRepository> repository) {
    this.repository = repository;
  }

  @Override
  @NotNull
  public Optional<Minefield> execute(Void params) {
    return repository.get().fetch();
  }
}
