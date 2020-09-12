package marc.nguyen.minesweeper.client.di.modules;

import com.squareup.inject.assisted.dagger2.AssistedModule;
import dagger.Module;
import dagger.Provides;
import marc.nguyen.minesweeper.client.presentation.models.MainModel;
import marc.nguyen.minesweeper.common.data.models.Minefield;

@AssistedModule
@Module(includes = AssistedInject_PresentationModule.class)
public abstract class PresentationModule {
  @Provides
  static MainModel provideMainModel(Minefield minefield) {
    return new MainModel(minefield);
  }
}
