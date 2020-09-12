package marc.nguyen.minesweeper.client.presentation.views.gamecreation;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import marc.nguyen.minesweeper.client.presentation.models.GameCreationModel;
import marc.nguyen.minesweeper.common.data.models.Level;

public class SettingsPanel extends JPanel {

  public SettingsPanel() {
    if (!SwingUtilities.isEventDispatchThread()) {
      throw new RuntimeException("View is running on unsafe thread!");
    }

    final var layout = new SpringLayout();
    setLayout(layout);

    /* IP settings */
    final var label = new JLabel("IP Address");
    final var field = new JTextField("127.0.0.1");
    add(label);
    add(field);
    // Align label
    layout.putConstraint(SpringLayout.NORTH, label, 0, SpringLayout.NORTH, field);
    layout.putConstraint(SpringLayout.SOUTH, label, 0, SpringLayout.SOUTH, field);
    layout.putConstraint(SpringLayout.WEST, label, 5, SpringLayout.WEST, this);
    // Align field
    layout.putConstraint(SpringLayout.NORTH, field, 5, SpringLayout.NORTH, this);
    layout.putConstraint(SpringLayout.WEST, field, 5, SpringLayout.EAST, label);
    // Expand Pane
    layout.putConstraint(SpringLayout.EAST, this, 5, SpringLayout.EAST, field);

    /* Level settings */
    final var label2 = new JLabel("Level");
    final var field2 = new JComboBox<>(GameCreationModel.levelChoices);
    field2.setRenderer(
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
    add(label2);
    add(field2);
    // Align label
    layout.putConstraint(SpringLayout.NORTH, label2, 0, SpringLayout.NORTH, field2);
    layout.putConstraint(SpringLayout.SOUTH, label2, 0, SpringLayout.SOUTH, field2);
    layout.putConstraint(SpringLayout.WEST, label2, 5, SpringLayout.WEST, this);
    // Align field
    layout.putConstraint(SpringLayout.NORTH, field2, 5, SpringLayout.SOUTH, field);
    layout.putConstraint(SpringLayout.EAST, field2, 0, SpringLayout.EAST, field);
    layout.putConstraint(SpringLayout.WEST, field2, 5, SpringLayout.EAST, label2);

    final var button = new JButton("Create game !");
    // Align button
    layout.putConstraint(SpringLayout.NORTH, button, 5, SpringLayout.SOUTH, field2);
    layout.putConstraint(SpringLayout.WEST, button, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.EAST, button, 0, SpringLayout.EAST, field);
    // Expand Pane
    layout.putConstraint(SpringLayout.SOUTH, this, 5, SpringLayout.SOUTH, button);
    button.setActionCommand("start");
    add(button);
  }
}
