package GUI;

import Transposition.Note;
import Transposition.TransposeMap;
import Transposition.TransposeStamp;
import Transposition.TransposeTrack;
import Transposition.Transposer;
import Transposition.TranspositionException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.swing.JTextArea;

public class transposerModel implements Model {

  private final Map<Note, Note> customIntervals;
  private final StatusBox statusBox;
  private final List<Updatable> views;
  private int inputMode, outputMode, liveMode;
  private Note inputRoot, outputRoot, liveRoot;
  private String inputFile, outputFile;
  private List<TransposeStamp> stamps;

  private Sequencer sequencer = null;
  private MidiDevice transmitter = null;
  private Synthesizer synthesizer = null;
  private boolean useCustomDiatonic;
  private boolean useCustomChromatic;
  private boolean useAutoUpdate;
  private boolean useFileOutput;
  private boolean useChannel;
  private boolean useLiveScale;
  private boolean recording;
  private boolean playing;
  private long tickPosition;
  private final transposeReceiver transposeReceiver;

  public transposerModel() {
    this.views = new ArrayList<>();
    this.inputMode = 0;
    this.inputRoot = new Note(0);
    this.outputMode = 0;
    this.outputRoot = new Note(0);
    this.liveMode = 0;
    this.liveRoot = new Note(0);
    this.customIntervals = new HashMap<>();
    this.useCustomDiatonic = false;
    this.useCustomChromatic = false;
    this.useChannel = false;
    this.useLiveScale = false;
    this.useAutoUpdate = false;
    this.useFileOutput = true;
    this.recording = false;
    this.playing = false;

    this.inputFile = null;
    this.outputFile = null;
    this.statusBox = new StatusBox();

    //setting up midi sequencer
    try {
      this.sequencer = MidiSystem.getSequencer();
      if (!sequencer.isOpen()) {
        sequencer.open();
      }
    } catch (MidiUnavailableException e) {
      addStatus(e.getMessage() + ", midi playback will likely not work");
    }

    //Creates new transpose receiver to intercept messages
    this.transposeReceiver = new transposeReceiver(this);
  }

  /* Adds a view to the views list */
  public void addObserver(Updatable observer) {
    this.views.add(observer);
  }

  /* Updates all views available to the model */
  @Override
  public void updateObservers() {
    for (Updatable u : views) {
      u.update(this);
    }
  }

  /* Creates a new TransposeTrack from current fields using the file input scale*/
  private TransposeTrack getTransposeTrack() {
    if (useCustomDiatonic && useCustomChromatic) {
      try {
        return new TransposeTrack(inputRoot, outputRoot, new Transposer(customIntervals));
      } catch (TranspositionException e) {
        addStatus(e.getMessage());
        addStatus("Transposing may have unexpected results");
      }
    }

    if (useCustomDiatonic || useCustomChromatic) {
      //Uses constructor which sets notes not present in map automatically
      return new TransposeTrack(inputRoot, inputMode, outputRoot, outputMode,
          new Transposer(customIntervals));
    }

    return new TransposeTrack(inputRoot, inputMode, outputRoot, outputMode);
  }

  /* Creates a file given a path and ads an error status if it cannot */
  private File loadFile(String path, String errorMessage) {
    File file;
    if (path == null) {
      addStatus(errorMessage);
      return null;
    }

    file = new File(path);

    return file;
  }

  @Override
  public void transposeToFile() {
    File input = loadFile(inputFile, "Input file not selected");
    if (input == null) {
      return;
    }

    File output = loadFile(outputFile, "Output file not selected");
    if (output == null) {
      return;
    }

    TransposeTrack tt = getTransposeTrack();

    try {
      if (recording) {
        Sequence sequence = MidiSystem.getSequence(input);

        //copies recorded notes in transpose receiver to sequence
        transposeReceiver.addRecordedNotes(sequence);

        tt.transposeToFile(sequence, output, stamps);
        addStatus("Saved recording to " + outputFile);
      } else {
        tt.transposeToFile(input, output);
        addStatus("Transposed file created at " + outputFile);
      }
    } catch (TranspositionException | IOException | InvalidMidiDataException e) {
      addStatus(e.getMessage());
    }
  }

