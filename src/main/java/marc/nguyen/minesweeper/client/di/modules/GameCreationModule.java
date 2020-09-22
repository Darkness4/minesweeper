package marc.nguyen.minesweeper.client.di.modules;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import javax.inject.Provider;
import marc.nguyen.minesweeper.client.di.components.MainComponent;
import marc.nguyen.minesweeper.client.di.components.MainComponent.Builder;
import marc.nguyen.minesweeper.client.domain.usecases.Connect;
import marc.nguyen.minesweeper.client.domain.usecases.DeleteSettings;
import marc.nguyen.minesweeper.client.domain.usecases.FetchAllSettingsName;
import marc.nguyen.minesweeper.client.domain.usecases.LoadSettings;
import marc.nguyen.minesweeper.client.domain.usecases.Quit;
import marc.nguyen.minesweeper.client.domain.usecases.SaveSettings;
import marc.nguyen.minesweeper.client.domain.usecases.UpdateServerTile;
import marc.nguyen.minesweeper.client.presentation.controllers.GameCreationController;
import marc.nguyen.minesweeper.client.presentation.controllers.MainController;
import marc.nguyen.minesweeper.client.presentation.models.GameCreationModel;
import marc.nguyen.minesweeper.client.presentation.utils.ResourcesLoader;
import marc.nguyen.minesweeper.client.presentation.views.GameCreationView;
import marc.nguyen.minesweeper.client.presentation.widgets.MainMenuBar;

@Module(subcomponents = MainComponent.class)
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
  static MainController.Factory provideMainControllerFactory(
      Lazy<UpdateServerTile> updateMinefield) {
    return new MainController.Factory(updateMinefield);
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
