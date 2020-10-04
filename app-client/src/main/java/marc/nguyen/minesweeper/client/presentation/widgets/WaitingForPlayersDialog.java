package marc.nguyen.minesweeper.client.presentation.widgets;

import io.reactivex.rxjava3.core.Observable;
import java.awt.event.WindowListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import marc.nguyen.minesweeper.common.data.models.StartGame;

public class WaitingForPlayersDialog extends JDialog {

  public WaitingForPlayersDialog(WindowListener windowListener, Observable<StartGame> startGame$) {
    super((JFrame) null, "Waiting for others to connect...");
    assert SwingUtilities.isEventDispatchThread() : "View is running on unsafe thread!";

    startGame$.subscribe(
        s -> {
          removeWindowListener(windowListener);
          dispose();
        },
        t -> {
          System.err.println("[ERROR] startGame$ error.");
          t.printStackTrace();
        });
    setModalityType(ModalityType.APPLICATION_MODAL);
    final var text = new JLabel("Waiting for others to connect...");
    text.setBorder(new EmptyBorder(10, 10, 10, 10));
    add(text);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    addWindowListener(windowListener);
    pack();
    setVisible(true);
  }
}
