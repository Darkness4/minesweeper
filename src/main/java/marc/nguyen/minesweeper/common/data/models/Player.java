package marc.nguyen.minesweeper.common.data.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * A simple Player object.
 *
 * <p>This is holding score, name, ...
 */
public class Player implements Serializable {
  final Counter score = new Counter();

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
    return "Player{" + "score=" + score + '}';
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
    return score.equals(player.score);
  }

  @Override
  public int hashCode() {
    return Objects.hash(score);
  }
}
