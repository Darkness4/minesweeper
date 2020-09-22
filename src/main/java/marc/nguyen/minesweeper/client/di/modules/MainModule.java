package marc.nguyen.minesweeper.client.di.modules;

import dagger.Module;
import dagger.Provides;
import io.reactivex.rxjava3.core.Observable;
import marc.nguyen.minesweeper.client.presentation.models.MainModel;
import marc.nguyen.minesweeper.client.presentation.utils.ResourcesLoader;
import marc.nguyen.minesweeper.client.presentation.views.MainView;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Player;
import marc.nguyen.minesweeper.common.data.models.Tile;

@Module()
public class MainModule {
  @Provides
  static MainModel provideMainModel(Minefield minefield, Player player, Observable<Tile> tiles) {
    return new MainModel(minefield, player, tiles);
  }

  @Provides
  static MainView provideMainView(Minefield minefield, ResourcesLoader resourcesLoader) {
    return new MainView(minefield, resourcesLoader);
  }
}
