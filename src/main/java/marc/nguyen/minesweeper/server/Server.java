package marc.nguyen.minesweeper.server;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import java.util.List;
import marc.nguyen.minesweeper.common.data.models.Level;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.server.api.GameServer;
import marc.nguyen.minesweeper.server.enums.LevelParams;

public final class Server {

  @Parameter(
      names = {"-p", "--port"},
      description = "TCP Port to be opened")
  private int port = 12345;

  @Parameter(
      names = {"-l", "--level"},
      description = "Level of difficulty")
  private LevelParams level = LevelParams.EASY;

  @Parameter(
      names = {"-s", "--settings"},
      arity = 3,
      description =
          "Settings of the mine field. Format `-s length height mines`. Overrides --level.")
  private List<Integer> settings;

  @Parameter(
      names = {"--max-players"},
      description = "Max players in the game.")
  private int maxPlayers = 10;

  @Parameter(
      names = {"--timeout"},
      description = "Launch the game on timeout. Unit is seconds.")
  private long timeout = 10L;

  @Parameter(
      names = {"-h", "--help"},
      help = true)
  private boolean help;

  private Server() {}

  public static void main(String... argv) {
    final var server = new Server();

    try {
      final var jCommander =
          JCommander.newBuilder().programName("minesweeper-server").addObject(server).build();
      jCommander.parse(argv);

      if (server.help) {
        jCommander.usage();
      } else {
        server.run();
      }
    } catch (ParameterException e) {
      System.out.println(e.getLocalizedMessage());
    }
  }

  public void run() {
    final Minefield minefield;
    if (settings != null) {
      minefield = new Minefield(settings.get(0), settings.get(1), settings.get(2), false);
    } else {
      switch (level) {
        case MEDIUM:
          minefield = new Minefield(Level.MEDIUM, false);
          break;
        case HARD:
          minefield = new Minefield(Level.HARD, false);
          break;
        default:
          minefield = new Minefield(Level.EASY, false);
      }
    }
    new GameServer(minefield, maxPlayers, timeout).start(port);
  }
}
