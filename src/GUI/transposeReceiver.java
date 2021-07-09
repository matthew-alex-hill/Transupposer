package GUI;

import static javax.sound.midi.ShortMessage.NOTE_OFF;
import static javax.sound.midi.ShortMessage.NOTE_ON;

import Transposition.TransposeTrack;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class transposeReceiver implements Receiver {

  private TransposeTrack tt;
  private Receiver receiver;
  private final Model model;

  public transposeReceiver(Model model) {

    this.model = model;
  }

  public void setTt(TransposeTrack tt) {
    this.tt = tt;
  }

  public void setReceiver(Receiver receiver) {
    this.receiver = receiver;
  }

  @Override
  public void send(MidiMessage midiMessage, long l) {
    int messageType = midiMessage.getStatus() & TransposeTrack.messageMask;
    ShortMessage shortMessage;

    //model.addStatus("Sending message " + midiMessage.toString());

    if (messageType == NOTE_ON || messageType == NOTE_OFF) {
      shortMessage = (ShortMessage) midiMessage;

      try {
        if (receiver != null && tt != null) {
          receiver.send(new ShortMessage(shortMessage.getStatus(),
              tt.getTransposer().transpose(shortMessage.getData1()),
              shortMessage.getData2()), l);
        }
        //model.addStatus("Message sent: " + shortMessage.toString());
      } catch (InvalidMidiDataException e) {
        //model.addStatus("Error Sending Message " + shortMessage.toString() + ": " + e.getMessage());
      }
    } else {
      //model.addStatus("Message sent: " + midiMessage.toString());
      if (receiver != null) {
        receiver.send(midiMessage, l);
      }
    }
  }

  @Override
  public void close() {
    receiver.close();
  }
}
