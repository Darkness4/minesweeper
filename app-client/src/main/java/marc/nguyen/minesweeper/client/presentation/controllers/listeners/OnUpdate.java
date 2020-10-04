package marc.nguyen.minesweeper.client.presentation.controllers.listeners;

import java.util.function.Consumer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A Document listener that execute a consumer of document events of every type (insert, remove,
 * changed).
 */
public class OnUpdate implements DocumentListener {

  final Consumer<DocumentEvent> consumer;

  public OnUpdate(Consumer<DocumentEvent> consumer) {
    this.consumer = consumer;
  }

  @Override
  public void insertUpdate(DocumentEvent e) {
    consumer.accept(e);
  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    consumer.accept(e);
  }

  @Override
  public void changedUpdate(DocumentEvent e) {
    consumer.accept(e);
  }
}
