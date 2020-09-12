package marc.nguyen.minesweeper.client.di.modules;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.domain.usecases.Connect;
import marc.nguyen.minesweeper.client.domain.usecases.FetchMinefield;
import marc.nguyen.minesweeper.client.domain.usecases.Quit;
import marc.nguyen.minesweeper.client.domain.usecases.UpdateMinefield;

@Module
public class DomainModule {
  @Provides
  @Singleton
  static Connect provideConnect() {
    return new Connect();
  }

  @Provides
  @Singleton
  static Quit provideQuit() {
    return new Quit();
  }

  @Provides
  @Singleton
  static UpdateMinefield provideUpdateMinefield() {
    return new UpdateMinefield();
  }

  @Provides
  @Singleton
  static FetchMinefield provideFetchMinefield() {
    return new FetchMinefield();
  }
}
