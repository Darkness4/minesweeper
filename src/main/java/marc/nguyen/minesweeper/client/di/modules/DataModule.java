package marc.nguyen.minesweeper.client.di.modules;

import dagger.Binds;
import dagger.Module;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.data.database.SettingsDb;
import marc.nguyen.minesweeper.client.data.database.SettingsDbInMemory;
import marc.nguyen.minesweeper.client.data.repositories.SettingsRepositoryImpl;
import marc.nguyen.minesweeper.client.domain.repositories.SettingsRepository;

@Module
public abstract class DataModule {

  @Binds
  @Singleton
  abstract SettingsDb bindSettingsDB(SettingsDbInMemory impl);

  @Binds
  @Singleton
  abstract SettingsRepository bindSettingsRepository(SettingsRepositoryImpl impl);
}
