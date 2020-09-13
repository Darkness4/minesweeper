package marc.nguyen.minesweeper.client.presentation.views.gamecreation;

import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import marc.nguyen.minesweeper.client.presentation.controllers.GameCreationController;
import marc.nguyen.minesweeper.client.presentation.models.GameCreationModel;
import marc.nguyen.minesweeper.client.presentation.utils.SpringUtilities;
import marc.nguyen.minesweeper.common.data.models.Level;

public class SettingsPanel extends JPanel {

  public final JButton startButton;
  public final JComboBox<Level> levelComboBox;
  public final JTextField ipTextField;
  public final JLabel sizeLabel;
  public final JSpinner lengthSpinner;
  public final JSpinner heightSpinner;
  public final JLabel minesLabel;
  public final JSpinner minesSpinner;

  final SpringLayout _layout;

  public SettingsPanel() {
    if (!SwingUtilities.isEventDispatchThread()) {
      throw new RuntimeException("View is running on unsafe thread!");
    }

    _layout = new SpringLayout();
    setLayout(_layout);

    /* IP settings */
    final var ipLabel = new JLabel("IP Address");
    ipTextField = new JTextField();
    add(ipLabel);
    add(ipTextField);

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

    SpringUtilities.makeCompactGrid(this, 4, 2, 6, 6, 6, 6);

    /* Start */
    startButton = new JButton("Create game !");
    startButton.setActionCommand("start");
    add(startButton);
    // Align button
    _layout.putConstraint(SpringLayout.NORTH, startButton, 5, SpringLayout.SOUTH, minesSpinner);
    _layout.putConstraint(SpringLayout.WEST, startButton, 5, SpringLayout.WEST, this);
    _layout.putConstraint(SpringLayout.EAST, startButton, -5, SpringLayout.EAST, this);
    // Expand Pane
    _layout.putConstraint(SpringLayout.SOUTH, this, 5, SpringLayout.SOUTH, startButton);

    setOpaque(true);
  }

  public void addListener(GameCreationController listener) {
    startButton.addActionListener(listener);
    levelComboBox.addItemListener(listener);
    ipTextField.getDocument().addDocumentListener(listener);
    lengthSpinner.addChangeListener(listener);
    heightSpinner.addChangeListener(listener);
    minesSpinner.addChangeListener(listener);
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
