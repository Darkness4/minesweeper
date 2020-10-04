package marc.nguyen.minesweeper.common.data.models;

import java.io.Serializable;
import java.util.Objects;

public class Pair<T1, T2> implements Serializable {

  public final T1 first;
  public final T2 second;

  public Pair(T1 first, T2 second) {
    this.first = first;
    this.second = second;
  }

  @Override
  public String toString() {
    return "Pair{" + first + ", " + second + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Pair<?, ?> pair = (Pair<?, ?>) o;
    return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
  }

  @Override
  public int hashCode() {
    return Objects.hash(first, second);
  }
}
