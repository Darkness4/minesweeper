package marc.nguyen.minesweeper.client.di.modules;

import dagger.Module;
import dagger.Provides;
import io.reactivex.rxjava3.core.Observable;
import marc.nguyen.minesweeper.client.presentation.models.GameModel;
import marc.nguyen.minesweeper.client.presentation.utils.ResourcesLoader;
import marc.nguyen.minesweeper.client.presentation.views.GameView;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Player;
import marc.nguyen.minesweeper.common.data.models.Tile;

/**
 * Provides the dependencies for the Game MVC.
 *
 * <p>The dependencies should NOT be singletons to be able to control their lifecycle.
 */
@Module()
public class GameModule {

  @Provides
  static GameModel provideGameModel(Minefield minefield, Player player, Observable<Tile> tiles) {
    return new GameModel(minefield, player, tiles);
  }

  @Provides
  static GameView provideGameView(Minefield minefield, ResourcesLoader resourcesLoader) {
    return new GameView(minefield, resourcesLoader);
  }
}
