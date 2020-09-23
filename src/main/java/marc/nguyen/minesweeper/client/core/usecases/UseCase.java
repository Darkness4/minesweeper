package marc.nguyen.minesweeper.client.core.usecases;

/**
 * Use case based on the Clean Architecture.
 *
 * <p>These use cases orchestrate the flow of data to and from the entities, and direct those
 * entities to use their Critical Business Rules to achieve the goals of the use case.
 *
 * <p>Precisely, this class will make sure that the IO Thread is used for IO tasks, and will make
 * sure that the use cases with the interface of the app are assured.
 *
 * @param <I> Parameter type
 * @param <O> Return type
 */
@FunctionalInterface
public interface UseCase<I, O> {

  O execute(I params);
}
