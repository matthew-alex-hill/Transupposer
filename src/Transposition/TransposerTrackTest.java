package Transposition;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Test;

/* Test suite for a TransposerTrack, contains the following:
*  Transpositions from C major to each of the 7 greek modes in C
*  Randomly selected transpositions from one greek mode in C to another
*  Transpositions from one major key to another
*  Completely randomly selected transpositions from one root and mode to another */
public class TransposerTrackTest {
  private static Note C = new Note(0);
  private static Note CSharp = new Note(1);
  private static Note D = new Note(2);
  private static Note DSharp = new Note(3);
  private static Note E = new Note(4);
  private static Note F = new Note(5);
  private static Note FSharp = new Note(6);
  private static Note G = new Note(7);
  private static Note GSharp = new Note(8);
  private static Note A = new Note(9);
  private static Note ASharp = new Note(10);
  private static Note B = new Note(11);

  private static JUnitRuleMockery context = new JUnitRuleMockery();
  private static TransposeMap transposer = context.mock(TransposeMap.class);

  @Test
  public void cMajorToCMajor() {

    context.checking(new Expectations() {{
      exactly(1).of(transposer).addInterval(C, C);
      exactly(1).of(transposer).addInterval(CSharp, CSharp);
      exactly(1).of(transposer).addInterval(D, D);
      exactly(1).of(transposer).addInterval(DSharp, DSharp);
      exactly(1).of(transposer).addInterval(E, E);
      exactly(1).of(transposer).addInterval(F, F);
      exactly(1).of(transposer).addInterval(FSharp, FSharp);
      exactly(1).of(transposer).addInterval(G, G);
      exactly(1).of(transposer).addInterval(GSharp, GSharp);
      exactly(1).of(transposer).addInterval(A, A);
      exactly(1).of(transposer).addInterval(ASharp, ASharp);
      exactly(1).of(transposer).addInterval(B, B);

      allowing(transposer).containsInterval(with(aNonNull(Note.class)));
    }});

    TransposeTrack tt = new TransposeTrack(C, 0, C,0, transposer);
  }

  @Test
  public void cMajorToCDorian() {

    context.checking(new Expectations() {{
      exactly(1).of(transposer).addInterval(D, D);
      exactly(1).of(transposer).addInterval(E, DSharp);
      exactly(1).of(transposer).addInterval(F, F);
      exactly(1).of(transposer).addInterval(G, G);
      exactly(1).of(transposer).addInterval(A, A);
      exactly(1).of(transposer).addInterval(B, ASharp);
      exactly(1).of(transposer).addInterval(C, C);
      exactly(1).of(transposer).addInterval(CSharp, CSharp);
      exactly(1).of(transposer).addInterval(DSharp, DSharp);
      exactly(1).of(transposer).addInterval(FSharp, FSharp);
      exactly(1).of(transposer).addInterval(GSharp, GSharp);
      exactly(1).of(transposer).addInterval(ASharp, ASharp);

      allowing(transposer).containsInterval(with(aNonNull(Note.class)));
    }});

    TransposeTrack tt = new TransposeTrack(C, 0, C,1, transposer);
  }

  @Test
  public void cMajorToCPhrygian() {

    context.checking(new Expectations() {{
      exactly(1).of(transposer).addInterval(E, DSharp);
      exactly(1).of(transposer).addInterval(F, F);
      exactly(1).of(transposer).addInterval(G, G);
      exactly(1).of(transposer).addInterval(A, GSharp);
      exactly(1).of(transposer).addInterval(B, ASharp);
      exactly(1).of(transposer).addInterval(D, CSharp);
      exactly(1).of(transposer).addInterval(C, C);
      exactly(1).of(transposer).addInterval(CSharp, CSharp);
      exactly(1).of(transposer).addInterval(DSharp, DSharp);
      exactly(1).of(transposer).addInterval(FSharp, FSharp);
      exactly(1).of(transposer).addInterval(GSharp, GSharp);
      exactly(1).of(transposer).addInterval(ASharp, ASharp);

      allowing(transposer).containsInterval(with(aNonNull(Note.class)));
    }});

    TransposeTrack tt = new TransposeTrack(C, 0, C,2, transposer);
  }

