package marc.nguyen.minesweeper.client.core.usecases;

/**
 * Use case based on the Clean Architecture.
 *
 * <p>These use cases orchestrate the flow of data to and from the entities, and direct those
 * entities to use their Critical Business Rules to achieve the goals of the use case.
 *
 * @param <I> Parameter type
 * @param <O> Return type
 */
@FunctionalInterface
public interface UseCase<I, O> {

  O execute(I params);
}
