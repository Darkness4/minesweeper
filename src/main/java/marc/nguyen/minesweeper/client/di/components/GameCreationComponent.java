package marc.nguyen.minesweeper.client.di.components;

import dagger.Component;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.di.modules.DataModule;
import marc.nguyen.minesweeper.client.di.modules.DomainModule;
import marc.nguyen.minesweeper.client.di.modules.PresentationModule;
import marc.nguyen.minesweeper.client.presentation.widgets.GameCreationDialog;

@Singleton
@Component(modules = {DataModule.class, DomainModule.class, PresentationModule.class})
public interface GameCreationComponent {
  GameCreationDialog gameCreationDialog();

  @Component.Builder
  interface Builder {
    GameCreationComponent build();
  }
}
