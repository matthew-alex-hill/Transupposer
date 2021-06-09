package GUI;

import Transposition.Note;
import javax.swing.JTextArea;

public interface Model {

  /* Runs the transposer creating a new Transposition.Transposer track for the input and output file and key set up */
  void runTransposer();

  /* Sets the input file path to path */
  void setInputFile(String path);

  /* Sets the output file path to path */
  void setOutputFile(String path);

  /* Sets the input mode to the given value */
  void setInputMode(int inputMode);

  /* Sets the output mode to the given value */
  void setOutputMode(int outputMode);

  /* Sets the input root note to the given value */
  void setInputRoot(Note inputRoot);

  /* Sets the output root note to the given value */
  void setOutputRoot(Note outputRoot);

  /* Getters for modes */
  int getInputMode();

  int getOutputMode();

  /* Getters for roots */
  Note getInputRoot();

  Note getOutputRoot();

  /* Getters for file paths */
  String getInputFile();

  String getOutputFile();

  /* Gets the status text field for the model */
  JTextArea getTextField();
}