  @Test
  public void cMajorToCLydian() {

    context.checking(new Expectations() {{
      exactly(1).of(transposer).addInterval(F, FSharp);
      exactly(1).of(transposer).addInterval(G, G);
      exactly(1).of(transposer).addInterval(A, A);
      exactly(1).of(transposer).addInterval(B, B);
      exactly(1).of(transposer).addInterval(D, D);
      exactly(1).of(transposer).addInterval(E, E);
      exactly(1).of(transposer).addInterval(C, C);
      exactly(1).of(transposer).addInterval(CSharp, CSharp);
      exactly(1).of(transposer).addInterval(DSharp, DSharp);
      exactly(1).of(transposer).addInterval(FSharp, FSharp);
      exactly(1).of(transposer).addInterval(GSharp, GSharp);
      exactly(1).of(transposer).addInterval(ASharp, ASharp);

      allowing(transposer).containsInterval(with(aNonNull(Note.class)));
    }});

    TransposeTrack tt = new TransposeTrack(C, 0, C,3, transposer);
  }

  @Test
  public void cMajorToCMixolydian() {

    context.checking(new Expectations() {{
      exactly(1).of(transposer).addInterval(G, G);
      exactly(1).of(transposer).addInterval(A, A);
      exactly(1).of(transposer).addInterval(B, ASharp);
      exactly(1).of(transposer).addInterval(D, D);
      exactly(1).of(transposer).addInterval(E, E);
      exactly(1).of(transposer).addInterval(F, F);
      exactly(1).of(transposer).addInterval(C, C);
      exactly(1).of(transposer).addInterval(CSharp, CSharp);
      exactly(1).of(transposer).addInterval(DSharp, DSharp);
      exactly(1).of(transposer).addInterval(FSharp, FSharp);
      exactly(1).of(transposer).addInterval(GSharp, GSharp);
      exactly(1).of(transposer).addInterval(ASharp, ASharp);

      allowing(transposer).containsInterval(with(aNonNull(Note.class)));
    }});

    TransposeTrack tt = new TransposeTrack(C, 0, C,4, transposer);
  }

  @Test
  public void cMajorToCAeolian() {

    context.checking(new Expectations() {{
      exactly(1).of(transposer).addInterval(A, GSharp);
      exactly(1).of(transposer).addInterval(B, ASharp);
      exactly(1).of(transposer).addInterval(D, D);
      exactly(1).of(transposer).addInterval(E, DSharp);
      exactly(1).of(transposer).addInterval(F, F);
      exactly(1).of(transposer).addInterval(G, G);
      exactly(1).of(transposer).addInterval(C, C);
      exactly(1).of(transposer).addInterval(CSharp, CSharp);
      exactly(1).of(transposer).addInterval(DSharp, DSharp);
      exactly(1).of(transposer).addInterval(FSharp, FSharp);
      exactly(1).of(transposer).addInterval(GSharp, GSharp);
      exactly(1).of(transposer).addInterval(ASharp, ASharp);

      allowing(transposer).containsInterval(with(aNonNull(Note.class)));
    }});

    TransposeTrack tt = new TransposeTrack(C, 0, C,5, transposer);
  }

  @Test
  public void cMajorToCLocrian() {

    context.checking(new Expectations() {{
      exactly(1).of(transposer).addInterval(B, ASharp);
      exactly(1).of(transposer).addInterval(D, CSharp);
      exactly(1).of(transposer).addInterval(E, DSharp);
      exactly(1).of(transposer).addInterval(F, F);
      exactly(1).of(transposer).addInterval(G, FSharp);
      exactly(1).of(transposer).addInterval(A, GSharp);
      exactly(1).of(transposer).addInterval(C, C);
      exactly(1).of(transposer).addInterval(CSharp, CSharp);
      exactly(1).of(transposer).addInterval(DSharp, DSharp);
      exactly(1).of(transposer).addInterval(FSharp, FSharp);
      exactly(1).of(transposer).addInterval(GSharp, GSharp);
      exactly(1).of(transposer).addInterval(ASharp, ASharp);

      allowing(transposer).containsInterval(with(aNonNull(Note.class)));
    }});

    TransposeTrack tt = new TransposeTrack(C, 0, C,6, transposer);
  }

