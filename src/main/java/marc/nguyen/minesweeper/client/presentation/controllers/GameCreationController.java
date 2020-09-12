package marc.nguyen.minesweeper.client.presentation.controllers;

import com.squareup.inject.assisted.Assisted;
import com.squareup.inject.assisted.AssistedInject;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import marc.nguyen.minesweeper.client.domain.usecases.Connect;
import marc.nguyen.minesweeper.client.presentation.models.GameCreationModel;
import marc.nguyen.minesweeper.client.presentation.views.GameCreationView;
import marc.nguyen.minesweeper.common.data.models.Level;

public class GameCreationController implements ActionListener {

  private final GameCreationModel _model;
  private final GameCreationView _view;
  private final Connect _connect;

  @AssistedInject
  public GameCreationController(
      Connect connect, @Assisted GameCreationModel model, @Assisted GameCreationView view) {
    _model = model;
    _view = view;
    _connect = connect;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    final Object o = e.getSource();
    if (o instanceof JCheckBox) {
      @SuppressWarnings("unchecked")
      final JComboBox<Level> cb = (JComboBox<Level>) e.getSource();
      final Level level = (Level) cb.getSelectedItem();
      assert level != null;
      _model.setLevel(level);
    }
    if (e.getActionCommand().equals("start")) {
      // TODO: Dagger invoke MainView
    }
  }

  @AssistedInject.Factory
  public interface Factory {

    GameCreationController create(GameCreationModel model, GameCreationView view);
  }
}
