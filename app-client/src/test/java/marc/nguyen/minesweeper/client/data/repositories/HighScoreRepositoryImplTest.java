package marc.nguyen.minesweeper.client.data.repositories;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dagger.Lazy;
import io.reactivex.rxjava3.observers.TestObserver;
import java.util.List;
import marc.nguyen.minesweeper.client.data.database.HighScoreDao;
import marc.nguyen.minesweeper.client.domain.entities.HighScore;
import marc.nguyen.minesweeper.client.domain.repositories.HighScoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HighScoreRepositoryImplTest {

  HighScoreRepository repository;
  HighScoreDao dao;
  Lazy<HighScoreDao> daoLazy;

  @BeforeEach
  void setUp(@Mock HighScoreDao dao, @Mock Lazy<HighScoreDao> daoLazy) {
    this.dao = dao;
    this.daoLazy = daoLazy;
    repository = new HighScoreRepositoryImpl(daoLazy);

    when(daoLazy.get()).thenReturn(dao);
  }

  @Test
  void save() {
    // Arrange
    final var t = new HighScore("name", 1, 2, 3, 4);
    final TestObserver<HighScore> observer = new TestObserver<>();
    // Act
    repository.save(t).subscribe(observer);
    // Assert
    observer.assertComplete();
    verify(dao).insert(t);
  }

  @Test
  void findAll() {
    // Arrange
    final var tList = List.of(new HighScore("name", 1, 2, 3, 4));
    when(dao.findAll()).thenReturn(tList);
    final TestObserver<List<HighScore>> observer = new TestObserver<>();
    // Act
    repository.findAll().subscribe(observer);
    // Assert
    verify(dao).findAll();
    observer.assertComplete();
    observer.assertValue(tList);
  }
}
