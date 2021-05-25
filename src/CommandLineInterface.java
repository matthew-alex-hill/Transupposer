import java.io.File;
import java.util.Scanner;


/* Command line interface to the program, contains only a main method and helpers */
public class CommandLineInterface {

  public static void main(String[] args) {
    if (args.length < 2) {
      System.out.println("Please provide an input and output file");
      return;
    }

    File inputFile = new File(args[0]), outputFile = new File(args[1]);
    Scanner scanner = new Scanner(System.in);

    Note inputRoot, outputRoot;
    int inputMode, outputMode;

    System.out.println("Please enter the root note of the input key");
    inputRoot = getNoteFromName(scanner.nextLine());
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
    outputRoot = getNoteFromName(scanner.nextLine());
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

    if (tt.transposeToFile(inputFile, outputFile) == 0) {
      System.out.println("Transposed file created successfully at " + args[1]);
    } else {
      System.out.println("An error occurred");
    }

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

  /* Checks if a Note object is valid or not */
  private static boolean checkNote(Note note) {
    return note != null;
  }

  /* Creates a Note object from a note name
  *  Note names begin with a valid note letter eg C or c
  *  Then any number of 'b' or '#' characters can be added
  *  Returns null if an invalid string is provided */
  private static Note getNoteFromName(String input) {
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

}
