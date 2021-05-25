import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* Class used to represent a musical note as an int from 0 to 11
*  Allows notes to be printed clearly for debugging purposes */
public class Note {
  private final int noteNumber;

  private static Map<Integer, String> noteNames = null;
  private final static List<Integer> scale = List.of(2,2,1,2,2,2,1);

  public Note(int noteNumber) {
    if (noteNumber < 0) {
      noteNumber += 12;
    }

    this.noteNumber = noteNumber % 12;

    if (noteNames == null) {
      noteNames = new HashMap<>();
      noteNames.put(0, "C");
      noteNames.put(1, "C#");
      noteNames.put(2, "D");
      noteNames.put(3, "D#");
      noteNames.put(4, "E");
      noteNames.put(5, "F");
      noteNames.put(6, "F#");
      noteNames.put(7, "G");
      noteNames.put(8, "G#");
      noteNames.put(9, "A");
      noteNames.put(10, "A#");
      noteNames.put(11, "B");
    }
  }

  public int getNoteNumber() {
    return noteNumber;
  }

  /* Returns the ith note of a major scale */
  public static int getScaleNote(int i) {
    return scale.get(i % scale.size());
  }

  @Override
  public String toString() {
    return noteNames.get(noteNumber);
  }

  /* Two Notes are equal if their note numbers are equal*/
  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof Note)) {
      return false;
    }

    Note otherNote = (Note) o;

    return this.getNoteNumber() == otherNote.getNoteNumber();
  }

  /* As there are only 12 unique notes, the note number is a suitable hash code */
  @Override
  public int hashCode() {
    return this.getNoteNumber();
  }
}
