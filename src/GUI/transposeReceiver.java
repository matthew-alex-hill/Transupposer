package GUI;

import static javax.sound.midi.ShortMessage.NOTE_OFF;
import static javax.sound.midi.ShortMessage.NOTE_ON;

import Transposition.TransposeTrack;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class transposeReceiver implements Receiver {

  private final TransposeTrack tt;
  private final Receiver receiver;
  private final Model model;

  public transposeReceiver(Model model, TransposeTrack tt, Receiver receiver) {
    this.tt = tt;
    this.receiver = receiver;
    this.model = model;
  }

  @Override
  public void send(MidiMessage midiMessage, long l) {
    int messageType = midiMessage.getStatus() & TransposeTrack.messageMask;
    ShortMessage shortMessage;

    if (messageType == NOTE_ON || messageType == NOTE_OFF) {
      shortMessage = (ShortMessage) midiMessage;

      try {
        receiver.send(new ShortMessage(shortMessage.getStatus(),
            tt.getTransposer().transpose(shortMessage.getData1()),
            shortMessage.getData2()), l);
        model.addStatus("Message sent: " + shortMessage.toString());
      } catch (InvalidMidiDataException e) {
        model.addStatus("Error Sending Message " + shortMessage.toString() + ": " + e.getMessage());
      }
    } else {
      model.addStatus("Message sent: " + midiMessage.toString());
      receiver.send(midiMessage, l);
    }
  }

  @Override
  public void close() {
    receiver.close();
  }
}
