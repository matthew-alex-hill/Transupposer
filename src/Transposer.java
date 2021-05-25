import java.util.HashMap;

/* Implementation of TransposeMap which uses a Hashmap for transposed notes and octaves */
public class Transposer implements TransposeMap {
  private HashMap<Note, Note> intervals;
  private HashMap<Note, Integer> octaveChanges;

  public Transposer() {
    intervals = new HashMap<>();
    octaveChanges = new HashMap<>();
  }

  /* Puts a pair of two notes into the intervals map */
  @Override
  public void addInterval(Note src, Note dst) {
    intervals.put(src, dst);
  }

  /* Puts a pair of two notes into the intervals map if the key is not present*/
  @Override
  public void addIntervalIfAbsent(Note src, Note dst) {
    intervals.putIfAbsent(src, dst);
  }

  /* Puts a pair of two notes into the octave changes map */
  @Override
  public void addOctaveChange(Note src, int dst) {
    octaveChanges.put(src, dst);
  }

  /* Puts a pair of two notes into the octave changes map if the key is not present*/
  @Override
  public void addOctaveChangeIfAbsent(Note src, int dst) {
    octaveChanges.putIfAbsent(src, dst);
  }

  /* Creates a new Pitch from a Midi Frequency
  *  Octaves range from 0 to 10 instead of -1 to 9 so are offset by 1 */
  @Override
  public Pitch pitchFromFrequency(int frequency) {
    return new Pitch(frequency / 12, new Note(frequency));
  }

  /* Gets the Midi frequency of a given Pitch */
  @Override
  public int frequencyFromPitch(Pitch pitch) {
    return pitch.getOctave() * 12 + pitch.getNote().getNoteNumber();
  }

  /* Gets the transposed frequency of a Midi Frequency
  *  Transposing done via the intervals map */
  @Override
  public int transpose(int frequency) {
    Pitch pitch = pitchFromFrequency(frequency);

    Integer octaveChange = octaveChanges.get(pitch.getNote());

    pitch.setNote(intervals.get(pitch.getNote()));

    if (octaveChange != null) {
      pitch.setOctave(pitch.getOctave() + octaveChange);
    }

    return frequencyFromPitch(pitch);
  }

  @Override
  public String toString() {
    return intervals.toString();
  }
}
