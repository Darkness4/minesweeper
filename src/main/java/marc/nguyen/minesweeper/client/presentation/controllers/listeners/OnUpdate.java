package marc.nguyen.minesweeper.client.presentation.controllers.listeners;

import java.util.function.Function;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class OnUpdate implements DocumentListener {

  final Function<DocumentEvent, Void> executable;

  public OnUpdate(Function<DocumentEvent, Void> executable) {
    this.executable = executable;
  }

  @Override
  public void insertUpdate(DocumentEvent e) {
    executable.apply(e);
  }

  @Override
  public void removeUpdate(DocumentEvent e) {
    executable.apply(e);
  }

  @Override
  public void changedUpdate(DocumentEvent e) {
    executable.apply(e);
  }
}
