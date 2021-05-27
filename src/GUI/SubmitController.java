package GUI;

import Transposition.Note;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

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

    if (note == null) {
      //TODO: use actual error field when you make it
      textField.setText("ERROR");
    }
  }
}
