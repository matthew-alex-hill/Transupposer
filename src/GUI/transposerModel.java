package GUI;

import Transposition.Note;
import Transposition.TransposeTrack;
import Transposition.Transposer;
import Transposition.TranspositionException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Transmitter;
import javax.swing.JTextArea;

public class transposerModel implements Model {

  private final Map<Note, Note> customIntervals;
  private final StatusBox statusBox;
  private final List<Updatable> views;
  private int inputMode, outputMode;
  private Note inputRoot, outputRoot;
  private String inputFile, outputFile;

  private Sequencer sequencer = null;
  private Transmitter transmitter= null;
  private boolean useCustomDiatonic;
  private boolean useCustomChromatic;
  private boolean useAutoUpdate;
  private boolean useFileOutput;
  private long microsecondPosition;

  public transposerModel() {
    this.views = new ArrayList<>();
    this.inputMode = 0;
    this.inputRoot = new Note(0);
    this.outputMode = 0;
    this.outputRoot = new Note(0);
    this.customIntervals = new HashMap<>();
    this.useCustomDiatonic = false;
    this.useCustomChromatic = false;
    this.useAutoUpdate = false;
    this.useFileOutput = true;

    this.inputFile = null;
    this.outputFile = null;
    this.statusBox = new StatusBox();
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

  /* Creates a new transpose track from current fields */
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

    if (file == null) {
      addStatus("Invalid File Path: " + path);
    }

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
      tt.transposeToFile(input, output);
      addStatus("Transposed file created at " + outputFile);
    } catch (TranspositionException e) {
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

    try {
      tt.transposeAndPlay(input, sequencer);
      if (microsecondPosition < sequencer.getSequence().getMicrosecondLength()) {
        sequencer.setMicrosecondPosition(microsecondPosition);
      }
      addStatus("Playing transposed Midi from " + inputFile);
    } catch (TranspositionException e) {
      addStatus(e.getMessage());
    }
  }

  @Override
  public void stop() {
    TransposeTrack tt = getTransposeTrack();

    try {
      tt.stop(sequencer);
      addStatus("Stopped sequencer output");
      microsecondPosition = 0;
    } catch (TranspositionException e) {
      addStatus(e.getMessage());
    }
  }

  @Override
  public void pause() {
    TransposeTrack tt = getTransposeTrack();

    try {
      tt.pause(sequencer);
    } catch (TranspositionException e) {
      addStatus(e.getMessage());
    }

    microsecondPosition = tt.getMicrosecondPosition();
  }

  @Override
  public void changeTransposer() {
    pause();
    transposeAndPlay();
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
  public void setSequencer(Sequencer sequencer) {
    if (this.sequencer != null && this.sequencer.isOpen()) {
      this.sequencer.close();
    }

    this.sequencer = sequencer;
  }

  @Override
  public void setTransmitter(Transmitter transmitter) {
    if (this.transmitter != null) {
      transmitter.close();
    }

    this.transmitter = transmitter;
    try {
      this.transmitter.setReceiver(sequencer.getReceiver());
    } catch (MidiUnavailableException e) {
      addStatus("Error trying to get receiver: " + e.getMessage());
    }
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
  public JTextArea getTextField() {
    return statusBox.getTextField();
  }
}
