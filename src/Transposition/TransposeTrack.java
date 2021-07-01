package Transposition;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;


/* Contains information about input and output key and a Transposition.Transposer object to intereact with
 *  Public methods let user transpose all notes in a midi sequence to file or to audio or print the transposer map */
public class TransposeTrack {


  private static final int messageMask = 240;
  private static final int drumChannel = 9;
  private static final int percussiveMin = 113;

  private TransposeMap transposer;

  /* One of the Greek modes, a value from 0 to 6 */
  private int inputMode;
  private int outputMode;

  /* Root notes of the keys as a Transposition.Note object for printing purposes */
  private Note inputRoot;
  private Note outputRoot;

  private long tickPosition = 0;

  //Creates new transpose map with roots and modes
  public TransposeTrack(Note inputRoot, int inputMode,
      Note outputRoot, int outputMode) {
    makeTransposeTrack(inputRoot, inputMode, outputRoot, outputMode, new Transposer());

  }

  //Loads data into existing transpose map
  public TransposeTrack(Note inputRoot, int inputMode,
      Note outputRoot, int outputMode, TransposeMap transposer) {
    makeTransposeTrack(inputRoot, inputMode, outputRoot, outputMode, transposer);
  }

  //Uses an existig transpose map to generate only an octaves map
  public TransposeTrack(Note inputRoot, Note outputRoot, TransposeMap transposer)
      throws TranspositionException {
    this.inputRoot = inputRoot;
    this.outputRoot = outputRoot;
    this.transposer = transposer;

    for (int i = 0; i < 12; i++) {
      Note tmp = new Note(i);
      if (!transposer.containsInterval(tmp)) {
        throw new TranspositionException("Note " + tmp + " not found in transpose map");
      }
      transposer.addOctaveIfNeeded(transposeNote(tmp).getNoteNumber(), i,
          outputRoot.getNoteNumber() - inputRoot.getNoteNumber());
    }
  }

