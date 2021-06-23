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
import javax.sound.midi.Sequencer;
import javax.swing.JTextArea;

public class transposerModel implements Model{

  private int inputMode, outputMode;
  private Note inputRoot, outputRoot;
  private String inputFile, outputFile;
  private Map<Note,Note> customIntervals;
  private boolean useCustomIntervals;
  private StatusBox statusBox;

  private final List<Updatable> views;

  public transposerModel() {
    this.views = new ArrayList<>();
    this.inputMode = 0;
    this.inputRoot = new Note(0);
    this.outputMode = 0;
    this.outputRoot = new Note(0);
    this.customIntervals = new HashMap<>();
    this.useCustomIntervals = false;

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
    for (Updatable u: views) {
      u.update(this);
    }
  }

  /* Creates a new transpose track from current fields */
  private TransposeTrack getTransposeTrack() {
    if (useCustomIntervals) {
      try {
        return new TransposeTrack(inputRoot, outputRoot, new Transposer(customIntervals));
      } catch (TranspositionException e) {
        addStatus(e.getMessage());
        addStatus("Transposing with selected mode sliders instead");
      }
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
      addStatus("Invalid FIle Path: " + path);
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
  public void transposeAndPlay(Sequencer sequencer) {
    TransposeTrack tt = getTransposeTrack();

    File input;
    input = loadFile(inputFile, "Input file not selected");

    if (input == null) {
      return;
    }

    try {
      tt.transposeAndPlay(input, sequencer);
      addStatus("Playing transposed Midi from " + inputFile);
    } catch (TranspositionException e) {
      addStatus(e.getMessage());
    }
  }

  @Override
  public  void stop(Sequencer sequencer) {
    TransposeTrack tt = getTransposeTrack();

    try {
      tt.stop(sequencer);
      addStatus("Stopped sequencer output");
    } catch (TranspositionException e) {
      addStatus(e.getMessage());
    }
  }

  @Override
  public void step(Sequencer sequencer, long microseconds) {
    TransposeTrack tt = getTransposeTrack();
    tt.step(sequencer, microseconds);
  }

  @Override
  public void changeSpeed(Sequencer sequencer, boolean forwards) {
    TransposeTrack tt = getTransposeTrack();
    tt.changeSpeed(sequencer, forwards);
  }

  @Override
  public void setInputFile(String path) {
    inputFile = path;
    updateObservers();
  }

  @Override
  public void setOutputFile(String path) {
    outputFile = path;
    updateObservers();
  }

  @Override
  public void setInputMode(int inputMode) {
    this.inputMode = inputMode;
    updateObservers();
  }

  @Override
  public void setOutputMode(int outputMode) {
    this.outputMode = outputMode;
    updateObservers();
  }

  /* Root setters can update the status box if an invalid root is given */

  @Override
  public void setInputRoot(Note inputRoot) {
    if (inputRoot == null) {
      statusBox.addStatus("Invalid note entered, cannot change input root");
    } else {
      this.inputRoot = inputRoot;
      statusBox.addStatus("Input root updated to " + inputRoot);
    }
    updateObservers();
  }

  @Override
  public void setOutputRoot(Note outputRoot) {
    if (outputRoot == null) {
      statusBox.addStatus("Invalid note entered, cannot change output root");
    } else {
      this.outputRoot = outputRoot;
      statusBox.addStatus("Output root updated to " + outputRoot);
    }
    updateObservers();
  }

  @Override
  public void addCustomInterval(Note src, Note dst) {
    customIntervals.remove(src);
    customIntervals.put(src, dst);
  }

  @Override
  public void addStatus(String status) {
    statusBox.addStatus(status);
  }

  @Override
  public int getInputMode() {
    return inputMode;
  }

  @Override
  public int getOutputMode() {
    return outputMode;
  }

  @Override
  public Note getInputRoot() {
    return inputRoot;
  }

  @Override
  public Note getOutputRoot() {
    return outputRoot;
  }

  @Override
  public String getInputFile() {
    return inputFile;
  }

  @Override
  public String getOutputFile() {
    return outputFile;
  }

  @Override
  public boolean isUseCustomIntervals() {
    return useCustomIntervals;
  }

  @Override
  public void setUseCustomIntervals(boolean useCustomIntervals) {
    this.useCustomIntervals = useCustomIntervals;
  }

  @Override
  public JTextArea getTextField() {
    return statusBox.getTextField();
  }
}
