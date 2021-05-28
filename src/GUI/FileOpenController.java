package GUI;

import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileOpenController extends Controller {

  private FileWindowListener listener;
  private JFileChooser fileChooser = new JFileChooser();
  private boolean isInput;

  public FileOpenController(Model model, int fileTypes, FileNameExtensionFilter filter, boolean isInput) {
    super(model);
    fileChooser.setFileSelectionMode(fileTypes);
    fileChooser.setFileFilter(filter);
    this.isInput = isInput;

    listener = new FileWindowListener(isInput, this, transposerModel);
    fileChooser.addActionListener(listener);
  }

  public JFileChooser getFileChooser() {
    return fileChooser;
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    fileChooser.showOpenDialog(null);
  }
}
