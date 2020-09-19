package marc.nguyen.minesweeper.client.di.components;

import dagger.BindsInstance;
import dagger.Component;
import javax.inject.Singleton;
import marc.nguyen.minesweeper.client.di.modules.DataModule;
import marc.nguyen.minesweeper.client.di.modules.DomainModule;
import marc.nguyen.minesweeper.client.di.modules.PresentationModule;
import marc.nguyen.minesweeper.client.presentation.widgets.MainFrame;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Player;

/** Dagger Component used for the Main Frame. */
@Singleton
@Component(modules = {DataModule.class, DomainModule.class, PresentationModule.class})
public interface MainComponent {
  MainFrame mainFrame();

  @Component.Builder
  interface Builder {
    @BindsInstance
    Builder minefield(Minefield minefield);

    @BindsInstance
    Builder player(Player player);

    MainComponent build();
  }
}
