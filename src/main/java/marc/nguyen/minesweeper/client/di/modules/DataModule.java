package marc.nguyen.minesweeper.client.di.modules;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.data.database.SettingsDB;
import marc.nguyen.minesweeper.client.data.repositories.SettingsRepositoryImpl;
import marc.nguyen.minesweeper.client.domain.repositories.SettingsRepository;

@Module
public abstract class DataModule {
  @Provides
  @Singleton
  static SettingsDB provideSettingsDB() {
    return new SettingsDB();
  }

  @Binds
  abstract SettingsRepository bindSettingsRepository(SettingsRepositoryImpl impl);
}
