package GUI;

import Transposition.Note;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.event.ChangeEvent;

public class NoteAdjusterController extends Controller {

  Note base;
  private final JComboBox<Note> box;

  public NoteAdjusterController(Model transposerModel, Note base, JComboBox<Note> box) {
    super(transposerModel);
    this.box = box;
    this.base = base;
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    Note transposed = (Note) box.getSelectedItem();
    transposerModel.addCustomInterval(base, transposed);
  }


  @Override
  public void stateChanged(ChangeEvent changeEvent) {
    //Unused
  }
}
