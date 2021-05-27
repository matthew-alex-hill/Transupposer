package GUI;

public interface Model {

  /* Runs the transposer creating a new Transposition.Transposer track for the input and output file and key set up */
  int runTransposer();

  /* Sets the input file path to path */
  void setInputFile(String path);

  /* Sets the output file path to path */
  void setOutputFile(String path);

  /* Sets the input mode to the given value */
  void setInputMode(int inputMode);

  /* Sets the output mode to the given value */
  void setOutputMode(int outputMode);

  /* Sets the input root note to the given value */
  void setInputRoot(int inputRoot);

  /* Sets the output root note to the given value */
  void setOutputRoot(int outputRoot);
}
