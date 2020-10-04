package marc.nguyen.minesweeper.client.data.datasources;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.sqlite.SQLiteDataSource;

/** Implementation of a local datasource based of SQLite 3. */
@Singleton
public class LocalDataSourceImpl extends SQLiteDataSource implements LocalDataSource {

  static final String URL = "jdbc:sqlite:data.sqlite3";

  @Inject
  public LocalDataSourceImpl() {
    setUrl(URL);
  }
}
