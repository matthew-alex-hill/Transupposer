package GUI;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FileSelectPanel {

  private JPanel panel = new JPanel();

  public FileSelectPanel(String label, JTextField field) {
    setUpText(label, field);
  }

  public FileSelectPanel(String label, JTextField field, JButton button) {
    setUpText(label, field);
    panel.add(button);
  }

  private void setUpText(String label, JTextField field) {
    panel.setLayout(new FlowLayout(FlowLayout.LEFT));
    panel.add(new JLabel(label));
    panel.add(field);
  }

  public JPanel getPanel() {
    return panel;
  }
}
