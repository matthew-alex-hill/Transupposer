package GUI;

public class transposerGUI {
  public final static String VERSION_NAME = "Transupposer v0.8";

  public transposerModel tm = new transposerModel();

  private final Updatable view = new transposerView(tm);

  public transposerGUI() {
    tm.addObserver(view);
  }

  public static void main(String[] args) {
    new transposerGUI();
  }
}
