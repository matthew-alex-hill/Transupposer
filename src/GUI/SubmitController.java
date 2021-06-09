package GUI;

import Transposition.Note;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;

public class SubmitController extends Controller {

  private boolean isInput;
  private JTextField textField;

  public SubmitController(Model transposerModel, boolean isInput, JTextField textField) {
    super(transposerModel);

    this.isInput = isInput;
    this.textField = textField;
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    Note note = Note.getNoteFromName(textField.getText());

    if (isInput) {
      transposerModel.setInputRoot(note);
    } else {
      transposerModel.setOutputRoot(note);
    }
  }

  @Override
  public void stateChanged(ChangeEvent changeEvent) {
    //Unused, does nothing as a change listener
  }
}
