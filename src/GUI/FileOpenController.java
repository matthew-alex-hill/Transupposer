package GUI;

import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;

/* Action listener for file browse buttons */
public class FileOpenController extends Controller {

  private FileWindowListener listener;
  private JFileChooser fileChooser = new JFileChooser();
  private boolean isInput;

  public static FileOpenController newInputFileOpenController(Model model) {
    return new FileOpenController(model, true);
  }

  public static FileOpenController newOutputFileOpenController(Model model) {
    return new FileOpenController(model, false);
  }

  private FileOpenController(Model model, boolean isInput) {
    super(model);
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fileChooser.setFileFilter(new FileNameExtensionFilter("Standard Midi Files", "mid"));
    this.isInput = isInput;

    listener = new FileWindowListener(isInput, this, transposerModel);
    fileChooser.addActionListener(listener);
  }

  public JFileChooser getFileChooser() {
    return fileChooser;
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    if (isInput) {
      fileChooser.showOpenDialog(null);
    } else {
      fileChooser.showSaveDialog(null);
    }
  }

  @Override
  public void stateChanged(ChangeEvent changeEvent) {
    //Unused, does nothing as a change listener
  }
}
