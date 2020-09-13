package marc.nguyen.minesweeper.client.domain.entities;

import java.net.InetAddress;
import java.util.Objects;
import marc.nguyen.minesweeper.common.data.models.Level;

public class Settings {

  public final String name;
  public final InetAddress address;
  public final int length;
  public final int height;
  public final int mines;
  public final Level level;

  public Settings(
      String name, InetAddress address, int length, int height, int mines, Level level) {
    this.name = name;
    this.address = address;
    this.length = length;
    this.height = height;
    this.mines = mines;
    this.level = level;
  }

  @Override
  public String toString() {
    return "Settings{"
        + "name='"
        + name
        + '\''
        + ", address="
        + address
        + ", length="
        + length
        + ", height="
        + height
        + ", mines="
        + mines
        + ", level="
        + level
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Settings settings = (Settings) o;
    return length == settings.length
        && height == settings.height
        && mines == settings.mines
        && name.equals(settings.name)
        && address.equals(settings.address)
        && level == settings.level;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, address, length, height, mines, level);
  }
}
