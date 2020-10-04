package marc.nguyen.minesweeper.client.presentation.models;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import marc.nguyen.minesweeper.client.domain.entities.HighScore;

public class HighScoreTableModel extends AbstractTableModel {

  private static final String[] columnNames = {
    "Player", "Score", "Length", "Height", "Number of mines"
  };
  private final List<HighScore> highScores = new ArrayList<>();
  private final Disposable highScoresListener;

  public HighScoreTableModel(Observable<List<HighScore>> highScoreList$) {
    highScoresListener =
        highScoreList$.subscribe(
            l -> {
              highScores.clear();
              l.stream().sorted(Comparator.comparingInt(a -> -a.score)).forEach(highScores::add);
              fireTableDataChanged();
            },
            (e) -> {
              System.err.println("[ERROR] playerListListener error.");
              e.printStackTrace();
            });
  }

  public void dispose() {
    highScoresListener.dispose();
  }

  @Override
  public int getRowCount() {
    return highScores.size();
  }

  @Override
  public int getColumnCount() {
    return columnNames.length;
  }

  @Override
  public String getColumnName(int col) {
    return columnNames[col];
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    switch (columnIndex) {
      case 0:
        return highScores.get(rowIndex).playerName;
      case 1:
        return highScores.get(rowIndex).score;
      case 2:
        return highScores.get(rowIndex).minefieldLength;
      case 3:
        return highScores.get(rowIndex).minefieldHeight;
      case 4:
        return highScores.get(rowIndex).mines;
      default:
        return null;
    }
  }
}
