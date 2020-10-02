package marc.nguyen.minesweeper.client.di.modules;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.data.devices.ServerSocketDevice;
import marc.nguyen.minesweeper.client.domain.repositories.MinefieldRepository;
import marc.nguyen.minesweeper.client.domain.repositories.PlayerRepository;
import marc.nguyen.minesweeper.client.domain.repositories.SettingsRepository;
import marc.nguyen.minesweeper.client.domain.usecases.Connect;
import marc.nguyen.minesweeper.client.domain.usecases.DeleteSettings;
import marc.nguyen.minesweeper.client.domain.usecases.FetchAllScores;
import marc.nguyen.minesweeper.client.domain.usecases.FetchMinefield;
import marc.nguyen.minesweeper.client.domain.usecases.LoadSettings;
import marc.nguyen.minesweeper.client.domain.usecases.Quit;
import marc.nguyen.minesweeper.client.domain.usecases.SaveScore;
import marc.nguyen.minesweeper.client.domain.usecases.SaveSettings;
import marc.nguyen.minesweeper.client.domain.usecases.UpdateServerPlayer;
import marc.nguyen.minesweeper.client.domain.usecases.UpdateServerTile;
import marc.nguyen.minesweeper.client.domain.usecases.WatchEndGameMessages;
import marc.nguyen.minesweeper.client.domain.usecases.WatchServerTiles;

/**
 * Dependencies from the domain layer.
 *
 * <p>Should be all singleton, stateless and immutable.
 */
@Module
public abstract class DomainModule {

  @Provides
  @Singleton
  static Connect provideConnect(
      Lazy<ServerSocketDevice> deviceLazy,
      Lazy<WatchServerTiles> watchServerTilesLazy,
      Lazy<FetchMinefield> fetchMinefieldLazy) {
    return new Connect(deviceLazy, watchServerTilesLazy, fetchMinefieldLazy);
  }

  @Provides
  @Singleton
  static Quit provideQuit(Lazy<ServerSocketDevice> deviceLazy) {
    return new Quit(deviceLazy);
  }

  @Provides
  @Singleton
  static UpdateServerTile provideUpdateMinefield(Lazy<MinefieldRepository> repositoryLazy) {
    return new UpdateServerTile(repositoryLazy);
  }

  @Provides
  @Singleton
  static WatchServerTiles provideWatchServerTiles(Lazy<MinefieldRepository> repositoryLazy) {
    return new WatchServerTiles(repositoryLazy);
  }

  @Provides
  @Singleton
  static FetchMinefield provideFetchMinefield(Lazy<MinefieldRepository> repositoryLazy) {
    return new FetchMinefield(repositoryLazy);
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

  @Provides
  @Singleton
  static UpdateServerPlayer provideUpdateServerPlayer(Lazy<PlayerRepository> repository) {
    return new UpdateServerPlayer(repository);
  }

  @Provides
  @Singleton
  static SaveScore provideSaveScore(Lazy<PlayerRepository> repository) {
    return new SaveScore(repository);
  }

  @Provides
  @Singleton
  static FetchAllScores provideFetchAllScores(Lazy<PlayerRepository> repository) {
    return new FetchAllScores(repository);
  }

  @Provides
  @Singleton
  static WatchEndGameMessages provideWatchEndGameMessages(Lazy<ServerSocketDevice> device) {
    return new WatchEndGameMessages(device);
  }
}
