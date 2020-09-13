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
    ipTextField = new JTextField("127.0.0.1");
    add(ipLabel);
    add(ipTextField);
    // Align label
    _layout.putConstraint(SpringLayout.NORTH, ipLabel, 0, SpringLayout.NORTH, ipTextField);
    _layout.putConstraint(SpringLayout.SOUTH, ipLabel, 0, SpringLayout.SOUTH, ipTextField);
    _layout.putConstraint(SpringLayout.WEST, ipLabel, 5, SpringLayout.WEST, this);
    // Align field
    _layout.putConstraint(SpringLayout.NORTH, ipTextField, 5, SpringLayout.NORTH, this);
    _layout.putConstraint(SpringLayout.WEST, ipTextField, 5, SpringLayout.EAST, ipLabel);
    _layout.putConstraint(SpringLayout.EAST, ipTextField, -5, SpringLayout.EAST, this);

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
    // Align label
    _layout.putConstraint(SpringLayout.NORTH, levelLabel, 0, SpringLayout.NORTH, levelComboBox);
    _layout.putConstraint(SpringLayout.SOUTH, levelLabel, 0, SpringLayout.SOUTH, levelComboBox);
    _layout.putConstraint(SpringLayout.WEST, levelLabel, 5, SpringLayout.WEST, this);
    // Align field
    _layout.putConstraint(SpringLayout.NORTH, levelComboBox, 5, SpringLayout.SOUTH, ipTextField);
    _layout.putConstraint(SpringLayout.EAST, levelComboBox, -5, SpringLayout.EAST, this);
    _layout.putConstraint(SpringLayout.WEST, levelComboBox, 5, SpringLayout.EAST, levelLabel);

    /* Custom Settings */
    sizeLabel = new JLabel("Size");

    final var sizePanel = new JPanel(new GridLayout(1, 2));
    lengthSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
    heightSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
    minesLabel = new JLabel("Mines");
    minesSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));
    disableCustomSettings();
    add(sizeLabel);
    add(minesLabel);
    add(sizePanel);
    sizePanel.add(lengthSpinner);
    sizePanel.add(heightSpinner);

    add(minesSpinner);

    // Size Label
    _layout.putConstraint(SpringLayout.NORTH, sizeLabel, 0, SpringLayout.NORTH, sizePanel);
    _layout.putConstraint(SpringLayout.SOUTH, sizeLabel, 0, SpringLayout.SOUTH, sizePanel);
    _layout.putConstraint(SpringLayout.WEST, sizeLabel, 5, SpringLayout.WEST, this);

    // SizePanel
    _layout.putConstraint(SpringLayout.NORTH, sizePanel, 5, SpringLayout.SOUTH, levelComboBox);
    _layout.putConstraint(SpringLayout.WEST, sizePanel, 5, SpringLayout.EAST, sizeLabel);
    _layout.putConstraint(SpringLayout.EAST, sizePanel, -5, SpringLayout.EAST, this);
    // Mines Label
    _layout.putConstraint(SpringLayout.NORTH, minesLabel, 0, SpringLayout.NORTH, minesSpinner);
    _layout.putConstraint(SpringLayout.SOUTH, minesLabel, 0, SpringLayout.SOUTH, minesSpinner);
    _layout.putConstraint(SpringLayout.WEST, minesLabel, 5, SpringLayout.WEST, this);

    // Mines
    _layout.putConstraint(SpringLayout.NORTH, minesSpinner, 5, SpringLayout.SOUTH, sizePanel);
    _layout.putConstraint(SpringLayout.WEST, minesSpinner, 5, SpringLayout.EAST, minesLabel);
    _layout.putConstraint(SpringLayout.EAST, minesSpinner, -5, SpringLayout.EAST, this);

    startButton = new JButton("Create game !");
    // Align button
    _layout.putConstraint(SpringLayout.NORTH, startButton, 5, SpringLayout.SOUTH, minesSpinner);
    _layout.putConstraint(SpringLayout.WEST, startButton, 5, SpringLayout.WEST, this);
    _layout.putConstraint(SpringLayout.EAST, startButton, -5, SpringLayout.EAST, this);
    // Expand Pane
    _layout.putConstraint(SpringLayout.SOUTH, this, 5, SpringLayout.SOUTH, startButton);
    startButton.setActionCommand("start");
    add(startButton);
  }

  public void addListener(GameCreationController listener) {
    startButton.addActionListener(listener);
    levelComboBox.addItemListener(listener);
    ipTextField.addActionListener(listener);
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
