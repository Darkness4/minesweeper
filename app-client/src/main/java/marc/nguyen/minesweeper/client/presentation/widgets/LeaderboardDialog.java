package marc.nguyen.minesweeper.client.presentation.widgets;

import dagger.Lazy;
import javax.inject.Inject;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.domain.usecases.FetchAllScores;
import marc.nguyen.minesweeper.client.presentation.models.HighScoreTableModel;

public class LeaderboardDialog extends JDialog {

  public final JTable playerTable;

  @Inject
  public LeaderboardDialog(Lazy<FetchAllScores> fetchAllScores) {
    super((JFrame) null, "Leaderboard");
    assert SwingUtilities.isEventDispatchThread() : "View is running on unsafe thread!";

    final var model = new HighScoreTableModel(fetchAllScores.get().execute(null).toObservable());

    playerTable = new JTable(model);
    final var scrollPane = new JScrollPane(playerTable);
    add(scrollPane);
    setModalityType(ModalityType.APPLICATION_MODAL);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    pack();
    setVisible(true);
  }
}
