package Transposition;

/* Interface for a map which can be used by a Transposition.TransposeTrack */
public interface TransposeMap {

  /* Puts a pair of two notes into the intervals map */
  void addInterval(Note src, Note dst);

  /* Puts a pair of two notes into the intervals map if the key is not present*/
  void addIntervalIfAbsent(Note src, Note dst);

  /* Puts a pair of two notes into the octave changes map */
  void addOctaveChange(Note src, int dst);

  /* Puts a pair of two notes into the octave changes map if the key is not present*/
  void addOctaveChangeIfAbsent(Note src, int dst);

  /* Creates a new Transposition.Pitch from a Midi Frequency
   *  Octaves range from 0 to 10 instead of -1 to 9 so are offset by 1 */
  Pitch pitchFromFrequency(int frequency);

  /* Gets the Midi frequency of a given Transposition.Pitch */
  int frequencyFromPitch(Pitch pitch);

  /* Gets the transposed frequency of a Midi Frequency
   *  Transposing done via the intervals map */
  int transpose(int frequency);
}
