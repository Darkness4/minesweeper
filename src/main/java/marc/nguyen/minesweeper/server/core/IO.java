package marc.nguyen.minesweeper.server.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Custom IO Threads for the Server.
 *
 * <p>These threads should only be used for non-intensive CPU work (e.g Accessing file, network
 * calls...). These can also be calls Worker threads, also known as background threads, where
 * time-consuming background tasks are executed.
 *
 * @see <a
 *     href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/ExecutorService.html">ExecutorService</a>
 */
public class IO {

  public static final ExecutorService executor = Executors.newCachedThreadPool();
}
