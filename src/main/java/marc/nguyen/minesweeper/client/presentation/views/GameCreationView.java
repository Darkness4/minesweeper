package marc.nguyen.minesweeper.client.presentation.views;

import java.awt.BorderLayout;
import javax.inject.Inject;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.core.mvc.View;
import marc.nguyen.minesweeper.client.presentation.views.gamecreation.SavedSettingsPanel;
import marc.nguyen.minesweeper.client.presentation.views.gamecreation.SettingsPanel;

public final class GameCreationView extends JPanel implements View {

  public final SettingsPanel settingsPanel;
  public final SavedSettingsPanel savedSettingsPanel;

  @Inject
  public GameCreationView() {
    if (!SwingUtilities.isEventDispatchThread()) {
      throw new RuntimeException("View is running on unsafe thread!");
    }

    setLayout(new BorderLayout());

    settingsPanel = new SettingsPanel();

    add(settingsPanel, BorderLayout.CENTER);

    savedSettingsPanel = new SavedSettingsPanel();

    add(savedSettingsPanel, BorderLayout.EAST);
  }
}
