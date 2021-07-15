package GUI.Controllers;

import GUI.Model;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.event.ChangeEvent;

/* Controller for channel select dropdown */
public class ChannelSelectorController extends Controller {

  private final JComboBox<Integer> selector;

  public ChannelSelectorController(Model transposerModel,
      JComboBox<Integer> selector) {
    super(transposerModel);
    this.selector = selector;
  }


  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    if (selector != null) {
      Integer channel = (Integer) selector.getSelectedItem();
      if (channel != null) {
        channel = channel - 1;
        transposerModel.setChannel(channel);
      }
    }
  }

  @Override
  public void stateChanged(ChangeEvent changeEvent) {
    //Unused
  }
}
