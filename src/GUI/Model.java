package GUI;

import Transposition.Note;
import javax.sound.midi.Sequencer;
import javax.swing.JTextArea;

public interface Model {

  /* Updates all views available to the model */
  void updateObservers();

  /* Runs the transposer creating a new Transposition.Transposer track for the input and output file and key set up */
  void transposeToFile();

  /* Runs transposer and plays resulting midi to supplied sequencer */
  void transposeAndPlay(Sequencer sequencer);

  /* Stops the provided sequencer from sending midi messages */
  void stop(Sequencer sequencer);

  /* Steps the sequencer playback by the given number of microseconds */
  void step(Sequencer sequencer, long microseconds);

  /* Doubles or halves playback speed of sequencer playback */
  void changeSpeed(Sequencer sequencer, boolean forwards);

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

  /* Adds note interval to customised scale or replaces existing interval with same base note */
  void addCustomInterval(Note src, Note dst);

  /* Adds a status message to the status box */
  void addStatus(String status);

  /* Getters for modes */
  int getInputMode();

  int getOutputMode();

  /* Getters for roots */
  Note getInputRoot();

  Note getOutputRoot();

  /* Getters for file paths */
  String getInputFile();

  String getOutputFile();

  boolean isUseCustomDiatonic();

  void setUseCustomDiatonic(boolean useCustomDiatonic);

  boolean isUseCustomChromatic();

  void setUseCustomChromatic(boolean useCustomChromatic);

  /* Gets the status text field for the model */
  JTextArea getTextField();
}
