package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class Controller implements ActionListener {

  protected Model transposerModel;

  public Controller(Model transposerModel) {
    this.transposerModel = transposerModel;
  }

  @Override
  public abstract void actionPerformed(ActionEvent actionEvent);
}
