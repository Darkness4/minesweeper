package marc.nguyen.minesweeper.common.data.models;

import org.jetbrains.annotations.NotNull;

/** An end game message */
public class EndGameMessage extends Message {
  public EndGameMessage(@NotNull String message) {
    super(message);
  }
}
