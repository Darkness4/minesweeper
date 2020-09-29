package marc.nguyen.minesweeper.client.presentation.views.gamecreation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

/** The panel where we CRUD the settings. */
public class SavedSettingsPanel extends JPanel {

  public final JList<String> settingsList;
  public final JTextField settingsNameTextField;
  public final JButton saveButton;
  public final JButton loadButton;
  public final JButton deleteButton;

  public SavedSettingsPanel() {
    assert SwingUtilities.isEventDispatchThread() : "View is running on unsafe thread!";

    setLayout(new BorderLayout());

    settingsList = new JList<>();
    settingsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    final var jScrollPane = new JScrollPane(settingsList);
    jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    add(jScrollPane, BorderLayout.CENTER);

    final var bottomFlow = new JPanel(new FlowLayout());

    settingsNameTextField = new JTextField(10);
    saveButton = new JButton("Save");
    loadButton = new JButton("Load");
    deleteButton = new JButton("Delete");
    bottomFlow.add(settingsNameTextField);
    bottomFlow.add(saveButton);
    bottomFlow.add(loadButton);
    bottomFlow.add(deleteButton);

    add(bottomFlow, BorderLayout.SOUTH);
  }

  public void enableLoadAndDeleteIfValid(boolean isValid) {
    SwingUtilities.invokeLater(
        () -> {
          loadButton.setEnabled(isValid);
          deleteButton.setEnabled(isValid);
        });
  }

  public void enableSaveButtonIfValid(boolean isValid) {
    SwingUtilities.invokeLater(() -> saveButton.setEnabled(isValid));
  }

  public void changeColorNameTextIfValid(boolean isValid) {
    SwingUtilities.invokeLater(
        () -> settingsNameTextField.setBackground(isValid ? Color.WHITE : Color.RED));
  }
}
