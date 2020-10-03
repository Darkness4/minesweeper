package marc.nguyen.minesweeper.common.data.models;

public class Position extends Pair<Integer, Integer> {
  public Position(Integer first, Integer second) {
    super(first, second);
  }

  public Integer getX() {
    return first;
  }

  public Integer getY() {
    return second;
  }

  @Override
  public String toString() {
    return "{" + first + ", " + second + "}";
  }
}
