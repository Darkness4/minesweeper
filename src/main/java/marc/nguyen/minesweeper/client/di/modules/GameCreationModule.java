package marc.nguyen.minesweeper.client.di.modules;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import javax.inject.Provider;
import marc.nguyen.minesweeper.client.di.components.GameComponent;
import marc.nguyen.minesweeper.client.di.components.GameComponent.Builder;
import marc.nguyen.minesweeper.client.domain.usecases.Connect;
import marc.nguyen.minesweeper.client.domain.usecases.DeleteSettings;
import marc.nguyen.minesweeper.client.domain.usecases.FetchAllSettingsName;
import marc.nguyen.minesweeper.client.domain.usecases.LoadSettings;
import marc.nguyen.minesweeper.client.domain.usecases.Quit;
import marc.nguyen.minesweeper.client.domain.usecases.SaveSettings;
import marc.nguyen.minesweeper.client.domain.usecases.UpdateServerTile;
import marc.nguyen.minesweeper.client.presentation.controllers.GameController;
import marc.nguyen.minesweeper.client.presentation.controllers.GameCreationController;
import marc.nguyen.minesweeper.client.presentation.models.GameCreationModel;
import marc.nguyen.minesweeper.client.presentation.utils.ResourcesLoader;
import marc.nguyen.minesweeper.client.presentation.views.GameCreationView;
import marc.nguyen.minesweeper.client.presentation.widgets.MainMenuBar;

/**
 * Provides the dependencies for the GameCreation MVC.
 *
 * <p>The dependencies should NOT be singletons to be able to control their lifecycle.
 */
@Module(subcomponents = GameComponent.class)
public class GameCreationModule {

  @Provides
  static GameCreationController.Factory provideGameCreationControllerFactory(
      Lazy<Connect> connect,
      Lazy<LoadSettings> loadSettings,
      Lazy<SaveSettings> saveSettings,
      Lazy<DeleteSettings> deleteSettings,
      Lazy<FetchAllSettingsName> fetchAllSettingsName,
      Provider<Builder> mainComponentProvider) {
    return new GameCreationController.Factory(
        connect,
        loadSettings,
        saveSettings,
        deleteSettings,
        fetchAllSettingsName,
        mainComponentProvider);
  }

  @Provides
  static GameController.Factory provideGameControllerFactory(
      Lazy<UpdateServerTile> updateMinefield) {
    return new GameController.Factory(updateMinefield);
  }

  @Provides
  static ResourcesLoader provideResourcesLoader() {
    return new ResourcesLoader();
  }

  @Provides
  static MainMenuBar provideMainMenuBar(Lazy<Quit> quit) {
    return new MainMenuBar(quit);
  }

  @Provides
  static GameCreationView provideGameCreationView() {
    return new GameCreationView();
  }

  @Provides
  static GameCreationModel provideGameCreationModel() {
    return new GameCreationModel();
  }
}