  @Test
  public void cLydianToCMixolydian() {

    context.checking(new Expectations() {{
      exactly(1).of(transposer).addInterval(D, D);
      exactly(1).of(transposer).addInterval(E, E);
      exactly(1).of(transposer).addInterval(FSharp, F);
      exactly(1).of(transposer).addInterval(G, G);
      exactly(1).of(transposer).addInterval(A, A);
      exactly(1).of(transposer).addInterval(B, ASharp);
      exactly(1).of(transposer).addInterval(C, C);
      exactly(1).of(transposer).addInterval(CSharp, CSharp);
      exactly(1).of(transposer).addInterval(DSharp, DSharp);
      exactly(1).of(transposer).addInterval(F, F);
      exactly(1).of(transposer).addInterval(GSharp, GSharp);
      exactly(1).of(transposer).addInterval(ASharp, ASharp);

      allowing(transposer).containsInterval(with(aNonNull(Note.class)));
    }});

    TransposeTrack tt = new TransposeTrack(C, 3, C,4, transposer);
  }

  @Test
  public void cPhrygianToCAeolian() {

    context.checking(new Expectations() {{
      exactly(1).of(transposer).addInterval(CSharp, D);
      exactly(1).of(transposer).addInterval(DSharp, DSharp);
      exactly(1).of(transposer).addInterval(F, F);
      exactly(1).of(transposer).addInterval(G, G);
      exactly(1).of(transposer).addInterval(GSharp, GSharp);
      exactly(1).of(transposer).addInterval(ASharp, ASharp);
      exactly(1).of(transposer).addInterval(C, C);
      exactly(1).of(transposer).addInterval(D, D);
      exactly(1).of(transposer).addInterval(E, E);
      exactly(1).of(transposer).addInterval(FSharp, FSharp);
      exactly(1).of(transposer).addInterval(A, A);
      exactly(1).of(transposer).addInterval(B, B);

      allowing(transposer).containsInterval(with(aNonNull(Note.class)));
    }});

    TransposeTrack tt = new TransposeTrack(C, 2, C,5, transposer);
  }

  @Test
  public void cDoriaanToCAolian() {

    context.checking(new Expectations() {{
      exactly(1).of(transposer).addInterval(D, D);
      exactly(1).of(transposer).addInterval(DSharp, DSharp);
      exactly(1).of(transposer).addInterval(F, F);
      exactly(1).of(transposer).addInterval(G, G);
      exactly(1).of(transposer).addInterval(A, GSharp);
      exactly(1).of(transposer).addInterval(ASharp, ASharp);
      exactly(1).of(transposer).addInterval(C, C);
      exactly(1).of(transposer).addInterval(CSharp, CSharp);
      exactly(1).of(transposer).addInterval(E, E);
      exactly(1).of(transposer).addInterval(FSharp, FSharp);
      exactly(1).of(transposer).addInterval(GSharp, GSharp);
      exactly(1).of(transposer).addInterval(B, B);

      allowing(transposer).containsInterval(with(aNonNull(Note.class)));
    }});

    TransposeTrack tt = new TransposeTrack(C, 1, C,5, transposer);
  }

