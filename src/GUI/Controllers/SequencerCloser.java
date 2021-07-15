package GUI.Controllers;

import GUI.Model;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

//Closes sequencer on exit
public class SequencerCloser implements WindowListener {

  private Model model;

  public SequencerCloser(Model model) {
    this.model = model;
  }

  @Override
  public void windowOpened(WindowEvent windowEvent) {

  }

  @Override
  public void windowClosing(WindowEvent windowEvent) {
    model.stop();
    model.getSequencer().close();
  }

  @Override
  public void windowClosed(WindowEvent windowEvent) {
  }

  @Override
  public void windowIconified(WindowEvent windowEvent) {

  }

  @Override
  public void windowDeiconified(WindowEvent windowEvent) {

  }

  @Override
  public void windowActivated(WindowEvent windowEvent) {

  }

  @Override
  public void windowDeactivated(WindowEvent windowEvent) {

  }
}
