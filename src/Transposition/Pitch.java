package Transposition;

/* Class representing a midi pitch, consisting of an octave and a note */
public class Pitch {

  private int octave;
  private Note note;

  public Pitch(int octave, Note note) {
    this.octave = octave;
    this.note = note;
  }

  public int getOctave() {
    return octave;
  }

  public Note getNote() {
    return note;
  }

  public void setNote(Note note) {
    this.note = note;
  }

  public void setOctave(int octave) {
    this.octave = octave;
  }
}