  @Test
  public void cMixoLydianToCLocrian() {

    context.checking(new Expectations() {{
      exactly(1).of(transposer).addInterval(D, CSharp);
      exactly(1).of(transposer).addInterval(E, DSharp);
      exactly(1).of(transposer).addInterval(F, F);
      exactly(1).of(transposer).addInterval(G, FSharp);
      exactly(1).of(transposer).addInterval(A, GSharp);
      exactly(1).of(transposer).addInterval(ASharp, ASharp);
      exactly(1).of(transposer).addInterval(C, C);
      exactly(1).of(transposer).addInterval(CSharp, CSharp);
      exactly(1).of(transposer).addInterval(DSharp, DSharp);
      exactly(1).of(transposer).addInterval(FSharp, FSharp);
      exactly(1).of(transposer).addInterval(GSharp, GSharp);
      exactly(1).of(transposer).addInterval(B, B);

      allowing(transposer).containsInterval(with(aNonNull(Note.class)));
    }});

    TransposeTrack tt = new TransposeTrack(C, 4, C,6, transposer);
  }

  @Test
  public void cLydianToCMajor() {

    context.checking(new Expectations() {{
      exactly(1).of(transposer).addInterval(D, D);
      exactly(1).of(transposer).addInterval(E, E);
      exactly(1).of(transposer).addInterval(FSharp, F);
      exactly(1).of(transposer).addInterval(G, G);
      exactly(1).of(transposer).addInterval(A, A);
      exactly(1).of(transposer).addInterval(B, B);
      exactly(1).of(transposer).addInterval(C, C);
      exactly(1).of(transposer).addInterval(CSharp, CSharp);
      exactly(1).of(transposer).addInterval(DSharp, DSharp);
      exactly(1).of(transposer).addInterval(F, F);
      exactly(1).of(transposer).addInterval(GSharp, GSharp);
      exactly(1).of(transposer).addInterval(ASharp, ASharp);

      allowing(transposer).containsInterval(with(aNonNull(Note.class)));
    }});

    TransposeTrack tt = new TransposeTrack(C, 3, C,0, transposer);
  }

  @Test
  public void cPhrygianToCMajor() {

    context.checking(new Expectations() {{
      exactly(1).of(transposer).addInterval(CSharp, D);
      exactly(1).of(transposer).addInterval(DSharp, E);
      exactly(1).of(transposer).addInterval(F, F);
      exactly(1).of(transposer).addInterval(G, G);
      exactly(1).of(transposer).addInterval(GSharp, A);
      exactly(1).of(transposer).addInterval(ASharp, B);
      exactly(1).of(transposer).addInterval(C, C);
      exactly(1).of(transposer).addInterval(D, D);
      exactly(1).of(transposer).addInterval(E, E);
      exactly(1).of(transposer).addInterval(FSharp, FSharp);
      exactly(1).of(transposer).addInterval(A, A);
      exactly(1).of(transposer).addInterval(B, B);

      allowing(transposer).containsInterval(with(aNonNull(Note.class)));
    }});

    TransposeTrack tt = new TransposeTrack(C, 2, C,0, transposer);
  }

  @Test
  public void cMajorToEMajor() {

    context.checking(new Expectations() {{
      exactly(1).of(transposer).addInterval(C, E);
      exactly(1).of(transposer).addInterval(CSharp, F);
      exactly(1).of(transposer).addInterval(D, FSharp);
      exactly(1).of(transposer).addInterval(DSharp, G);
      exactly(1).of(transposer).addInterval(E, GSharp);
      exactly(1).of(transposer).addInterval(F, A);
      exactly(1).of(transposer).addInterval(FSharp, ASharp);
      exactly(1).of(transposer).addInterval(G, B);
      exactly(1).of(transposer).addInterval(GSharp, C);
      exactly(1).of(transposer).addInterval(A, CSharp);
      exactly(1).of(transposer).addInterval(ASharp, D);
      exactly(1).of(transposer).addInterval(B, DSharp);

      exactly(1).of(transposer).addOctaveChangeIfAbsent(GSharp, 1);
      exactly(1).of(transposer).addOctaveChangeIfAbsent(A, 1);
      exactly(1).of(transposer).addOctaveChangeIfAbsent(ASharp, 1);
      exactly(1).of(transposer).addOctaveChangeIfAbsent(B, 1);

      allowing(transposer).containsInterval(with(aNonNull(Note.class)));
    }});

    TransposeTrack tt = new TransposeTrack(C, 0, E,0, transposer);
  }

