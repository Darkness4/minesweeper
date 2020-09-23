package marc.nguyen.minesweeper.server;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import java.util.List;
import marc.nguyen.minesweeper.common.data.models.Level;
import marc.nguyen.minesweeper.server.api.GameServer;
import marc.nguyen.minesweeper.server.enums.LevelParams;

public final class Server {

  @Parameter(
      names = {"-p", "--port"},
      description = "TCP Port to be opened")
  private int port = 12345;

  @Parameter(
      names = {"-l", "--Level"},
      description = "Level of difficulty")
  private LevelParams level = LevelParams.EASY;

  @Parameter(
      names = {"-s", "--settings"},
      arity = 3,
      description =
          "Settings of the mine field. Format `-s length height mines`. Overrides --Level.")
  private List<Integer> settings;

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
    final GameServer gameServer;
    if (settings != null) {
      gameServer = new GameServer(settings.get(0), settings.get(1), settings.get(2));
    } else {
      switch (level) {
        case MEDIUM:
          gameServer = new GameServer(Level.MEDIUM);
          break;
        case HARD:
          gameServer = new GameServer(Level.HARD);
          break;
        default:
          gameServer = new GameServer(Level.EASY);
      }
    }
    gameServer.start(port);
  }
}