  @Override
  public void transposeAndPlay() {
    TransposeTrack tt = getTransposeTrack();

    File input;
    input = loadFile(inputFile, "Input file not selected");

    if (input == null) {
      return;
    }

    if (synthesizer == null) {
      addStatus("No synthesizer selected for playback");
      return;
    }

    try {
      if (transmitter != null) {
        tt.setRecording(true);
      }

      tt.transposeAndPlay(input, sequencer);

      logTransposerChange(tt.getTransposer());

      if (tickPosition < sequencer.getSequence().getMicrosecondLength()) {
        sequencer.setTickPosition(tickPosition);
        tt.setTickPosition(tickPosition);
      }
      addStatus("Playing transposed Midi from " + inputFile);

      playing = true;
    } catch (TranspositionException e) {
      addStatus(e.getMessage());
    }
  }

  private void logTransposerChange(TransposeMap transposer) {
    if (recording) {
      addStatus("Transposer change logged at " + tickPosition);
      stamps.add(0, new TransposeStamp(transposer, tickPosition));
    }
  }

  @Override
  public void record() {
    //TODO: Add way of saving recording if song finishes without stop being pressed
    recording = true;
    stamps = new ArrayList<>();
    transposeReceiver.resetSequence();
    transposeAndPlay();
  }

  @Override
  public void stop() {
    TransposeTrack tt = getTransposeTrack();

    if (synthesizer == null) {
      addStatus("No synthesizer selected for playback");
      return;
    }

    try {
      tt.stop(sequencer);
      if (recording) {
        transposeToFile();
        recording = false;
        transposeReceiver.resetSequence();
      }
      addStatus("Stopped sequencer output");
      tickPosition = 0;
      playing = false;
    } catch (TranspositionException e) {
      addStatus(e.getMessage());
    }
  }

  @Override
  public void pause() {
    TransposeTrack tt = getTransposeTrack();

    if (synthesizer == null) {
      addStatus("No synthesizer selected for playback");
      return;
    }

    try {
      tt.pause(sequencer);
    } catch (TranspositionException e) {
      addStatus(e.getMessage());
    }

    playing = false;
    tickPosition = tt.getTickPosition();
  }

  @Override
  public void changeTransposer() {
    updateTransposeReceiver();

    //If live playback is enabled restart audio with new transposer
    if (playing) {
      pause();
      transposeAndPlay();
    }
  }

  @Override
  public void step(long microseconds) {
    TransposeTrack tt = getTransposeTrack();
    tt.step(sequencer, microseconds);
  }

  @Override
  public void changeSpeed(boolean forwards) {
    TransposeTrack tt = getTransposeTrack();
    tt.changeSpeed(sequencer, forwards);
  }

  private void clearCustomIntervals() {
    for (int i = 0; i < 12; i++) {
      customIntervals.remove(new Note(i));
    }
  }

  private void autochangeTransposer() {
    if (isUseAutoUpdate()) {
      changeTransposer();
    }
  }

  @Override
  public void addCustomInterval(Note src, Note dst) {
    customIntervals.remove(src);
    customIntervals.put(src, dst);

    autochangeTransposer();
  }

  @Override
  public void addStatus(String status) {
    statusBox.addStatus(status);
  }

  @Override
  public int getInputMode() {
    return inputMode;
  }

  /* Root setters can update the status box if an invalid root is given */

  @Override
  public void setInputMode(int inputMode) {
    this.inputMode = inputMode;
    autochangeTransposer();
    updateObservers();
  }

  @Override
  public int getOutputMode() {
    return outputMode;
  }

  @Override
  public void setOutputMode(int outputMode) {
    this.outputMode = outputMode;
    clearCustomIntervals();
    autochangeTransposer();
    updateObservers();
  }

  @Override
  public int getLiveMode() {
    return liveMode;
  }

  @Override
  public void setLiveMode(int liveMode) {
    this.liveMode = liveMode;
    updateTransposeReceiver();
  }

  @Override
  public Note getInputRoot() {
    return inputRoot;
  }

  @Override
  public void setInputRoot(Note inputRoot) {
    if (inputRoot == null) {
      statusBox.addStatus("Invalid note entered, cannot change input root");
    } else {
      this.inputRoot = inputRoot;
      statusBox.addStatus("Input root updated to " + inputRoot);
    }
    autochangeTransposer();
    updateObservers();
  }

  @Override
  public Note getOutputRoot() {
    return outputRoot;
  }

  @Override
  public void setOutputRoot(Note outputRoot) {
    if (outputRoot == null) {
      statusBox.addStatus("Invalid note entered, cannot change output root");
    } else {
      this.outputRoot = outputRoot;
      statusBox.addStatus("Output root updated to " + outputRoot);
    }
    autochangeTransposer();
    clearCustomIntervals();
    updateObservers();
  }

  @Override
  public Note getLiveRoot() {
    return liveRoot;
  }

