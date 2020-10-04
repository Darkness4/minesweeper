package marc.nguyen.minesweeper.client.presentation.models.game;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import marc.nguyen.minesweeper.common.data.models.Player;

public class PlayerListTableModel extends AbstractTableModel {

  private static final String[] columnNames = {"Player", "Score"};
  private final List<Player> playerList = new ArrayList<>();
  private final Disposable playerListListener;

  public PlayerListTableModel(Observable<List<Player>> playerList$) {
    playerListListener =
        playerList$.subscribe(
            l -> {
              playerList.clear();
              l.stream()
                  .sorted(Comparator.comparingInt(Player::getScore).reversed())
                  .forEach(playerList::add);
              fireTableDataChanged();
            },
            (e) -> {
              System.err.println("[ERROR] playerListListener error.");
              e.printStackTrace();
            });
  }

  public void dispose() {
    playerListListener.dispose();
  }

  @Override
  public int getRowCount() {
    return playerList.size();
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
        return playerList.get(rowIndex).name;
      case 1:
        return playerList.get(rowIndex).getScore();
      default:
        return null;
    }
  }
}
