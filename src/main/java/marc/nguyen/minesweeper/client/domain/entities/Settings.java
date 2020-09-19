package marc.nguyen.minesweeper.client.domain.entities;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Objects;
import marc.nguyen.minesweeper.common.data.models.Level;
import org.jetbrains.annotations.NotNull;

/** Game Creation Settings. */
public class Settings implements Serializable {

  @NotNull public final String name;
  @NotNull public final InetAddress address;
  public final int port;
  public final int length;
  public final int height;
  public final int mines;
  @NotNull public final Level level;

  public Settings(
      @NotNull String name,
      @NotNull InetAddress address,
      int port,
      int length,
      int height,
      int mines,
      @NotNull Level level) {
    this.name = name;
    this.address = address;
    this.port = port;
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
        + ", port="
        + port
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
    return port == settings.port
        && length == settings.length
        && height == settings.height
        && mines == settings.mines
        && name.equals(settings.name)
        && address.equals(settings.address)
        && level == settings.level;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, address, port, length, height, mines, level);
  }
}
