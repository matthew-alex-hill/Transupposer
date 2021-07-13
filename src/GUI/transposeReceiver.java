package GUI;

import static javax.sound.midi.ShortMessage.NOTE_OFF;
import static javax.sound.midi.ShortMessage.NOTE_ON;

import Transposition.TransposeTrack;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class transposeReceiver implements Receiver {

  private TransposeTrack tt;
  private Receiver receiver;
  private final Model model;
  private Sequence recordedSequence;

  /*
  private boolean recording;
  private Track track;
  */

  public transposeReceiver(Model model) {
    this.tt = null;
    this.receiver = null;
    this.model = model;
    resetSequence();
  }

  public void setTt(TransposeTrack tt) {
    this.tt = tt;
  }

  public void setReceiver(Receiver receiver) {
    this.receiver = receiver;
  }

  public void resetSequence() {
    this.recordedSequence = null;
    try {
      this.recordedSequence = new Sequence(Sequence.PPQ, 10, 1);
    } catch (InvalidMidiDataException e) {
      model.addStatus("Error creating recording sequence: " + e.getMessage());
      model.addStatus("Midi recording will not work");
    }
  }

  //Adds a new track to the sequence for each track of the recorded notes sequence
  public void addRecordedNotes(Sequence sequence) {
    Track tmp;

    for (Track track : recordedSequence.getTracks()) {
        tmp = sequence.createTrack();
      for (int i = 0; i < track.size(); i++) {
        tmp.add(track.get(i));
      }
    }
  }

  @Override
  public void send(MidiMessage midiMessage, long l) {
    int messageType = midiMessage.getStatus() & TransposeTrack.messageMask;
    ShortMessage shortMessage;

    //model.addStatus("Sending message " + midiMessage.toString() + " at " + model.getSequencer().getTickPosition());

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

    //add raw midi message to the recorded sequence
    if (recordedSequence != null) {
      //TODO: these events are not set up properly
      recordedSequence.getTracks()[0].add(new MidiEvent(midiMessage, model.getSequencer().getTickPosition()));
    }
  }

  @Override
  public void close() {
    receiver.close();
  }
}
