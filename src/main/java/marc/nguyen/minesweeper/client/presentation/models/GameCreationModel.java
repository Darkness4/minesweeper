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

  @Inject
  public GameCreationModel() {}

  public @NotNull Level getLevel() {
    return level;
  }

  public void setLevel(@NotNull Level level) {
    this.level = level;
  }
}
