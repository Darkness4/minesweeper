package marc.nguyen.minesweeper.client.di.modules;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import marc.nguyen.minesweeper.client.domain.usecases.Connect;
import marc.nguyen.minesweeper.client.domain.usecases.DeleteSettings;
import marc.nguyen.minesweeper.client.domain.usecases.FetchAllSettingsName;
import marc.nguyen.minesweeper.client.domain.usecases.LoadSettings;
import marc.nguyen.minesweeper.client.domain.usecases.SaveSettings;
import marc.nguyen.minesweeper.client.domain.usecases.UpdateMinefield;
import marc.nguyen.minesweeper.client.presentation.controllers.GameCreationController;
import marc.nguyen.minesweeper.client.presentation.controllers.MainController;
import marc.nguyen.minesweeper.client.presentation.models.GameCreationModel;
import marc.nguyen.minesweeper.client.presentation.models.MainModel;
import marc.nguyen.minesweeper.client.presentation.utils.ResourcesLoader;
import marc.nguyen.minesweeper.client.presentation.views.GameCreationView;
import marc.nguyen.minesweeper.client.presentation.views.MainView;
import marc.nguyen.minesweeper.client.presentation.widgets.MainMenuBar;
import marc.nguyen.minesweeper.common.data.models.Minefield;

@Module()
public abstract class PresentationModule {

  @Provides
  static GameCreationController.Factory provideGameCreationControllerFactory(
      Lazy<Connect> connect,
      Lazy<LoadSettings> loadSettings,
      Lazy<SaveSettings> saveSettings,
      Lazy<DeleteSettings> deleteSettings,
      Lazy<FetchAllSettingsName> fetchAllSettingsName) {
    return new GameCreationController.Factory(
        connect, loadSettings, saveSettings, deleteSettings, fetchAllSettingsName);
  }

  @Provides
  static MainController.Factory provideMainControllerFactory(
      Lazy<UpdateMinefield> updateMinefield) {
    return new MainController.Factory(updateMinefield);
  }

  @Provides
  static ResourcesLoader provideResourcesLoader() {
    return new ResourcesLoader();
  }

  @Provides
  static MainModel provideMainModel(Minefield minefield) {
    return new MainModel(minefield);
  }

  @Provides
  static MainView provideMainView(Minefield minefield, ResourcesLoader resourcesLoader) {
    return new MainView(minefield, resourcesLoader);
  }

  @Provides
  static MainMenuBar provideMainMenuBar() {
    return new MainMenuBar();
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
