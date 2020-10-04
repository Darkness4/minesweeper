package marc.nguyen.minesweeper.client.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Custom IO Threads for the Client.
 *
 * <p>These threads should only be used for non-intensive CPU work (e.g Accessing file, network
 * calls...). These can also be calls Worker threads, also known as background threads, where
 * time-consuming background tasks are executed.
 *
 * @see <a href="https://docs.oracle.com/javase/tutorial/uiswing/concurrency/">Concurrency in
 *     Swing</a>
 */
public class IO {

  public static final ExecutorService executor = Executors.newFixedThreadPool(64);
}
