package GUI;

import java.awt.event.ActionEvent;
import javax.swing.event.ChangeEvent;

/* Action listener for transpose button */
public class transposeController extends Controller{

  public transposeController(Model model) {
    super(model);
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    transposerModel.transposeToFile();
  }

  @Override
  public void stateChanged(ChangeEvent changeEvent) {
    //unused, does nothing as a changeListener
  }
}
