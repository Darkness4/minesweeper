package marc.nguyen.minesweeper.client.data.repositories;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dagger.Lazy;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.TestObserver;
import java.util.List;
import marc.nguyen.minesweeper.client.data.devices.ServerSocketDevice;
import marc.nguyen.minesweeper.client.domain.repositories.PlayerRepository;
import marc.nguyen.minesweeper.common.data.models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlayerRepositoryImplTest {

  PlayerRepository repository;
  ServerSocketDevice device;
  Lazy<ServerSocketDevice> deviceLazy;

  @BeforeEach
  void setUp(@Mock ServerSocketDevice device, @Mock Lazy<ServerSocketDevice> deviceLazy) {
    this.device = device;
    this.deviceLazy = deviceLazy;
    repository = new PlayerRepositoryImpl(deviceLazy);

    when(deviceLazy.get()).thenReturn(device);
  }

  @Test
  void update() {
    // Arrange
    final var t = new Player("name");
    final TestObserver<Player> observer = new TestObserver<>();

    // Act
    repository.update(t).subscribe(observer);

    // Assert
    observer.assertComplete();
    verify(device).write(t);
  }

  @Test
  void fetchServerPlayerList() {
    // Arrange
    final var t = List.of(new Player("name"));
    when(device.getObservable())
        .thenReturn(Observable.just("trash object", t, new Player("Not the right data")));
    final TestObserver<List<Player>> observer = new TestObserver<>();

    // Act
    repository.fetchServerPlayerList().subscribe(observer);

    // Assert
    observer.assertComplete();
    observer.assertValue(t);
  }
}
