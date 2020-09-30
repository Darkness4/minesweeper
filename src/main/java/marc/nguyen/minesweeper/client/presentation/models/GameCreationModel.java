package marc.nguyen.minesweeper.client.presentation.models;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.inject.Inject;
import marc.nguyen.minesweeper.client.core.mvc.Model;
import marc.nguyen.minesweeper.client.domain.entities.GameMode;
import marc.nguyen.minesweeper.client.domain.entities.Settings;
import marc.nguyen.minesweeper.common.data.models.Level;
import org.jetbrains.annotations.NotNull;

/**
 * The Game Creation Model.
 *
 * <p>Mutable. Mostly used for the form.
 */
public class GameCreationModel implements Model {

  public static final Level[] levelChoices = Level.values();
  public static final GameMode[] gameModeChoices = GameMode.values();

  @NotNull private Level level = Level.EASY;
  private int length = 10;
  private int height = 10;
  private int mines = 10;
  @NotNull private String address = InetAddress.getLoopbackAddress().getHostName();
  private int port = 12345;
  @NotNull private String settingsName = "My Settings";
  @NotNull private GameMode mode = GameMode.SINGLEPLAYER;
  @NotNull private String playerName = "Player";

  @Inject
  public GameCreationModel() {}

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public @NotNull String getSettingsName() {
    return settingsName;
  }

  public void setSettingsName(@NotNull String settingsName) {
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

  public @NotNull String getAddress() {
    return address;
  }

  public void setAddress(@NotNull String address) {
    this.address = address;
  }

  public InetAddress getInetAddress() throws UnknownHostException {
    return InetAddress.getByName(address);
  }

  public boolean isSettingsNameValid() {
    return !settingsName.isEmpty();
  }

  public void fromEntity(Settings settings) {
    settingsName = settings.name;
    height = settings.height;
    length = settings.length;
    port = settings.port;
    mines = settings.mines;
    level = settings.level;
    address = settings.address.getHostAddress();
    mode = settings.mode;
  }

  public Boolean isSinglePlayer() {
    return mode == GameMode.SINGLEPLAYER;
  }

  public Settings toEntity() throws UnknownHostException {
    return new Settings(
        settingsName, getInetAddress(), port, length, height, mines, level, mode, playerName);
  }

  public @NotNull GameMode getMode() {
    return mode;
  }

  public void setMode(@NotNull GameMode mode) {
    this.mode = mode;
  }

  public @NotNull String getPlayerName() {
    return playerName;
  }

  public void setPlayerName(@NotNull String playerName) {
    this.playerName = playerName;
  }
}
