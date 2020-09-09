package marc.nguyen.minesweeper.common.domain.usecases;

public interface UseCase<I, O> {
  O execute(I params);
}
