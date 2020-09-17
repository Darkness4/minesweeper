package marc.nguyen.minesweeper.client.data.datasources;

import java.io.File;
import javax.inject.Inject;
import org.sqlite.SQLiteDataSource;

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
