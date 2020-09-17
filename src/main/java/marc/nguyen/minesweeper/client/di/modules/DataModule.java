package marc.nguyen.minesweeper.client.di.modules;

import dagger.Binds;
import dagger.Module;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.data.database.SettingsDao;
import marc.nguyen.minesweeper.client.data.database.SettingsDaoSqlite;
import marc.nguyen.minesweeper.client.data.datasources.LocalDataSource;
import marc.nguyen.minesweeper.client.data.datasources.LocalDataSourceImpl;
import marc.nguyen.minesweeper.client.data.repositories.SettingsRepositoryImpl;
import marc.nguyen.minesweeper.client.domain.repositories.SettingsRepository;

@Module
public abstract class DataModule {

  @Binds
  @Singleton
  abstract LocalDataSource bindsSettingsDataSource(LocalDataSourceImpl impl);

  @Binds
  @Singleton
  abstract SettingsDao bindSettingsDao(SettingsDaoSqlite impl);

  @Binds
  @Singleton
  abstract SettingsRepository bindSettingsRepository(SettingsRepositoryImpl impl);
}
