package marc.nguyen.minesweeper.client.presentation.models;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Vector;
import javax.inject.Inject;
import marc.nguyen.minesweeper.client.core.mvc.Model;
import marc.nguyen.minesweeper.client.domain.entities.Settings;
import marc.nguyen.minesweeper.common.data.models.Level;
import org.jetbrains.annotations.NotNull;

public class GameCreationModel implements Model {

  public static Vector<Level> levelChoices =
      new Vector<>(Arrays.asList(Level.EASY, Level.MEDIUM, Level.HARD, Level.CUSTOM));

  @NotNull private Level level = Level.EASY;
  private int length = 10;
  private int height = 10;
  private int mines = 10;
  private String address = InetAddress.getLoopbackAddress().getHostName();
  private int port = 12345;
  private String settingsName = "My Settings";

  @Inject
  public GameCreationModel() {}

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getSettingsName() {
    return settingsName;
  }

  public void setSettingsName(String settingsName) {
    this.settingsName = settingsName;
  }

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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public InetAddress getInetAddress() throws UnknownHostException {
    return InetAddress.getByName(address);
  }

  public boolean isSettingsNameValid() {
    return settingsName != null && !settingsName.isEmpty();
  }

  public void fromEntity(Settings settings) {
    settingsName = settings.name;
    height = settings.height;
    length = settings.length;
    port = settings.port;
    mines = settings.mines;
    level = settings.level;
    address = settings.address.getHostAddress();
  }

  public Settings toEntity() throws UnknownHostException {
    return new Settings(
        getSettingsName(),
        getInetAddress(),
        getPort(),
        getLength(),
        getHeight(),
        getMines(),
        getLevel());
  }
}
