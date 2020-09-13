package marc.nguyen.minesweeper.client.presentation.models;

import java.util.Arrays;
import java.util.Vector;
import javax.inject.Inject;
import marc.nguyen.minesweeper.client.core.mvc.Model;
import marc.nguyen.minesweeper.common.data.models.Level;
import org.jetbrains.annotations.NotNull;

public class GameCreationModel implements Model {

  public static Vector<Level> levelChoices =
      new Vector<>(Arrays.asList(Level.EASY, Level.MEDIUM, Level.HARD, Level.CUSTOM));

  @NotNull private Level level = Level.EASY;
  private int length = 10;
  private int height = 10;
  private int mines = 10;

  @Inject
  public GameCreationModel() {}

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getMines() {
    return mines;
  }

  public void setMines(int mines) {
    this.mines = mines;
  }

  public @NotNull Level getLevel() {
    return level;
  }

  public void setLevel(@NotNull Level level) {
    this.level = level;
  }
}
