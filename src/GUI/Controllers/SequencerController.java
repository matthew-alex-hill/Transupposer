package GUI.Controllers;

import GUI.Model;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeEvent;

public class SequencerController extends Controller {

  private static final long OFFSET = 10000000;
  private final SequencerCommand command;


  public SequencerController(Model transposerModel, SequencerCommand command) {
    super(transposerModel);
    this.command = command;
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    switch (command) {
      case PLAY:
        transposerModel.transposeAndPlay();
        break;
      case PAUSE:
        transposerModel.pause();
        break;
      case STOP:
        transposerModel.stop();
        break;
      case FAST_FORWARD:
        transposerModel.changeSpeed(true);
        break;
      case REWIND:
        transposerModel.changeSpeed(false);
        break;
      case STEP_FORWARD:
        transposerModel.step(OFFSET);
        break;
      case STEP_BACKWARD:
        transposerModel.step(-OFFSET);
        break;
      case UPDATE:
        transposerModel.changeTransposer();
        break;
      case SAVE:
        //Does not write to file if a recording is in process
        if (!transposerModel.isRecording()) {
          transposerModel.transposeToFile();
        }
        break;
      case RECORD:
        transposerModel.record();
        break;
    }
  }

  /* Unimplemented, does nothing as a state listener */
  @Override
  public void stateChanged(ChangeEvent changeEvent) {
    //Unused
  }
}
