package GUI.Controllers;

import GUI.Model;
import java.awt.event.ActionEvent;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Transmitter;
import javax.swing.JComboBox;
import javax.swing.event.ChangeEvent;

public class SelectorController extends Controller {
  private final JComboBox<String> selector;
  private final Device deviceType;

  public SelectorController(Model transposerModel, JComboBox<String> selector,
      Device deviceType) {
    super(transposerModel);
    this.selector = selector;
    this.deviceType = deviceType;
  }

  //Updates all users to selected sequencer when sequencer selected
  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    String name = (String) selector.getSelectedItem();

    Sequencer sequencer = null;
    Transmitter transmitter = null;

    MidiDevice device;
    MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
    for (int i = 0; i < infos.length; i++) {
      try {
        device = MidiSystem.getMidiDevice(infos[i]);

        if (device.getDeviceInfo().getName().equals(name)) {
          //TODO: This line does not create a valid sequencer
          if (deviceType == Device.SEQUENCER) {
            sequencer = (Sequencer) device;
          } else if (deviceType  == Device.TRANSMITTER) {
            transmitter = device.getTransmitter();
          }
        }
      } catch (MidiUnavailableException e) {
        transposerModel.addStatus("Cannot locate device " + name);
      }
    }

    if (sequencer != null) {
      transposerModel.setSequencer(sequencer);
      transposerModel.addStatus("Sequencer Updated to " + name);
    }

    if (transmitter != null) {
      transposerModel.setTransmitter(transmitter);
      transposerModel.addStatus("Midi Input Updated to " + name);
    }

  }

  //Unused, does nothing as a change listener
  @Override
  public void stateChanged(ChangeEvent changeEvent) {
    //Unused
  }
}
