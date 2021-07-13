package GUI.Controllers;

import GUI.Model;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.event.ChangeEvent;

public class ChannelSelectorController extends Controller {

  private final JComboBox<Integer> selector;

  public ChannelSelectorController(Model transposerModel,
      JComboBox<Integer> selector) {
    super(transposerModel);
    this.selector = selector;
  }


  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    int channel = ((int) selector.getSelectedItem()) - 1;

    transposerModel.setChannel(channel);
  }

  @Override
  public void stateChanged(ChangeEvent changeEvent) {
    //Unused
  }
}
