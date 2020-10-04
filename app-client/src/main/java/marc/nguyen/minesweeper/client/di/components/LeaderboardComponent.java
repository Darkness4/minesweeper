package marc.nguyen.minesweeper.client.di.components;

import dagger.Component;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.di.modules.DataModule;
import marc.nguyen.minesweeper.client.di.modules.DomainModule;
import marc.nguyen.minesweeper.client.presentation.widgets.LeaderboardDialog;

/** Dagger Component used for the Game Creation Dialog. */
@Singleton
@Component(
    modules = {
      DataModule.class,
      DomainModule.class,
    })
public interface LeaderboardComponent {

  LeaderboardDialog leaderboardDialog();

  @Component.Builder
  interface Builder {

    LeaderboardComponent build();
  }
}
