package marc.nguyen.minesweeper.client;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import marc.nguyen.minesweeper.client.di.components.DaggerGameCreationComponent;

/** Main Client Entrypoint. */
public final class Client {

  // TODO: Multiplayer leaderboard
  public static void main(String[] args) {
    SwingUtilities.invokeLater(
        () -> {
          try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          } catch (Exception e) {
            e.printStackTrace();
          }

          DaggerGameCreationComponent.builder().build().gameCreationDialog();
        });
  }
}
