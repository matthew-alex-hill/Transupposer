package GUI;

public class transposerGUI {
  public final static String VERSION_NAME = "Transupposer v1.0";
  public static final String selectNothingText = "None";

  public transposerModel tm = new transposerModel();

  private final Updatable view = new transposerView(tm);

  public transposerGUI() {
    tm.addObserver(view);
  }

  public static void main(String[] args) {
    new transposerGUI();
  }
}
