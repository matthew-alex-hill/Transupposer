package Transposition;

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

  /* Creates a Note object from a note name
  *  Transposition.Note names begin with a valid note letter eg C or c
  *  Then any number of 'b' or '#' characters can be added
  *  Returns null if an invalid string is provided */
  public static Note getNoteFromName(String input) {
    int note;

    if (input.length() == 0) {
      return null;
    }

    switch (input.toUpperCase().charAt(0)) {
      case 'C':
        note = 0;
        break;
      case 'D':
        note = 2;
        break;
      case 'E':
        note = 4;
        break;
      case 'F':
        note = 5;
        break;
      case 'G':
        note = 7;
        break;
      case 'A':
        note = 9;
        break;
      case 'B':
        note = 11;
        break;
      default:
        return null;
    }

    for (int i = 1; i < input.length(); i++) {
      if (input.charAt(i) == 'b') {
        note--;
      } else if (input.charAt(i) == '#') {
        note++;
      } else {
        return new Note(note);
      }
    }

    return new Note(note);
  }

  public int getNoteNumber() {
    return noteNumber;
  }

  /* Returns the ith note of a major scale */
  public static int getScaleNote(int i) {
    int mod = i % scale.size();
    return scale.get((mod < 0 ? mod + scale.size() : mod));
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
