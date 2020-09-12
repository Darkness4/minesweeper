package marc.nguyen.minesweeper.client.di.modules;

import com.squareup.inject.assisted.dagger2.AssistedModule;
import dagger.Module;
import dagger.Provides;
import marc.nguyen.minesweeper.client.presentation.models.GameCreationModel;
import marc.nguyen.minesweeper.client.presentation.models.MainModel;
import marc.nguyen.minesweeper.client.presentation.views.GameCreationView;
import marc.nguyen.minesweeper.client.presentation.views.MainView;
import marc.nguyen.minesweeper.client.presentation.widgets.MainMenuBar;
import marc.nguyen.minesweeper.common.data.models.Minefield;

@AssistedModule
@Module(includes = AssistedInject_PresentationModule.class)
public abstract class PresentationModule {
  @Provides
  static MainModel provideMainModel(Minefield minefield) {
    return new MainModel(minefield);
  }

  @Provides
  static MainView provideMainView(Minefield minefield) {
    return new MainView(minefield);
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
