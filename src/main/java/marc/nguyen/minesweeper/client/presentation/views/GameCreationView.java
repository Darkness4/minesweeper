package marc.nguyen.minesweeper.client.presentation.views;

import javax.inject.Inject;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.core.mvc.View;
import marc.nguyen.minesweeper.client.presentation.views.gamecreation.SettingsPanel;

public final class GameCreationView extends JPanel implements View {

  @Inject
  public GameCreationView() {
    if (!SwingUtilities.isEventDispatchThread()) {
      throw new RuntimeException("View is running on unsafe thread!");
    }

    add(new SettingsPanel());
  }
}
