package marc.nguyen.minesweeper.client.data.datasources;

import java.io.File;
import javax.inject.Inject;
import org.sqlite.SQLiteDataSource;

/**
 * A Local Data Source used for unit testing.
 *
 * <p>This class create a SQLite 3 database. After each test, this class should call <code>purge()
 * </code>.
 */
public class MockLocalDataSource extends SQLiteDataSource implements LocalDataSource {

  static String URL = "jdbc:sqlite:testdata.sqlite3";

  @Inject
  public MockLocalDataSource() {
    setUrl(URL);
  }

  public boolean purge() {
    final var file = new File("testdata.sqlite3");
    return file.delete();
  }
}
