package GUI.Controllers;

import GUI.Model;
import Transposition.Note;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;

public class SubmitController extends Controller {

  private final ScaleType scaleType;
  private final JTextField textField;

  public SubmitController(Model transposerModel,
      ScaleType scaleType, JTextField textField) {
    super(transposerModel);
    this.scaleType = scaleType;
    this.textField = textField;
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    Note note = Note.getNoteFromName(textField.getText());

    switch (scaleType) {
      case INPUT:
        transposerModel.setInputRoot(note);
        break;
      case OUTPUT:
        transposerModel.setOutputRoot(note);
        break;
      case LIVE:
        transposerModel.setLiveRoot(note);
    }
  }

  @Override
  public void stateChanged(ChangeEvent changeEvent) {
    //Unused, does nothing as a change listener
  }
}