  @Test
  public void cMajorToGMajor() {

    context.checking(new Expectations() {{
      exactly(1).of(transposer).addInterval(C, G);
      exactly(1).of(transposer).addInterval(CSharp, GSharp);
      exactly(1).of(transposer).addInterval(D, A);
      exactly(1).of(transposer).addInterval(DSharp, ASharp);
      exactly(1).of(transposer).addInterval(E, B);
      exactly(1).of(transposer).addInterval(F, C);
      exactly(1).of(transposer).addInterval(FSharp, CSharp);
      exactly(1).of(transposer).addInterval(G, D);
      exactly(1).of(transposer).addInterval(GSharp, DSharp);
      exactly(1).of(transposer).addInterval(A, E);
      exactly(1).of(transposer).addInterval(ASharp, F);
      exactly(1).of(transposer).addInterval(B, FSharp);

      exactly(1).of(transposer).addOctaveChangeIfAbsent(F, 1);
      exactly(1).of(transposer).addOctaveChangeIfAbsent(FSharp, 1);
      exactly(1).of(transposer).addOctaveChangeIfAbsent(G, 1);
      exactly(1).of(transposer).addOctaveChangeIfAbsent(GSharp, 1);
      exactly(1).of(transposer).addOctaveChangeIfAbsent(A, 1);
      exactly(1).of(transposer).addOctaveChangeIfAbsent(ASharp, 1);
      exactly(1).of(transposer).addOctaveChangeIfAbsent(B, 1);

      allowing(transposer).containsInterval(with(aNonNull(Note.class)));
    }});

    TransposeTrack tt = new TransposeTrack(C, 0, G,0, transposer);
  }

  @Test
  public void AMajorToFMajor() {

    context.checking(new Expectations() {{
      exactly(1).of(transposer).addInterval(C, GSharp);
      exactly(1).of(transposer).addInterval(CSharp, A);
      exactly(1).of(transposer).addInterval(D, ASharp);
      exactly(1).of(transposer).addInterval(DSharp, B);
      exactly(1).of(transposer).addInterval(E, C);
      exactly(1).of(transposer).addInterval(F, CSharp);
      exactly(1).of(transposer).addInterval(FSharp, D);
      exactly(1).of(transposer).addInterval(G, DSharp);
      exactly(1).of(transposer).addInterval(GSharp, E);
      exactly(1).of(transposer).addInterval(A, F);
      exactly(1).of(transposer).addInterval(ASharp, FSharp);
      exactly(1).of(transposer).addInterval(B, G);

      exactly(1).of(transposer).addOctaveChangeIfAbsent(C, -1);
      exactly(1).of(transposer).addOctaveChangeIfAbsent(CSharp, -1);
      exactly(1).of(transposer).addOctaveChangeIfAbsent(D, -1);
      exactly(1).of(transposer).addOctaveChangeIfAbsent(DSharp, -1);

      allowing(transposer).containsInterval(with(aNonNull(Note.class)));
    }});

    TransposeTrack tt = new TransposeTrack(A, 0, F,0, transposer);
  }

