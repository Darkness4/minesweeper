package marc.nguyen.minesweeper.client.presentation.views.game;

import java.time.LocalTime;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.common.data.models.Minefield;

/** The panel with the score and mines. */
public class DisplayPanel extends JPanel {

  public final JLabel bombLeftText;
  public final JLabel playerScoreText;
  public final JLabel timeText;

  public DisplayPanel(Minefield minefield) {
    assert SwingUtilities.isEventDispatchThread() : "View is running on unsafe thread!";

    final var layout = new SpringLayout();
    setLayout(layout);

    final var bombLeftLabel = new JLabel("Mines left: ");
    bombLeftText = new JLabel(String.valueOf(minefield.getMinesOnField()));
    layout.putConstraint(SpringLayout.WEST, bombLeftLabel, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, bombLeftLabel, 5, SpringLayout.NORTH, this);
    layout.putConstraint(SpringLayout.WEST, bombLeftText, 5, SpringLayout.EAST, bombLeftLabel);
    layout.putConstraint(SpringLayout.NORTH, bombLeftText, 5, SpringLayout.NORTH, this);
    add(bombLeftLabel);
    add(bombLeftText);

    final var scoreLabel = new JLabel("Score: ");
    playerScoreText = new JLabel("0");
    add(scoreLabel);
    add(playerScoreText);
    layout.putConstraint(SpringLayout.WEST, scoreLabel, 20, SpringLayout.EAST, bombLeftText);
    layout.putConstraint(SpringLayout.NORTH, scoreLabel, 5, SpringLayout.NORTH, this);
    layout.putConstraint(SpringLayout.WEST, playerScoreText, 5, SpringLayout.EAST, scoreLabel);
    layout.putConstraint(SpringLayout.NORTH, playerScoreText, 5, SpringLayout.NORTH, this);

    final var timeLabel = new JLabel("Time: ");
    timeText = new JLabel(LocalTime.MIN.toString());
    add(timeLabel);
    add(timeText);
    layout.putConstraint(SpringLayout.WEST, timeLabel, 20, SpringLayout.EAST, playerScoreText);
    layout.putConstraint(SpringLayout.NORTH, timeLabel, 5, SpringLayout.NORTH, this);
    layout.putConstraint(SpringLayout.WEST, timeText, 5, SpringLayout.EAST, timeLabel);
    layout.putConstraint(SpringLayout.NORTH, timeText, 5, SpringLayout.NORTH, this);

    layout.putConstraint(SpringLayout.EAST, this, 5, SpringLayout.EAST, timeText);
    layout.putConstraint(SpringLayout.SOUTH, this, 5, SpringLayout.SOUTH, bombLeftText);
  }

  public void updatePlayerScore(int score) {
    SwingUtilities.invokeLater(() -> playerScoreText.setText(String.valueOf(score)));
  }

  public void updateMinesLeft(long mines) {
    SwingUtilities.invokeLater(() -> bombLeftText.setText(String.valueOf(mines)));
  }

  public void updateTimeLeft(LocalTime time) {
    SwingUtilities.invokeLater(() -> timeText.setText(time.toString()));
  }
}
