package GUI;

import java.awt.event.ActionEvent;
import javax.sound.midi.Sequencer;
import javax.swing.event.ChangeEvent;

public class transposerUpdateController extends Controller {

  private Sequencer sequencer;

  public transposerUpdateController(Model transposerModel, Sequencer sequencer) {
    super(transposerModel);
    this.sequencer = sequencer;
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    transposerModel.changeTransposer(sequencer);
  }

  @Override
  public void stateChanged(ChangeEvent changeEvent) {

  }
}
