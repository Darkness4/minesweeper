package marc.nguyen.minesweeper.client.presentation.views;

import java.awt.BorderLayout;
import javax.inject.Inject;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.core.mvc.View;
import marc.nguyen.minesweeper.client.presentation.views.gamecreation.EditSettingsPanel;
import marc.nguyen.minesweeper.client.presentation.views.gamecreation.SavedSettingsPanel;

public final class GameCreationView extends JPanel implements View {

  public final EditSettingsPanel editSettingsPanel;
  public final SavedSettingsPanel savedSettingsPanel;

  @Inject
  public GameCreationView() {
    if (!SwingUtilities.isEventDispatchThread()) {
      throw new RuntimeException("View is running on unsafe thread!");
    }

    setLayout(new BorderLayout());

    editSettingsPanel = new EditSettingsPanel();
    add(editSettingsPanel, BorderLayout.CENTER);

    savedSettingsPanel = new SavedSettingsPanel();
    add(savedSettingsPanel, BorderLayout.EAST);
  }
}
