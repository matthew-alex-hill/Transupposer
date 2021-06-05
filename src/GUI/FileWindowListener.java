package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

public class FileWindowListener implements WindowListener, ActionListener {


  private FileOpenController controller;
  private Model model;
  private boolean isInput;

  public FileWindowListener(boolean isInput, FileOpenController controller, Model model) {
    this.isInput = isInput;
    this.controller = controller;
    this.model = model;
  }


  @Override
  public void windowOpened(WindowEvent windowEvent) {
    //Unused
  }

  @Override
  public void windowClosing(WindowEvent windowEvent) {
    windowEvent.getWindow().dispose();
  }

  @Override
  public void windowClosed(WindowEvent windowEvent) {
    setRelevantFile();
  }

  @Override
  public void windowIconified(WindowEvent windowEvent) {
    //Unused
  }

  @Override
  public void windowDeiconified(WindowEvent windowEvent) {
    //Unused
  }

  @Override
  public void windowActivated(WindowEvent windowEvent) {
    //Unused
  }

  @Override
  public void windowDeactivated(WindowEvent windowEvent) {

  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    setRelevantFile();
  }

  /* Sets either the input file or output file to the selected path */
  private void setRelevantFile() {
    File file = controller.getFileChooser().getSelectedFile();
    if (file != null) {
      String path = file.getPath();
      if (isInput) {
        model.setInputFile(path);
      } else {
        model.setOutputFile(path + '/' + controller.getOutputFileName() + ".mid");
      }
    }
  }
}
