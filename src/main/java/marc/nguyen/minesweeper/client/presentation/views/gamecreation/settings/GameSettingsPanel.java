package marc.nguyen.minesweeper.client.presentation.views.gamecreation.settings;

import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import marc.nguyen.minesweeper.client.presentation.models.GameCreationModel;
import marc.nguyen.minesweeper.client.presentation.utils.SpringUtilities;
import marc.nguyen.minesweeper.common.data.models.Level;

public class GameSettingsPanel extends JPanel {

  public final JComboBox<Level> levelComboBox;
  public final JLabel sizeLabel;
  public final JSpinner lengthSpinner;
  public final JSpinner heightSpinner;
  public final JLabel minesLabel;
  public final JSpinner minesSpinner;

  public GameSettingsPanel() {
    assert SwingUtilities.isEventDispatchThread() : "View is running on unsafe thread!";

    setLayout(new SpringLayout());
    setAlignmentX(Component.CENTER_ALIGNMENT);

    /* Level settings */
    final var levelLabel = new JLabel("Level");
    levelComboBox = new JComboBox<>(GameCreationModel.levelChoices);
    levelComboBox.setRenderer(
        new BasicComboBoxRenderer() {
          @Override
          public Component getListCellRendererComponent(
              JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Level) {
              Level item = (Level) value;

              setText(item.name());
            }
            return this;
          }
        });
    add(levelLabel);
    add(levelComboBox);

    /* Custom Settings */
    sizeLabel = new JLabel("Size");
    final var sizePanel = new JPanel(new GridLayout(1, 2));
    lengthSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
    heightSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
    sizePanel.add(lengthSpinner);
    sizePanel.add(heightSpinner);
    add(sizeLabel);
    add(sizePanel);

    minesLabel = new JLabel("Mines");
    minesSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));
    disableCustomSettings();
    add(minesLabel);
    add(minesSpinner);

    SpringUtilities.makeCompactGrid(this, 3, 2, 6, 6, 6, 6);
  }

  public void enableCustomSettings() {
    sizeLabel.setEnabled(true);
    lengthSpinner.setEnabled(true);
    heightSpinner.setEnabled(true);
    minesSpinner.setEnabled(true);
    minesLabel.setEnabled(true);
  }

  public void disableCustomSettings() {
    sizeLabel.setEnabled(false);
    lengthSpinner.setEnabled(false);
    heightSpinner.setEnabled(false);
    minesSpinner.setEnabled(false);
    minesLabel.setEnabled(false);
  }
}
