package Transposition;

public class TransposeStamp {

  private final TransposeMap transposer;
  private final long tickPosition;

  public TransposeStamp(TransposeMap transposer, long tickPosition) {
    this.transposer = transposer;
    this.tickPosition = tickPosition;
  }

  public TransposeMap getTransposer() {
    return transposer;
  }

  public long getTickPosition() {
    return tickPosition;
  }
}
