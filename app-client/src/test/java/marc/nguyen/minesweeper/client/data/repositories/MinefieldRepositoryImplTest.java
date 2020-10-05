package marc.nguyen.minesweeper.client.data.repositories;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.TestObserver;
import marc.nguyen.minesweeper.client.data.devices.ServerSocketDevice;
import marc.nguyen.minesweeper.client.domain.repositories.MinefieldRepository;
import marc.nguyen.minesweeper.common.data.models.Level;
import marc.nguyen.minesweeper.common.data.models.Minefield;
import marc.nguyen.minesweeper.common.data.models.Player;
import marc.nguyen.minesweeper.common.data.models.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MinefieldRepositoryImplTest {

  MinefieldRepository repository;
  ServerSocketDevice device;
  Lazy<ServerSocketDevice> deviceLazy;

  @BeforeEach
  void setUp(@Mock ServerSocketDevice device, @Mock Lazy<ServerSocketDevice> deviceLazy) {
    this.device = device;
    this.deviceLazy = deviceLazy;
    repository = new MinefieldRepositoryImpl(deviceLazy);

    when(deviceLazy.get()).thenReturn(device);
  }

  @Test
  void fetch() {
    // Arrange
    final var t = new Minefield(Level.EASY, true);
    when(device.getObservable())
        .thenReturn(Observable.just("trash object", t, new Player("Not the right data")));
    final TestObserver<Minefield> observer = new TestObserver<>();

    // Act
    repository.fetch().subscribe(observer);

    // Assert
    observer.assertComplete();
    observer.assertValue(t);
  }

  @Test
  void watchTiles() {
    // Arrange
    final var t = new Position(0, 0);
    when(device.getObservable())
        .thenReturn(Observable.just("trash object", t, new Player("Not the right data")));
    final TestObserver<Position> observer = new TestObserver<>();

    // Act
    repository.watchTiles().subscribe(observer);

    // Assert
    observer.assertComplete();
    observer.assertValue(t);
  }

  @Test
  void updateTile() {
    // Arrange
    final var t = new Position(0, 0);
    final TestObserver<Position> observer = new TestObserver<>();

    // Act
    repository.updateTile(t).subscribe(observer);

    // Assert
    observer.assertComplete();
    verify(device).write(t);
  }
}