  //the following tests were selected with a random number generator
  @Test
  public void gMajorToDSharpAeolian() {

    context.checking(new Expectations() {{
      exactly(1).of(transposer).addInterval(A, F);
      exactly(1).of(transposer).addInterval(B, FSharp);
      exactly(1).of(transposer).addInterval(C, GSharp);
      exactly(1).of(transposer).addInterval(D, ASharp);
      exactly(1).of(transposer).addInterval(E, B);
      exactly(1).of(transposer).addInterval(FSharp, CSharp);
      exactly(1).of(transposer).addInterval(CSharp, A);
      exactly(1).of(transposer).addInterval(DSharp, B);
      exactly(1).of(transposer).addInterval(F, CSharp);
      exactly(1).of(transposer).addInterval(G, DSharp);
      exactly(1).of(transposer).addInterval(GSharp, E);
      exactly(1).of(transposer).addInterval(ASharp, FSharp);

      exactly(1).of(transposer).addOctaveChangeIfAbsent(C, -1);
      exactly(1).of(transposer).addOctaveChangeIfAbsent(CSharp, -1);
      exactly(1).of(transposer).addOctaveChangeIfAbsent(D, -1);
      exactly(1).of(transposer).addOctaveChangeIfAbsent(DSharp, -1);
      exactly(1).of(transposer).addOctaveChangeIfAbsent(E, -1);

      allowing(transposer).containsInterval(with(aNonNull(Note.class)));
    }});

    TransposeTrack tt = new TransposeTrack(G, 0, DSharp,5, transposer);
  }

  @Test
  public void fMixoLydianToGSharpMajor() {

    context.checking(new Expectations() {{
      exactly(1).of(transposer).addInterval(G, ASharp);
      exactly(1).of(transposer).addInterval(A, C);
      exactly(1).of(transposer).addInterval(ASharp, CSharp);
      exactly(1).of(transposer).addInterval(C, DSharp);
      exactly(1).of(transposer).addInterval(D, F);
      exactly(1).of(transposer).addInterval(DSharp, G);
      exactly(1).of(transposer).addInterval(CSharp, E);
      exactly(1).of(transposer).addInterval(E, G);
      exactly(1).of(transposer).addInterval(F, GSharp);
      exactly(1).of(transposer).addInterval(FSharp, A);
      exactly(1).of(transposer).addInterval(GSharp, B);
      exactly(1).of(transposer).addInterval(B, D);

      exactly(1).of(transposer).addOctaveChangeIfAbsent(A, 1);
      exactly(1).of(transposer).addOctaveChangeIfAbsent(ASharp, 1);
      exactly(1).of(transposer).addOctaveChangeIfAbsent(B, 1);

      allowing(transposer).containsInterval(with(aNonNull(Note.class)));
    }});

    TransposeTrack tt = new TransposeTrack(F, 4, GSharp,0, transposer);
  }

  @Test
  public void ASharpDorianToDSharpLocrian() {

    context.checking(new Expectations() {{
      exactly(1).of(transposer).addInterval(C, E);
      exactly(1).of(transposer).addInterval(CSharp, FSharp);
      exactly(1).of(transposer).addInterval(DSharp, GSharp);
      exactly(1).of(transposer).addInterval(F, A);
      exactly(1).of(transposer).addInterval(G, B);
      exactly(1).of(transposer).addInterval(GSharp, CSharp);
      exactly(1).of(transposer).addInterval(D, G);
      exactly(1).of(transposer).addInterval(E, A);
      exactly(1).of(transposer).addInterval(FSharp, B);
      exactly(1).of(transposer).addInterval(A, D);
      exactly(1).of(transposer).addInterval(ASharp, DSharp);
      exactly(1).of(transposer).addInterval(B, E);

      exactly(1).of(transposer).addOctaveChangeIfAbsent(C,-1);
      exactly(1).of(transposer).addOctaveChangeIfAbsent(CSharp,-1);
      exactly(1).of(transposer).addOctaveChangeIfAbsent(D,-1);
      exactly(1).of(transposer).addOctaveChangeIfAbsent(DSharp,-1);
      exactly(1).of(transposer).addOctaveChangeIfAbsent(E,-1);
      exactly(1).of(transposer).addOctaveChangeIfAbsent(F,-1);
      exactly(1).of(transposer).addOctaveChangeIfAbsent(FSharp,-1);
      exactly(1).of(transposer).addOctaveChangeIfAbsent(G,-1);

      allowing(transposer).containsInterval(with(aNonNull(Note.class)));
    }});

    TransposeTrack tt = new TransposeTrack(ASharp, 1, DSharp,6, transposer);
  }
}
