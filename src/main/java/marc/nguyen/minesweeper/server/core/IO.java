package marc.nguyen.minesweeper.server.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IO {
  public static final ExecutorService executor = Executors.newCachedThreadPool();
}
