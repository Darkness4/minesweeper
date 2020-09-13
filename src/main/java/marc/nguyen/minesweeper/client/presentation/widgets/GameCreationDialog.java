package marc.nguyen.minesweeper.client.presentation.widgets;

import javax.inject.Inject;
import marc.nguyen.minesweeper.client.presentation.controllers.GameCreationController;
import marc.nguyen.minesweeper.client.presentation.models.GameCreationModel;
import marc.nguyen.minesweeper.client.presentation.views.GameCreationView;

public class GameCreationDialog extends Dialog {

  @Inject
  public GameCreationDialog(
      GameCreationView view,
      GameCreationModel model,
      GameCreationController.Factory gameCreationControllerFactory) {
    super(view);
    gameCreationControllerFactory.create(model, view);

    setTitle("Create a new game.");
  }
}