  @Override
  public void setLiveRoot(Note liveRoot) {
    if (liveRoot == null) {
      statusBox.addStatus("Invalid note entered, cannot change live root");
    } else {
      this.liveRoot = liveRoot;
      statusBox.addStatus("Live root updated to " + liveRoot);
    }

    updateTransposeReceiver();
    updateObservers();
  }

  @Override
  public String getInputFile() {
    return inputFile;
  }

  @Override
  public void setInputFile(String path) {
    inputFile = path;
    updateObservers();
  }

  @Override
  public String getOutputFile() {
    return outputFile;
  }

  @Override
  public void setOutputFile(String path) {
    outputFile = path;
    updateObservers();
  }

  @Override
  public void setSynthesizer(Synthesizer synthesizer) {
    /* TODO: Closing synth here causes a runtime exception next time it is used
    if (this.synthesizer != null && this.synthesizer.isOpen()) {
      this.synthesizer.close();
    }
    */

    this.synthesizer = synthesizer;

    //Opens synth and sets relevant receivers to new synth
    try {
      if (synthesizer != null && !synthesizer.isOpen()) {
        synthesizer.open();
        this.sequencer.getTransmitter().setReceiver(synthesizer.getReceiver());
        updateTransposeReceiver();
        transposeReceiver.setReceiver(synthesizer.getReceiver());
      } else if (this.transmitter != null) {
        transmitter.getTransmitter().setReceiver(null);
      }
    } catch (MidiUnavailableException e) {
      addStatus("Error updating synthesizer: " + e.getMessage());
    }


  }

  @Override
  public void setTransmitter(MidiDevice transmitter) {
    /*
    if (this.transmitter != null) {
      this.transmitter.close();
    }
    */

    this.transmitter = transmitter;

    try {
      //Establishes a new transposed receiver which transposes note on/off messages and sends all others
      if (this.transmitter != null) {
        if (!this.transmitter.isOpen()) {
          this.transmitter.open();
        }
        updateTransposeReceiver();
        this.transmitter.getTransmitter().setReceiver(transposeReceiver);
      }
    } catch (MidiUnavailableException e) {
      addStatus("Error updating transmitter: " + e.getMessage());
    }
  }

  private void updateTransposeReceiver() {
    //Sets base transposer to go from live to input and output transposer from input to output
    if (useLiveScale) {
      transposeReceiver.setBaseTransposer(new TransposeTrack(liveRoot, liveMode, inputRoot, inputMode));
    } else {
      transposeReceiver.setBaseTransposer(null);
    }

    transposeReceiver.setOutputTransposer(getTransposeTrack());
  }

  @Override
  public void setChannel(int channel) {
    if (useChannel) {
      transposeReceiver.setChannel(channel);
    }
  }

  @Override
  public boolean isUseChannel() {
    return useChannel;
  }

  @Override
  public void setUseChannel(boolean useChannel) {
    this.useChannel = useChannel;
    if (!useChannel) {
      transposeReceiver.useDeafaultChannel();
    }
  }

  @Override
  public Sequencer getSequencer() {
    return sequencer;
  }

  @Override
  public Note getCustomNote(Note src) {
    return customIntervals.get(src);
  }

  @Override
  public boolean containsCustomNote(Note src) {
    return customIntervals.containsKey(src);
  }

  @Override
  public boolean isUseCustomDiatonic() {
    return useCustomDiatonic;
  }

  @Override
  public void setUseCustomDiatonic(boolean useCustomDiatonic) {
    this.useCustomDiatonic = useCustomDiatonic;
  }

  @Override
  public boolean isUseCustomChromatic() {
    return useCustomChromatic;
  }

  @Override
  public void setUseCustomChromatic(boolean useCustomChromatic) {
    this.useCustomChromatic = useCustomChromatic;
  }

  @Override
  public boolean isUseAutoUpdate() {
    return useAutoUpdate;
  }

  @Override
  public void setUseAutoUpdate(boolean useAutoUpdate) {
    this.useAutoUpdate = useAutoUpdate;
  }

  @Override
  public boolean isUseFileOutput() {
    return useFileOutput;
  }

  @Override
  public void setUseFileOutput(boolean useFileOutput) {
    this.useFileOutput = useFileOutput;
  }

  @Override
  public boolean isRecording() {
    return recording;
  }

  @Override
  public boolean isUseLiveScale() {
    return useLiveScale;
  }

  @Override
  public void setUseLiveScale(boolean useLiveScale) {
    this.useLiveScale = useLiveScale;
    updateTransposeReceiver();
  }

  @Override
  public JTextArea getTextField() {
    return statusBox.getTextField();
  }
}
