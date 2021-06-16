import Transposition.Note;
import Transposition.TransposeTrack;
import Transposition.TranspositionException;
import java.io.File;
import java.util.Scanner;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;


/* Command line interface to the program, contains only a main method and helpers */
public class CommandLineInterface {

  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println("Please provide an input and output file");
      return;
    }

    File inputFile = new File(args[0]), outputFile = null;
    Sequencer sequencer = null;

    if (args.length > 1) {
      outputFile = new File(args[1]);
    } else {
      try {
        sequencer = MidiSystem.getSequencer(true);

        if (sequencer == null) {
          System.out.println("No Sequencers Found");
          return;
        } else {
          System.out.println(sequencer.getDeviceInfo().getName());
          System.out.println(sequencer.getDeviceInfo().getDescription());
          sequencer.open();
        }
      } catch (MidiUnavailableException e) {
        System.out.println("Midi error: " + e.getMessage());
        return;
      }
    }


    Scanner scanner = new Scanner(System.in);

    Note inputRoot, outputRoot;
    int inputMode, outputMode;

    System.out.println("Please enter the root note of the input key");
    inputRoot = Note.getNoteFromName(scanner.nextLine());
    if (!checkNote(inputRoot)) {
      System.out.println("Invalid note entered");
      return;
    }

    System.out.println("Please enter the Mode of the input key");
    inputMode = getModeFromName(scanner.nextLine());
    if (!checkMode(inputMode)) {
      System.out.println("Invalid mode entered");
      return;
    }

    System.out.println("Please enter the root note of the output key");
    outputRoot = Note.getNoteFromName(scanner.nextLine());
    if (!checkNote(outputRoot)) {
      System.out.println("Invalid note entered");
      return;
    }

    System.out.println("Please enter the Mode of the output key");
    outputMode = getModeFromName(scanner.nextLine());
    if (!checkMode(outputMode)) {
      System.out.println("Invalid mode entered");
      return;
    }

    TransposeTrack tt = new TransposeTrack(inputRoot, inputMode, outputRoot, outputMode);

    try {
      if (args.length > 1) {
        tt.transposeToFile(inputFile, outputFile);
        System.out.println("Transposed file created successfully at " + args[1]);
      } else {
        System.out.println("Starting Transposition");
        tt.transposeAndPlay(inputFile, sequencer);
      }

    } catch (TranspositionException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }

    if (sequencer != null) {
      System.out.println("Stopping sequencer");
      sequencer.stop();
      System.out.println("Closing sequencer");
      sequencer.close();
    }

    System.out.println("Exiting Normally");

  }

  /* Checks if a mode is valid or not (from 0 to 6) */
  private static boolean checkMode(int mode) {
    return mode >= 0 && mode <= 6;
  }

  /* Gets a mode number from a String
  *  Returns the number if an integer is given
  *  Otherwise returns the corresponding number for a named mode
  *  Returns -1 if a string is given which does not refer to a mode */
  private static int getModeFromName(String input) {
    try {
      return Integer.parseInt(input);
    } catch(NumberFormatException e) {
      String tmp = input.toLowerCase();

      if (tmp.equals("major") || tmp.equals("ionian")) {
        return 0;
      }
      if (tmp.equals("dorian")) {
        return 1;
      }
      if (tmp.equals("phrygian")) {
        return 2;
      }
      if (tmp.equals("lydian")) {
        return 3;
      }
      if (tmp.equals("mixolydian")) {
        return 4;
      }
      if (tmp.equals("aeolian") || tmp.equals("minor")) {
        return 5;
      }
      if (tmp.equals("locrian")) {
        return 6;
      }
      return -1;
    }

  }

  /* Checks if a Transposition.Note object is valid or not */
  private static boolean checkNote(Note note) {
    return note != null;
  }

}
