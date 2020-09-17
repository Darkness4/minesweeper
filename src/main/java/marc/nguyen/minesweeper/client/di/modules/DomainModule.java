package marc.nguyen.minesweeper.client.di.modules;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.domain.repositories.SettingsRepository;
import marc.nguyen.minesweeper.client.domain.usecases.Connect;
import marc.nguyen.minesweeper.client.domain.usecases.DeleteSettings;
import marc.nguyen.minesweeper.client.domain.usecases.FetchMinefield;
import marc.nguyen.minesweeper.client.domain.usecases.LoadSettings;
import marc.nguyen.minesweeper.client.domain.usecases.Quit;
import marc.nguyen.minesweeper.client.domain.usecases.SaveSettings;
import marc.nguyen.minesweeper.client.domain.usecases.UpdateMinefield;

@Module
public abstract class DomainModule {

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

  @Provides
  @Singleton
  static DeleteSettings provideDeleteSettings(Lazy<SettingsRepository> repository) {
    return new DeleteSettings(repository);
  }

  @Provides
  @Singleton
  static LoadSettings provideLoadSettings(Lazy<SettingsRepository> repository) {
    return new LoadSettings(repository);
  }

  @Provides
  @Singleton
  static SaveSettings provideSaveSettings(Lazy<SettingsRepository> repository) {
    return new SaveSettings(repository);
  }
}
