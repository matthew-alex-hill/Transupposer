package GUI.Controllers;

import GUI.Model;
import java.awt.event.ActionEvent;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.swing.JComboBox;
import javax.swing.event.ChangeEvent;

public class SequencerSelectorController extends Controller {
  private final JComboBox<String> selector;

  public SequencerSelectorController(Model transposerModel, JComboBox<String> selector) {
    super(transposerModel);
    this.selector = selector;
  }

  //Updates all users to selected sequencer when sequencer selected
  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    String name = (String) selector.getSelectedItem();

    Sequencer sequencer = null;

    MidiDevice device;
    MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
    for (int i = 0; i < infos.length; i++) {
      try {
        device = MidiSystem.getMidiDevice(infos[i]);

        if (device.getDeviceInfo().getName().equals(name)) {
          //TODO: This line does not create a valid sequencer
          sequencer = (Sequencer) device;
        }
      } catch (MidiUnavailableException e) {
        transposerModel.addStatus("Cannot locate device " + name);
      }
    }

    if (sequencer != null) {
      transposerModel.setSequencer(sequencer);
      transposerModel.addStatus("Sequencer Updated to " + name);
    }
  }

  //Unused, does nothing as a change listener
  @Override
  public void stateChanged(ChangeEvent changeEvent) {
    //Unused
  }
}
