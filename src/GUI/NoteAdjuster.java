package GUI;

import GUI.Controllers.NoteAdjusterController;
import Transposition.Note;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NoteAdjuster {

  private final JLabel label;
  private final JComboBox<Note> box;
  private final JPanel panel;

  public NoteAdjuster(Model model, Note note, Note defaultNote) {
    this.label = new JLabel(note.toString() + ':');
    this.box = new JComboBox<>();
    this.panel = new JPanel();

    panel.setLayout(new GridBagLayout());

    box.addActionListener(new NoteAdjusterController(model, note, box));

    Note tmp;
    for (int i = 0; i < 12; i++) {
      tmp = new Note(i);
      box.addItem(tmp);

      if (tmp.equals(defaultNote)) {
        box.setSelectedItem(tmp);
      }
    }

    GridBagConstraints c = new GridBagConstraints();
    c.anchor = GridBagConstraints.LINE_START;
    c.gridx = 0;
    c.gridy = 0;
    c.gridheight = 1;
    c.gridwidth = 1;

    panel.add(label, c);

    c.anchor = GridBagConstraints.CENTER;
    c.gridy = 1;
    panel.add(box, c);
  }

  public JPanel getPanel() {
    return panel;
  }
}
