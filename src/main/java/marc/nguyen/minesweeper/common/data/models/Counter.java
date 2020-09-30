package marc.nguyen.minesweeper.common.data.models;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/** A simple counter. */
class Counter implements Serializable {

  public Counter() {
    this.value = new AtomicInteger(0);
  }

  public Counter(int initialValue) {
    this.value = new AtomicInteger(initialValue);
  }

  private final AtomicInteger value;

  public int increment() {
    return value.incrementAndGet();
  }

  public int add(int delta) {
    return value.addAndGet(delta);
  }

  public int decrement() {
    return value.decrementAndGet();
  }

  public int getValue() {
    return value.get();
  }

  @Override
  public String toString() {
    return value.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Counter counter = (Counter) o;
    return value.equals(counter.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
