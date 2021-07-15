package GUI;

import Transposition.Note;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.swing.JTextArea;

public interface Model {

  /* Updates all views available to the model */
  void updateObservers();

  /* Runs the transposer creating a new Transposition.Transposer track for the input and output file and key set up */
  void transposeToFile();

  /* Runs transposer and plays resulting midi to supplied sequencer */
  void transposeAndPlay();

  //Creates a new list of transpose intervals and time stamps and plays midi file to record changes into list
  void record();

  /* Stops the provided sequencer from sending midi messages */
  void stop();

  /* Pauses playback of midi sequence */
  void pause();

  /* Updates transposer to current settings and resumes playback */
  void changeTransposer();

  /* Steps the sequencer playback by the given number of microseconds */
  void step(long microseconds);

  /* Doubles or halves playback speed of sequencer playback */
  void changeSpeed(boolean forwards);

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

  int getLiveMode();

  void setLiveMode(int liveMode);

  /* Getters for roots */
  Note getInputRoot();

  Note getOutputRoot();

  Note getLiveRoot();

  void setLiveRoot(Note liveRoot);

  /* Getters for file paths */
  String getInputFile();

  String getOutputFile();

  void setSynthesizer(Synthesizer synthesizer);

  void setTransmitter(MidiDevice transmitter);

  void setChannel(int channel);

  boolean isUseChannel();

  void setUseChannel(boolean useChannel);

  Sequencer getSequencer();

  Note getCustomNote(Note src);

  boolean containsCustomNote(Note src);

  boolean isUseCustomDiatonic();

  void setUseCustomDiatonic(boolean useCustomDiatonic);

  boolean isUseCustomChromatic();

  void setUseCustomChromatic(boolean useCustomChromatic);

  boolean isUseAutoUpdate();

  void setUseAutoUpdate(boolean useAutoUpdate);

  boolean isUseFileOutput();

  void setUseFileOutput(boolean useFileOutput);

  boolean isRecording();

  boolean isUseLiveScale();

  void setUseLiveScale(boolean useLiveScale);

  /* Gets the status text field for the model */
  JTextArea getTextField();
}
