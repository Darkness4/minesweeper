package marc.nguyen.minesweeper.common.data.models;

import java.io.Serializable;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/** A simple message. */
public abstract class BaseMessage implements Serializable {
  @NotNull public final String message;

  public BaseMessage(@NotNull String message) {
    this.message = message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BaseMessage message1 = (BaseMessage) o;
    return message.equals(message1.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message);
  }

  @Override
  public String toString() {
    return message;
  }
}
