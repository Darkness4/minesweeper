package marc.nguyen.minesweeper.client.core.usecases;

public interface UseCase<I, O> {
  O execute(I params);
}
