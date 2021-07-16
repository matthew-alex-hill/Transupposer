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

  private TransposeTrack outputTransposer;
  private TransposeTrack baseTransposer;
  //TODO: Add second tt to go from live root to input root for the recorded sequence
  private Receiver receiver;
  private final Model model;
  private Sequence recordedSequence;

  private boolean useChannel;
  private int channel;

  public transposeReceiver(Model model) {
    this.outputTransposer = null;
    this.receiver = null;
    this.model = model;
    resetSequence();
  }

  public void setOutputTransposer(TransposeTrack outputTransposer) {
    this.outputTransposer = outputTransposer;
  }

  public void setBaseTransposer(TransposeTrack baseTransposer) {
    this.baseTransposer = baseTransposer;
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

  //Enable channel use and set channel to given value
  public void setChannel(int channel) {
    this.useChannel = true;
    this.channel = channel;
  }

  public void useDeafaultChannel() {
    this.useChannel = false;
  }

  @Override
  public void send(MidiMessage midiMessage, long l) {
    int messageType = midiMessage.getStatus() & TransposeTrack.messageMask;
    ShortMessage shortMessage = null;
    MidiMessage recordedMessage = midiMessage;

    if (midiMessage instanceof ShortMessage) {
      shortMessage = (ShortMessage) midiMessage;
    }

    if (messageType == NOTE_ON || messageType == NOTE_OFF) {

      shortMessage = (ShortMessage) midiMessage;
      try {
        //Try transposing the message
        if (receiver != null && outputTransposer != null) {
          ShortMessage newMessage;

          //If input scale is different to live scale transpose from live to input scale
          if (baseTransposer != null) {
            newMessage = new ShortMessage(shortMessage.getStatus(),
                baseTransposer.getTransposer().transpose(shortMessage.getData1()),
                shortMessage.getData2());
          } else {
            newMessage = shortMessage;
          }

          recordedMessage = getCorrectedMessage(newMessage);

          receiver.send(new ShortMessage(recordedMessage.getStatus(),
              outputTransposer.getTransposer().transpose(newMessage.getData1()),
              newMessage.getData2()), l);
        }
      } catch (InvalidMidiDataException e) {
        model.addStatus("Error Sending Message " + shortMessage.toString() + ": " + e.getMessage());
      }

    } else {
      if (receiver != null) {
        receiver.send(getCorrectedMessage(recordedMessage), l);
      }
    }

    //add raw midi message to the recorded sequence
    if (recordedSequence != null) {
        recordedSequence.getTracks()[0]
            .add(new MidiEvent(getCorrectedMessage(midiMessage),
                model.getSequencer().getTickPosition()));

    }
  }

  /* Corrects the channel on the midi message to the user selected channel */
  private MidiMessage getCorrectedMessage(MidiMessage midiMessage) {
    if (midiMessage instanceof ShortMessage) {
      ShortMessage shortMessage = (ShortMessage) midiMessage;
      if (useChannel) {
        try {
          return new ShortMessage(shortMessage.getCommand(),
              channel,
              shortMessage.getData1(),
              shortMessage.getData2());
        } catch (InvalidMidiDataException e) {
          model.addStatus("Unable to create message in channel " + channel + ": " + e.getMessage());
          return null;
        }
      }
    }
    return midiMessage;
  }

  @Override
  public void close() {
    receiver.close();
  }
}
