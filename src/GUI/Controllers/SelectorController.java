package GUI.Controllers;

import GUI.Model;
import java.awt.event.ActionEvent;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
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

    if (name.equals("None")) {
      if (deviceType == Device.SYNTHESIZER) {
        transposerModel.setSynthesizer(null);
        transposerModel.addStatus("Synthesizer removed");
      } else if (deviceType == Device.TRANSMITTER) {
        transposerModel.setTransmitter(null);
        transposerModel.addStatus("Transmitter removed");
      }
      return;
    }

    Transmitter transmitter = null;
    Synthesizer synthesizer = null;

    MidiDevice device;
    MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
    for (int i = 0; i < infos.length; i++) {
      try {
        device = MidiSystem.getMidiDevice(infos[i]);

        if (device.getDeviceInfo().getName().equals(name)) {
          if (deviceType == Device.SYNTHESIZER) {
            synthesizer = (Synthesizer) device;
          } else if (deviceType  == Device.TRANSMITTER) {
            transmitter = device.getTransmitter();
            if (!device.isOpen()) {
              device.open();
            }
          }
        }
      } catch (MidiUnavailableException e) {
        transposerModel.addStatus("Cannot locate device " + name);
      }
    }

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
