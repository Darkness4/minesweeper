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
public class LocalDataSourceMock extends SQLiteDataSource implements LocalDataSource {

  static final String URL = "jdbc:sqlite:testdata.sqlite3";

  @Inject
  public LocalDataSourceMock() {
    setUrl(URL);
  }

  /**
   * Delete the file DB.
   *
   * @return isSuccess?
   */
  public boolean purge() {
    final var file = new File("testdata.sqlite3");
    return file.delete();
  }
}
