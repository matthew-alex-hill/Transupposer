package GUI;

import java.awt.event.ActionEvent;
import javax.sound.midi.Sequencer;
import javax.swing.event.ChangeEvent;

public class SequencerController extends Controller {

  private static final long OFFSET = 10000000;
  private final SequencerCommand command;
  private Sequencer sequencer;


  public SequencerController(Model transposerModel, Sequencer sequencer, SequencerCommand command) {
    super(transposerModel);
    this.command = command;
    this.sequencer = sequencer;
  }

  public void setSequencer(Sequencer sequencer) {
    if (this.sequencer.isOpen()) {
      this.sequencer.close();
    }

    this.sequencer = sequencer;
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    switch (command) {
      case PLAY:
        transposerModel.transposeAndPlay(sequencer);
        break;
      case STOP:
        transposerModel.stop(sequencer);
        break;
      case FAST_FORWARD:
        transposerModel.changeSpeed(sequencer, true);
        break;
      case REWIND:
        transposerModel.changeSpeed(sequencer, false);
        break;
      case STEP_FORWARD:
        transposerModel.step(sequencer, OFFSET);
        break;
      case STEP_BACKWARD:
        transposerModel.step(sequencer, -OFFSET);
        break;
    }
  }

  /* Unimplemented, does nothing as a state listener */
  @Override
  public void stateChanged(ChangeEvent changeEvent) {
    //Unused
  }
}
