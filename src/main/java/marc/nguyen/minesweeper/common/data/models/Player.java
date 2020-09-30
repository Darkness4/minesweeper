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

  @NotNull private final Counter score;
  @NotNull public final String name;

  public Player(@NotNull String name) {
    this.name = name;
    this.score = new Counter();
  }

  public Player(@NotNull String name, int score) {
    this.name = name;
    this.score = new Counter(score);
  }

  public int incrementScore() {
    return score.increment();
  }

  public int decrementScore() {
    return score.decrement();
  }

  public int addScore(int delta) {
    return score.add(delta);
  }

  public int getScore() {
    return score.getValue();
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
    return score.equals(player.score) && name.equals(player.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(score, name);
  }
}
