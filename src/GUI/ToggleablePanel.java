package GUI;

import java.awt.LayoutManager;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ToggleablePanel {
  private final JPanel panel;
  private boolean visibility;

  public ToggleablePanel (JPanel panel) {
    this.panel = panel;
    this.visibility = true;
  }

  public JPanel getPanel() {
    return panel;
  }

  public boolean isVisibile() {
    return visibility;
  }

  public void setVisibility(boolean visibility) {
    this.visibility = visibility;
  }

  public void setLayout(LayoutManager mgr) {
    panel.setLayout(mgr);
  }

  public void add(JComponent component) {
    panel.add(component);
  }
}
