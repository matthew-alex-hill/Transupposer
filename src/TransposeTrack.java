import java.io.File;
import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;


/* Contains information about input and output key and a Transposer object to intereact with
*  Public methods let user transpose all notes in a midi sequence to file or to audio or print the transposer map */
public class TransposeTrack {


  private static int messageMask = 240;
  private TransposeMap transposer;

  /* One of the Greek modes, a value from 0 to 6 */
  private int inputMode;
  private int outputMode;

  /* Root notes of the keys as a Note object for printing purposes */
  private Note inputRoot;
  private Note outputRoot;

  public TransposeTrack(Note inputRoot, int inputMode,
      Note outputRoot, int outputMode) {
    this.inputRoot = inputRoot;
    this.inputMode = inputMode % 7;
    this.outputRoot = outputRoot;
    this.outputMode = outputMode % 7;

    this.transposer = new Transposer();
    setUpTransposer();

  }

  public TransposeTrack(Note inputRoot, int inputMode,
      Note outputRoot, int outputMode, TransposeMap transposer) {
    this.inputRoot = inputRoot;
    this.inputMode = inputMode % 7;
    this.outputRoot = outputRoot;
    this.outputMode = outputMode % 7;

    this.transposer = transposer;
    setUpTransposer();
  }

  /* Initialises a transposer by populating its notes and octaves maps */
  private void setUpTransposer() {
    int interval = outputRoot.getNoteNumber() - inputRoot.getNoteNumber();

    // Number of steps around keys needed to get root note of corresponding mode
    int steps = outputMode - inputMode;

    if (steps < 0) {
      steps += 7;
    }

    if (steps != 0) {
      // Root note of key with same notes as inputMode but same mode as outputMode
      int workingRoot = inputRoot.getNoteNumber();

      for (int i = 1; i <= steps; i++) {
        workingRoot = (workingRoot + Note.getScaleNote(inputMode + i)) % 12;
      }

      /* Gap to increase / decrease each diatonic note by
       *  Gap between root notes of each scale - Gap between root note of modes */
      int transposeGap = interval - (workingRoot - inputRoot.getNoteNumber());

      int workingNote = inputRoot.getNoteNumber();
      /* For notes 2 - 7 of the diatonic scale put the new note in the map
       *  workingRoot used to keep track of current note
       *  workingRoot is incremented by one diatonic step each loop */
      for (int i = 1; i < 7; i++) {
        workingRoot = (workingRoot + Note.getScaleNote(outputMode + i - 1));
        workingNote = (workingNote + Note.getScaleNote(inputMode + i - 1)) % 12;

        transposer.addInterval(new Note(workingNote), new Note(workingRoot + transposeGap));

        addOctaveIfNeeded(workingRoot + transposeGap, workingNote, interval);
      }
    }

    /* Map chromatic notes and note 1 to same note with new root */
    for (int i = 0; i < 12; i++) {
      transposer.addIntervalIfAbsent(new Note(i), new Note(i + interval));

      addOctaveIfNeeded(i + interval, i, interval);
    }
  }

  /* Adds an octave to a note if it wraps round over the circle of fifths back to C*/
  private void addOctaveIfNeeded(int noteValue, int mapKey, int interval) {
    if (interval > 0 && noteValue % 12 < mapKey % 12) {
      transposer.addOctaveChangeIfAbsent(new Note(mapKey), 1);
    }
    if (interval < 0 && noteValue % 12 > mapKey % 12) {
      transposer.addOctaveChangeIfAbsent(new Note(mapKey), -1);
    }
  }

  /* Takes midi sequence from a inputFile
     Transposes all note on / note off events
     Writes transposed notes into outputFile
     Returns 0 if successful, -1 if an error occured */
  public int transposeToFile(File inputFile, File outputFile) {
    Sequence outputSequence = transposeFile(inputFile);

    if (outputSequence != null) {
      try {
        MidiSystem.write(outputSequence, 1, outputFile);
      } catch (IOException e) {
        e.printStackTrace();
        System.out.println("A Midi Error Occured Writing to this file");
        return -1;
      }
    } else {
      return -1;
    }

    return 0;
  }

  /* Takes in a file and generates a transposed sequence from it
  *  Returns the sequence or null if an error occurs
  *  Errors include invalid midi messages in a file, invalid file type or IO exceptions */
  private Sequence transposeFile(File inputFile) {
    try {
      Sequence inputSequence = MidiSystem.getSequence(inputFile);
      Track[] tracks = inputSequence.getTracks();

      Sequence outputSequence = new Sequence(inputSequence.getDivisionType(),
          inputSequence.getResolution(),
          tracks.length);

      if (transposeAll(tracks, outputSequence) != 0) {
        System.out.println("A Midi Message with invalid status was found in the input file");
        return null;
      }

      return outputSequence;

    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
      System.out.println("Error reading midi file");
      return null;

    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  //TODO: add playing to sequencer
  /*
  public int transposeAndPlay(File inputFile) {
    Sequence outputSequence = transposeFile(inputFile);

    if (outputSequence != null) {

    } else {
      return -1;
    }

    return 0;
  }
   */

  /* Transposes all notes in a list of tracks and adds them to a sequence
   *  Returns 0 if successful, -1 if an error occured */
  private int transposeAll(Track[] tracks, Sequence outputSequence) {
    Track tmpTrack;
    MidiEvent tmpEvent;
    MidiMessage tmpMessage;
    ShortMessage shortMessage;
    int messageType;

    //for each event in each track add either the event or a new event with transposed frequency
    for (Track track : tracks) {
      tmpTrack = outputSequence.createTrack();
      for (int i = 0; i < track.size(); i++) {
        tmpEvent = track.get(i);
        tmpMessage = tmpEvent.getMessage();

        messageType = tmpMessage.getStatus() & messageMask;

        //If event is a note on/off message modify the note frequency, else add the event as is
        if (messageType == ShortMessage.NOTE_OFF || messageType == ShortMessage.NOTE_ON) {

          shortMessage = (ShortMessage) tmpMessage;

          try {
            tmpTrack.add(new MidiEvent(
                new ShortMessage(tmpMessage.getStatus(),
                    transposer.transpose(shortMessage.getData1()),
                    shortMessage.getData2()),
                tmpEvent.getTick()));
          } catch (InvalidMidiDataException e) {
            e.printStackTrace();
            System.out.println("Invalid Note: " + tmpMessage.getStatus() + ", " +
                transposer.transpose(shortMessage.getData1()) + ", " +
                shortMessage.getData2());
            return -1;
          }
        } else {
          tmpTrack.add(tmpEvent);
        }
      }
    }

    return 0;
  }

  public void showMap() {
    System.out.println(transposer);
  }

}
