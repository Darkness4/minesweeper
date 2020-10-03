package marc.nguyen.minesweeper.client.presentation.views.game;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

/** A panel showing the list of player their score. */
public class PlayerListPanel extends JPanel {

  public final JTable playerTable;

  public PlayerListPanel() {
    assert SwingUtilities.isEventDispatchThread() : "View is running on unsafe thread!";

    playerTable = new JTable();

    final var scrollPane = new JScrollPane(playerTable);
    add(scrollPane);
  }
}
