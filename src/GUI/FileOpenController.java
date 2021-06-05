package GUI;

import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileOpenController extends Controller {

  private FileWindowListener listener;
  private JFileChooser fileChooser = new JFileChooser();
  private boolean isInput;
  private JTextField outputFileName;

  public static FileOpenController newInputFileOpenController(Model model, int filetypes, FileNameExtensionFilter filter) {
    return new FileOpenController(model, filetypes, filter, true, null);
  }

  public static FileOpenController newOutputFileOpenController(Model model, int filetypes, FileNameExtensionFilter filter, JTextField outputFileName) {
    return new FileOpenController(model, filetypes, filter, false, outputFileName);
  }

  private FileOpenController(Model model, int fileTypes, FileNameExtensionFilter filter,
      boolean isInput, JTextField outputFileName) {
    super(model);
    fileChooser.setFileSelectionMode(fileTypes);
    fileChooser.setFileFilter(filter);
    this.isInput = isInput;
    this.outputFileName = outputFileName;

    listener = new FileWindowListener(isInput, this, transposerModel);
    fileChooser.addActionListener(listener);
  }

  public JFileChooser getFileChooser() {
    return fileChooser;
  }

  //returns the text in the outputFileName text field
  public String getOutputFileName() {
    return outputFileName.getText();
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    fileChooser.showOpenDialog(null);
  }

  @Override
  public void stateChanged(ChangeEvent changeEvent) {
    //Unused, does nothing as a change listener
  }
}
