package GUI;

import Transposition.Note;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.event.ChangeEvent;

public class NoteAdjusterController extends Controller {

  Note base;
  private JComboBox<Note> box;

  public NoteAdjusterController(Model transposerModel, Note base, JComboBox<Note> box) {
    super(transposerModel);
    this.box = box;
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    Note transposed = (Note) box.getSelectedItem();
  }


  @Override
  public void stateChanged(ChangeEvent changeEvent) {
    //Unused
  }
}
