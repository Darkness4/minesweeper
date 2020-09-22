package marc.nguyen.minesweeper.client.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IO {
  public static final ExecutorService executor = Executors.newFixedThreadPool(64);
}
