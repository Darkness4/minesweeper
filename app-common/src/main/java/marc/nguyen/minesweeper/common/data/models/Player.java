package marc.nguyen.minesweeper.common.data.models;

import java.io.Serializable;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * A simple Player object.
 *
 * <p>This is holding score, name, ...
 */
public class Player implements Serializable {

  private int score;
  @NotNull public final String name;

  public Player(@NotNull String name) {
    this.name = name;
    this.score = 0;
  }

  public Player(@NotNull String name, int score) {
    this.name = name;
    this.score = score;
  }

  public synchronized void incrementScore() {
    score++;
  }

  public synchronized void addScore(int delta) {
    score += delta;
  }

  public synchronized void decrementScore() {
    score--;
  }

  public Player copyWith(int score) {
    return new Player(name, score);
  }

  public synchronized int getScore() {
    return score;
  }

  @Override
  public String toString() {
    return "Player{" + "score=" + score + ", name='" + name + '\'' + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Player player = (Player) o;
    return score == player.score && name.equals(player.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(score, name);
  }
}
