package GUI;

import Transposition.Note;
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

    box.addActionListener(new NoteAdjusterController(model, note, box));

    Note tmp;
    for (int i = 0; i < 12; i++) {
      tmp = new Note(i);
      box.addItem(tmp);

      if (tmp.equals(defaultNote)) {
        box.setSelectedItem(tmp);
      }
    }

    panel.add(label);
    panel.add(box);
  }

  public JPanel getPanel() {
    return panel;
  }
}
