package marc.nguyen.minesweeper.client.data.datasources;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.sqlite.SQLiteDataSource;

@Singleton
public class LocalDataSourceImpl extends SQLiteDataSource implements LocalDataSource {

  static String URL = "jdbc:sqlite:data.sqlite3";

  @Inject
  public LocalDataSourceImpl() {
    setUrl(URL);
  }
}
