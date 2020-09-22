package marc.nguyen.minesweeper.client.di.components;

import dagger.Component;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.di.modules.DataModule;
import marc.nguyen.minesweeper.client.di.modules.DomainModule;
import marc.nguyen.minesweeper.client.di.modules.PresentationModule;
import marc.nguyen.minesweeper.client.presentation.widgets.GameCreationFrame;

/** Dagger Component used for the Game Creation Dialog. */
@Singleton
@Component(modules = {DataModule.class, DomainModule.class, PresentationModule.class})
public interface GameCreationComponent {

  GameCreationFrame gameCreationDialog();

  @Component.Builder
  interface Builder {

    GameCreationComponent build();
  }
}
