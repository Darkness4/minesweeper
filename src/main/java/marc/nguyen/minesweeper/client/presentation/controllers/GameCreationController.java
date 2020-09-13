package marc.nguyen.minesweeper.client.presentation.controllers;

import com.squareup.inject.assisted.Assisted;
import com.squareup.inject.assisted.AssistedInject;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import marc.nguyen.minesweeper.client.di.components.DaggerMainComponent;
import marc.nguyen.minesweeper.client.domain.usecases.Connect;
import marc.nguyen.minesweeper.client.presentation.models.GameCreationModel;
import marc.nguyen.minesweeper.client.presentation.views.GameCreationView;
import marc.nguyen.minesweeper.common.data.models.Level;
import marc.nguyen.minesweeper.common.data.models.Minefield;

public class GameCreationController implements ActionListener, ItemListener, ChangeListener {

  private final GameCreationModel _model;
  private final GameCreationView _view;
  private final Connect _connect;

  @AssistedInject
  public GameCreationController(
      Connect connect, @Assisted GameCreationModel model, @Assisted GameCreationView view) {
    _model = model;
    _view = view;
    _connect = connect;

    _view.settingsPanel.addListener(this);
  }

  /**
   * Handles StartButton events.
   *
   * @param e Events
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    new Thread(
            () -> {
              final String action = e.getActionCommand();

              if (action != null && action.equals("start")) {
                onStartButtonPushed();
              }
            })
        .start();
  }

  private void onStartButtonPushed() {
    // TODO: Connect
    final var level = _model.getLevel();

    final Minefield minefield;
    if (level == Level.CUSTOM) {
      minefield = new Minefield(_model.getLength(), _model.getHeight());
      minefield.placeMines(_model.getMines());
    } else {
      minefield = new Minefield(level);
    }

    SwingUtilities.windowForComponent(_view).dispose();
    SwingUtilities.invokeLater(
        () -> {
          DaggerMainComponent.builder().minefield(minefield).build().mainFrame();
        });
  }

  /**
   * Handles Level Combo Box events
   *
   * @param e Events
   */
  @Override
  public void itemStateChanged(ItemEvent e) {
    final var item = e.getItem();
    if (item instanceof Level) {
      final Level level = (Level) item;
      _model.setLevel(level);
      if (level == Level.CUSTOM) {
        SwingUtilities.invokeLater(_view.settingsPanel::enableCustomSettings);
      } else {
        SwingUtilities.invokeLater(
            () -> {
              _view.settingsPanel.disableCustomSettings();
              _view.settingsPanel.lengthSpinner.setValue(level.length);
              _view.settingsPanel.heightSpinner.setValue(level.height);
              _view.settingsPanel.minesSpinner.setValue(level.mines);
            });
      }
    }
  }

  /**
   * Handles spinners.
   *
   * @param e Events
   */
  @Override
  public void stateChanged(ChangeEvent e) {
    final var source = e.getSource();
    if (source == _view.settingsPanel.lengthSpinner) {
      _model.setLength((Integer) _view.settingsPanel.lengthSpinner.getValue());
    } else if (source == _view.settingsPanel.heightSpinner) {
      _model.setHeight((Integer) _view.settingsPanel.heightSpinner.getValue());
    } else if (source == _view.settingsPanel.minesSpinner) {
      _model.setMines((Integer) _view.settingsPanel.minesSpinner.getValue());
    }
  }

  @AssistedInject.Factory
  public interface Factory {

    GameCreationController create(GameCreationModel model, GameCreationView view);
  }
}
