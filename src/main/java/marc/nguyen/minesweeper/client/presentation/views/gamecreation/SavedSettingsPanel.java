package marc.nguyen.minesweeper.client.presentation.views.gamecreation;

import javax.swing.JList;
import javax.swing.JPanel;

public class SavedSettingsPanel extends JPanel {
  public final JList<String> settingsList;

  public SavedSettingsPanel() {
    String[] week = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    settingsList = new JList<>(week);
    add(settingsList);
  }
}
