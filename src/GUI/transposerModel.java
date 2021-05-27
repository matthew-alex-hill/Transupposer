package GUI;

import Transposition.Note;
import Transposition.TransposeTrack;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class transposerModel implements Model{

  private int inputMode, outputMode, inputRoot, outputRoot;
  private String inputFile, outputFile;

  private final List<Updatable> views;

  public transposerModel() {
    this.views = new ArrayList<>();
    this.inputMode = 0;
    this.inputRoot = 0;
    this.outputMode = 0;
    this.outputRoot = 0;

    this.inputFile = null;
    this.outputFile = null;
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
  public int runTransposer() {
    File input, output;
    input = new File(inputFile);
    output = new File(outputFile);

    TransposeTrack tt = new TransposeTrack(new Note(inputRoot), inputMode, new Note(outputRoot), outputMode);

    return tt.transposeToFile(input, output);
  }

  @Override
  public void setInputFile(String path) {
    inputFile = path;
  }

  @Override
  public void setOutputFile(String path) {
    outputFile = path;
  }

  @Override
  public void setInputMode(int inputMode) {
    this.inputMode = inputMode;
  }

  @Override
  public void setOutputMode(int outputMode) {
    this.outputMode = outputMode;
  }

  @Override
  public void setInputRoot(int inputRoot) {
    this.inputRoot = inputRoot;
  }

  @Override
  public void setOutputRoot(int outputRoot) {
    this.outputRoot = outputRoot;
  }
}
