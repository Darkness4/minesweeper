package marc.nguyen.minesweeper.client.di.components;

import dagger.BindsInstance;
import dagger.Subcomponent;
import io.reactivex.rxjava3.core.Observable;
import java.util.List;
import marc.nguyen.minesweeper.client.di.modules.GameModule;
import marc.nguyen.minesweeper.client.presentation.views.GameFrame;
import marc.nguyen.minesweeper.common.data.models.EndGameMessage;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Player;
import marc.nguyen.minesweeper.common.data.models.Position;

/**
 * Dagger Component used for the Game Frame.
 *
 * <p>Should be a subcomponent of a component which already have the DataModule and DomainModule.
 */
@Subcomponent(modules = {GameModule.class})
public interface GameComponent {

  GameFrame gameFrame();

  @Subcomponent.Builder
  interface Builder {

    @BindsInstance
    Builder minefield(Minefield minefield);

    @BindsInstance
    Builder updateTiles(Observable<Position> positions);

    @BindsInstance
    Builder endGameMessages(Observable<EndGameMessage> endGameMessageObservable);

    @BindsInstance
    Builder playerList(Observable<List<Player>> playerListObservable);

    @BindsInstance
    Builder player(Player player);

    GameComponent build();
  }
}
