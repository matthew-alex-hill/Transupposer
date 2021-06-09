package GUI;

import Transposition.Note;
import Transposition.TransposeTrack;
import Transposition.TranspositionException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;

public class transposerModel implements Model{

  private int inputMode, outputMode;
  private Note inputRoot, outputRoot;
  private String inputFile, outputFile;
  private StatusBox statusBox;

  private final List<Updatable> views;

  public transposerModel() {
    this.views = new ArrayList<>();
    this.inputMode = 0;
    this.inputRoot = new Note(0);
    this.outputMode = 0;
    this.outputRoot = new Note(0);

    this.inputFile = null;
    this.outputFile = null;
    this.statusBox = new StatusBox();
  }

  /* Adds a view to the views list */
  public void addObserver(Updatable observer) {
    this.views.add(observer);
  }

  /* Updates all views available to the model */
  private void updateObservers() {
    for (Updatable u: views) {
      u.update(this);
    }
  }

  @Override
  public void runTransposer() {
    File input, output;
    if (inputFile == null) {
      statusBox.addStatus("Input file not selected");
      return;
    }

    if (outputFile == null) {
      statusBox.addStatus("Output file not selected");
      return;
    }

    input = new File(inputFile);
    output = new File(outputFile);

    TransposeTrack tt = new TransposeTrack(inputRoot, inputMode, outputRoot, outputMode);

    try {
      tt.transposeToFile(input, output);
      statusBox.addStatus("Transposed file created at " + outputFile);
    } catch (TranspositionException e) {
      statusBox.addStatus(e.getMessage());
    }
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
  }

  @Override
  public void setOutputMode(int outputMode) {
    this.outputMode = outputMode;
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
  }

  @Override
  public void setOutputRoot(Note outputRoot) {
    if (outputRoot == null) {
      statusBox.addStatus("Invalid note entered, cannot change output root");
    } else {
      this.outputRoot = outputRoot;
      statusBox.addStatus("Output root updated to " + outputRoot);
    }
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
  public JTextArea getTextField() {
    return statusBox.getTextField();
  }
}
