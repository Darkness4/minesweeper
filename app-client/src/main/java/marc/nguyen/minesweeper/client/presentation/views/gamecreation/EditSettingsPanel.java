package marc.nguyen.minesweeper.client.presentation.views.gamecreation;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import marc.nguyen.minesweeper.client.di.components.DaggerLeaderboardComponent;
import marc.nguyen.minesweeper.client.domain.entities.GameMode;
import marc.nguyen.minesweeper.client.presentation.models.GameCreationModel;
import marc.nguyen.minesweeper.client.presentation.views.gamecreation.settings.GameSettingsPanel;
import marc.nguyen.minesweeper.client.presentation.views.gamecreation.settings.NetworkSettingsPanel;

/** The panel where we edit the settings. */
public class EditSettingsPanel extends JPanel {

  public final JComboBox<GameMode> gameModeComboBox;
  public final JTextField playerTextField;
  public final GameSettingsPanel gameSettingsPanel;
  public final NetworkSettingsPanel networkSettingsPanel;
  public final JButton startButton;
  public final JButton leaderboardButton;
  private final JPanel cards = new JPanel();

  public EditSettingsPanel() {
    assert SwingUtilities.isEventDispatchThread() : "View is running on unsafe thread!";

    cards.setLayout(new CardLayout());
    final var layout = new BoxLayout(this, BoxLayout.Y_AXIS);
    setLayout(layout);

    gameModeComboBox = new JComboBox<>(GameCreationModel.gameModeChoices);
    gameModeComboBox.setEditable(false);
    gameModeComboBox.setRenderer(
        new BasicComboBoxRenderer() {
          @Override
          public Component getListCellRendererComponent(
              JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof GameMode) {
              GameMode item = (GameMode) value;

              setText(item.name());
            }
            return this;
          }
        });

    playerTextField = new JTextField("Player");

    gameSettingsPanel = new GameSettingsPanel();
    networkSettingsPanel = new NetworkSettingsPanel();

    cards.add(gameSettingsPanel, GameMode.SINGLEPLAYER.name());
    cards.add(networkSettingsPanel, GameMode.MULTIPLAYER.name());

    final var buttonPanel = new JPanel(new FlowLayout());
    startButton = new JButton("Play !");
    startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    buttonPanel.add(startButton);

    leaderboardButton = new JButton("Leaderboard");
    leaderboardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    leaderboardButton.addActionListener(
        e ->
            SwingUtilities.invokeLater(
                () -> DaggerLeaderboardComponent.builder().build().leaderboardDialog()));
    buttonPanel.add(leaderboardButton);

    add(gameModeComboBox);
    add(playerTextField);
    add(cards);
    add(buttonPanel);

    setOpaque(true);
  }

  public void changeCard(ItemEvent e) {
    SwingUtilities.invokeLater(
        () -> {
          CardLayout cl = (CardLayout) (cards.getLayout());
          cl.show(cards, ((GameMode) e.getItem()).name());
        });
  }
}
