package marc.nguyen.minesweeper.client.core.usecases;

@FunctionalInterface
public interface UseCase<I, O> {
  O execute(I params);
}