  private void makeTransposeTrack(Note inputRoot, int inputMode, Note outputRoot, int outputMode,
      TransposeMap transposer) {
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

        transposer.addIntervalIfAbsent(new Note(workingNote), new Note(workingRoot + transposeGap));

        transposer
            .addOctaveIfNeeded(transposer.getInterval(workingNote).getNoteNumber(), workingNote,
                interval);
      }
    }

    /* Map chromatic notes and note 1 to same note with new root */
    for (int i = 0; i < 12; i++) {
      if (!transposer.containsInterval(new Note(i))) {
        transposer.addIntervalIfAbsent(new Note(i), new Note(i + interval));
        transposer.addOctaveIfNeeded(transposer.getInterval(i).getNoteNumber(), i, interval);
      }
    }
  }

  public TransposeMap getTransposer() {
    return transposer;
  }

  /* Transposes a single note and returns the result */
  public Note transposeNote(Note note) {
    return new Note(transposer.transpose(note.getNoteNumber()));
  }

  /* Takes midi sequence from an inputFile
     Transposes all note on / note off events
     Writes transposed notes into outputFile
     Throws a transposition exception in the case of any errors */
  public void transposeToFile(File inputFile, File outputFile) throws TranspositionException {
    Sequence outputSequence = transposeFile(inputFile);

    try {
      MidiSystem.write(outputSequence, 1, outputFile);
    } catch (IOException e) {
      throw new TranspositionException("Midi Write Error: " + e.getMessage());
    }
  }

  /* Takes midi sequence from an inputFile and a list of transposers and timestamps
     Transposes all note on / note off events using latest transposer timestamp after the microsecond position
     Writes transposed notes into outputFile
     Throws a transposition exception in the case of any errors */
  public void transposeToFile(File inputFile, File outputFile, List<TransposeStamp> stamps) throws TranspositionException {
    Sequence outputSequence = transposeFile(inputFile, stamps);

    try {
      MidiSystem.write(outputSequence, 1, outputFile);
    } catch (IOException e) {
      throw new TranspositionException("Midi Write Error: " + e.getMessage());
    }
  }

  /* Takes midi sequence from an inputFile
     Transposes all note on / note off events
     Writes transposed notes into a new sequence
     Plays the sequence through a given sequencer
     Throws a transposition exception in the case of any errors */
  public void transposeAndPlay(File inputFile, Sequencer sequencer) throws TranspositionException {
    Sequence outputSequence = transposeFile(inputFile);

    try {
      sequencer.setSequence(outputSequence);
    } catch (InvalidMidiDataException e) {
      throw new TranspositionException(e.getMessage());
    }

    try {
      if (!sequencer.isOpen()) {
        System.out.println("Opening sequencer " + sequencer.getDeviceInfo().getName());
        sequencer.open();
      }
      sequencer.setLoopCount(0);
      sequencer.setTempoFactor(1);
      sequencer.start();
    } catch (MidiUnavailableException e) {
      throw new TranspositionException(e.getMessage());
    }
  }

  /* Stops a playing sequence or throws an exception if it cannot access the sequencer*/
  public void stop(Sequencer sequencer) throws TranspositionException {

    try {
      sequencer.stop();
      sequencer.close();
    } catch (IllegalStateException e) {
      throw new TranspositionException(e.getMessage());
    }
  }

  /* Stops a playing sequence and stores the current position, throws an exception if it cannot be stopped */
  public void pause(Sequencer sequencer) throws TranspositionException {
    tickPosition = sequencer.getTickPosition();
    stop(sequencer);
  }

  /* Steps midi playback by a given offset in microseconds */
  public void step(Sequencer sequencer, long microseconds) {
    sequencer.setMicrosecondPosition(sequencer.getMicrosecondPosition() + microseconds);
    if (sequencer.getMicrosecondPosition() < 0) {
      sequencer.setMicrosecondPosition(0);
    }
  }

  /* Either doubles or halves the playback tempo factor */
  public void changeSpeed(Sequencer sequencer, boolean forward) {

    double newTempoFac = Math.log(sequencer.getTempoFactor()) / Math.log(2);

    if (forward) {
      newTempoFac += 1;
    } else {
      newTempoFac -= 1;
    }

    sequencer.setTempoFactor((float) Math.pow(2, newTempoFac));
  }

  public long getTickPosition() {
    return tickPosition;
  }

  public void setTickPosition(long tickPosition) {
    this.tickPosition = tickPosition;
  }

  /* Takes in a file and generates a transposed sequence from it
   *  Returns the sequence or null if an error occurs
   *  Errors include invalid midi messages in a file, invalid file type or IO exceptions */
  private Sequence transposeFile(File inputFile) throws TranspositionException {
    List<TransposeStamp> stamps = new ArrayList<>();
    stamps.add(new TransposeStamp(transposer, 0));
    return getFileSequence(inputFile, stamps);
  }

  /* Takes in a file and a list of transposers at timestamps and generates a transposed sequence from them
   *  Returns the sequence or null if an error occurs
   *  Errors include invalid midi messages in a file, invalid file type or IO exceptions */
  private Sequence transposeFile(File inputFile, List<TransposeStamp> stamps) throws TranspositionException {
    return getFileSequence(inputFile, stamps);
  }

  private Sequence getFileSequence(File inputFile, List<TransposeStamp> stamps)
      throws TranspositionException {
    try {
      Sequence inputSequence = MidiSystem.getSequence(inputFile);
      Track[] tracks = inputSequence.getTracks();

      Sequence outputSequence = new Sequence(inputSequence.getDivisionType(),
          inputSequence.getResolution(),
          tracks.length);
      transposeAll(tracks, outputSequence, stamps);
      return outputSequence;

    } catch (InvalidMidiDataException | IOException e) {
      throw new TranspositionException(e.getMessage());
    }
  }


  /* Transposes all notes in a list of tracks and adds them to a sequence
   *  Returns 0 if successful, -1 if an error occured */
  private void transposeAll(Track[] tracks, Sequence outputSequence, List<TransposeStamp> stamps) throws TranspositionException {
    Track tmpTrack;
    MidiEvent tmpEvent;
    MidiMessage tmpMessage;
    TransposeMap tmpMap;
    ShortMessage shortMessage;
    int messageType;
    Set<Integer> drumChannels = new HashSet<>();

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

          // if channel is a drum channel add frequency as is, else transpose and add
          if (drumChannels.contains(shortMessage.getChannel())) {
            tmpTrack.add(tmpEvent);
          } else {
            //adds the transposed version of the note as a new event and fails if this cannot be created
            try {
              tmpMap = getTrackAtTime(stamps, tmpEvent.getTick());
              if (tmpMap != null) {
                tmpTrack.add(new MidiEvent(
                    new ShortMessage(tmpMessage.getStatus(),
                        tmpMap.transpose(shortMessage.getData1()),
                        shortMessage.getData2()),
                    tmpEvent.getTick()));
              } else {
                throw new TranspositionException("No transposer found for tick " + tmpEvent.getTick());
              }
            } catch (InvalidMidiDataException e) {
              e.printStackTrace();

              throw new TranspositionException(
                  "Invalid Transposition.Note: " + tmpMessage.getStatus() + ", " +
                      transposer.transpose(shortMessage.getData1()) + ", " +
                      shortMessage.getData2());
            }
          }

        } else {
          //if we are changing to a drum program, put the channel in the do not transpose list
          if (messageType == ShortMessage.PROGRAM_CHANGE) {
            shortMessage = (ShortMessage) tmpMessage;
            if (shortMessage.getData1() >= percussiveMin
                || shortMessage.getChannel() == drumChannel) {
              drumChannels.add(shortMessage.getChannel());
            } else {
              //remove from the do not transpose list if we are changing to a melodic program
              drumChannels.remove(shortMessage.getChannel());
            }
          }

          tmpTrack.add(tmpEvent);
        }
      }
    }
  }

  //Gets current transposer given a list of stamped TransposeMaps in reverse order
  private static TransposeMap getTrackAtTime(List<TransposeStamp> stamps, long tickPosition) {
    TransposeStamp stamp;
    for (int i = 0; i < stamps.size(); i++) {
      stamp = stamps.get(i);
      if (tickPosition >= stamp.getTickPosition()) {
        return stamp.getTransposer();
      }
    }
    return null;
  }
}
