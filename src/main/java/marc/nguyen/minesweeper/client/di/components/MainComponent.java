package marc.nguyen.minesweeper.client.di.components;

import dagger.BindsInstance;
import dagger.Subcomponent;
import io.reactivex.rxjava3.core.Observable;
import marc.nguyen.minesweeper.client.di.modules.MainModule;
import marc.nguyen.minesweeper.client.presentation.widgets.MainFrame;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Player;
import marc.nguyen.minesweeper.common.data.models.Tile;

/** Dagger Component used for the Main Frame. */
@Subcomponent(modules = {MainModule.class})
public interface MainComponent {

  MainFrame mainFrame();

  @Subcomponent.Builder
  interface Builder {

    @BindsInstance
    Builder minefield(Minefield minefield);

    @BindsInstance
    Builder updateTiles(Observable<Tile> tiles);

    @BindsInstance
    Builder player(Player player);

    MainComponent build();
  }
}
