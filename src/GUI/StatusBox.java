package GUI;

import javax.swing.JTextArea;

public class StatusBox {

  private JTextArea textField;
  private StringBuilder statusList;

  public StatusBox() {
    textField = new JTextArea();
    this.statusList = new StringBuilder();
  }

  public void addStatus(String status) {
    statusList.append(status + '\n');
    textField.setText(statusList.toString());
  }

  public JTextArea getTextField() {
    return textField;
  }
}
