package GUI.Controllers;

import GUI.Model;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeListener;

public abstract class Controller implements ActionListener, ChangeListener {

  protected Model transposerModel;

  public Controller(Model transposerModel) {
    this.transposerModel = transposerModel;
  }

  @Override
  public abstract void actionPerformed(ActionEvent actionEvent);
}
