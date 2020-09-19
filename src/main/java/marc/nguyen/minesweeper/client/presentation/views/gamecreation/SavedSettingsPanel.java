package marc.nguyen.minesweeper.client.presentation.views.gamecreation;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import marc.nguyen.minesweeper.client.presentation.controllers.GameCreationController;

public class SavedSettingsPanel extends JPanel {

  public final JList<String> settingsList;
  public final JTextField settingsNameTextField;
  public final JButton saveButton;
  public final JButton loadButton;
  public final JButton deleteButton;

  public SavedSettingsPanel() {
    if (!SwingUtilities.isEventDispatchThread()) {
      throw new RuntimeException("View is running on unsafe thread!");
    }

    setLayout(new BorderLayout());

    settingsList = new JList<>();
    settingsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    final var jScrollPane = new JScrollPane(settingsList);
    jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    add(jScrollPane, BorderLayout.CENTER);

    final var bottomFlow = new JPanel(new FlowLayout());

    settingsNameTextField = new JTextField(10);
    saveButton = new JButton("Save");
    saveButton.setActionCommand("save_settings");
    loadButton = new JButton("Load");
    loadButton.setActionCommand("load_settings");
    deleteButton = new JButton("Delete");
    deleteButton.setActionCommand("delete_settings");
    bottomFlow.add(settingsNameTextField);
    bottomFlow.add(saveButton);
    bottomFlow.add(loadButton);
    bottomFlow.add(deleteButton);

    add(bottomFlow, BorderLayout.SOUTH);
  }

  public void addListener(GameCreationController listener) {
    saveButton.addActionListener(listener);
    loadButton.addActionListener(listener);
    deleteButton.addActionListener(listener);
    settingsNameTextField.getDocument().addDocumentListener(listener);
    settingsList.addListSelectionListener(listener);
  }
}
