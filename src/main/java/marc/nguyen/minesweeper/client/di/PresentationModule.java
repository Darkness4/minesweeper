package marc.nguyen.minesweeper.client.di;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import marc.nguyen.minesweeper.client.presentation.controllers.MainController;

public class PresentationModule extends AbstractModule {
  @Provides
  @Singleton
  static MainController.Factory provideMainControllerFactory() {
    return new MainController.Factory();
  }
}
