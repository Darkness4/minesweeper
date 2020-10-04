package marc.nguyen.minesweeper.client.domain.entities;

import java.util.Objects;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Player;
import org.jetbrains.annotations.NotNull;

public class HighScore {

  public final @NotNull String playerName;
  public final int score;
  public final int minefieldLength;
  public final int minefieldHeight;
  public final int mines;

  public HighScore(
      @NotNull String playerName, int score, int minefieldLength, int minefieldHeight, int mines) {
    this.playerName = playerName;
    this.score = score;
    this.minefieldLength = minefieldLength;
    this.minefieldHeight = minefieldHeight;
    this.mines = mines;
  }

  public HighScore(Player player, Settings settings) {
    this.playerName = player.name;
    this.score = player.getScore();
    this.minefieldLength = settings.length;
    this.minefieldHeight = settings.height;
    this.mines = settings.mines;
  }

  public HighScore(Player player, Minefield minefield) {
    this.playerName = player.name;
    this.score = player.getScore();
    this.minefieldLength = minefield.getLength();
    this.minefieldHeight = minefield.getHeight();
    this.mines = (int) minefield.getMinesOnField();
  }

  @Override
  public String toString() {
    return "HighScore{"
        + "playerName='"
        + playerName
        + '\''
        + ", score="
        + score
        + ", minefieldLength="
        + minefieldLength
        + ", minefieldHeight="
        + minefieldHeight
        + ", mines="
        + mines
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HighScore highScore = (HighScore) o;
    return score == highScore.score
        && minefieldLength == highScore.minefieldLength
        && minefieldHeight == highScore.minefieldHeight
        && mines == highScore.mines
        && playerName.equals(highScore.playerName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerName, score, minefieldLength, minefieldHeight, mines);
  }
}
