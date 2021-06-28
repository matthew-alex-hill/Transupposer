package GUI.Controllers;

import GUI.Model;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

/* Change listener for the mode sliders */
public class SliderController extends Controller {

  boolean isInput;
  private final JSlider slider;

  public SliderController(Model transposerModel, JSlider slider, boolean isInput) {
    super(transposerModel);

    this.slider = slider;
    this.isInput = isInput;
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    //Unused, does nothing as an action listener
  }

  @Override
  public void stateChanged(ChangeEvent changeEvent) {
    int val = (3 * slider.getValue() + 6) % 7;
    if (isInput) {
      transposerModel.setInputMode(val);
    } else {
      transposerModel.setOutputMode(val);
    }
  }
}
