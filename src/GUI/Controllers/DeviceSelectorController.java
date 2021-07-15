package GUI.Controllers;

import GUI.Model;
import GUI.transposerGUI;
import java.awt.event.ActionEvent;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.JComboBox;
import javax.swing.event.ChangeEvent;

/* Controller for device selection dropdown menu */
public class DeviceSelectorController extends Controller {
  private final JComboBox<String> selector;
  private final Device deviceType;

  public DeviceSelectorController(Model transposerModel, JComboBox<String> selector,
      Device deviceType) {
    super(transposerModel);
    this.selector = selector;
    this.deviceType = deviceType;
  }

  //Updates all users to selected sequencer when sequencer selected
  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    String name = (String) selector.getSelectedItem();

    //Nothing selected, remove current device
    if (name == null || name.equals(transposerGUI.selectNothingText)) {
      if (deviceType == Device.SYNTHESIZER) {
        transposerModel.setSynthesizer(null);
        transposerModel.addStatus("Synthesizer removed");
      } else if (deviceType == Device.TRANSMITTER) {
        transposerModel.setTransmitter(null);
        transposerModel.addStatus("Transmitter removed");
      }
      return;
    }

    MidiDevice transmitter = null;
    Synthesizer synthesizer = null;

    MidiDevice device;
    MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

    //Find device with matching name
    for (MidiDevice.Info info : infos) {
      try {
        device = MidiSystem.getMidiDevice(info);

        if (device.getDeviceInfo().getName().equals(name)) {
          if (deviceType == Device.SYNTHESIZER) {
            synthesizer = (Synthesizer) device;
          } else if (deviceType == Device.TRANSMITTER) {
            transmitter = device;
          }
        }
      } catch (MidiUnavailableException e) {
        transposerModel.addStatus("Cannot open device " + name);
      }
    }

    //Update relevant model fields
    if (synthesizer != null) {
      transposerModel.setSynthesizer(synthesizer);
      transposerModel.addStatus("Synthesizer Updated to " + name);
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
