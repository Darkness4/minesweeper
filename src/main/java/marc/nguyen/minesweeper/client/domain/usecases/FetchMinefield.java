package marc.nguyen.minesweeper.client.domain.usecases;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.core.IO;
import marc.nguyen.minesweeper.client.core.usecases.UseCase;
import marc.nguyen.minesweeper.client.domain.repositories.MinefieldRepository;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import org.jetbrains.annotations.NotNull;

/** A user should be able to download the minefield of the server. */
@Singleton
public class FetchMinefield implements UseCase<Void, Maybe<Minefield>> {

  private final Lazy<MinefieldRepository> repository;

  @Inject
  public FetchMinefield(Lazy<MinefieldRepository> repository) {
    this.repository = repository;
  }

  @Override
  @NotNull
  public Maybe<Minefield> execute(Void params) {
    return repository.get().fetch().observeOn(Schedulers.from(IO.executor));
  }
}
