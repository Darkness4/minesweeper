package marc.nguyen.minesweeper.client.core.mvc;

/**
 * Controller used in the MVC pattern.
 *
 * <p>The controller responds to the user input and performs interactions on the data model objects.
 * The controller receives the input, optionally validates it and then passes the input to the
 * model.
 *
 * @param <M> A Model injected in the controller.
 * @param <V> A View injected in the controller.
 */
public interface Controller<M extends Model, V extends View> {}
