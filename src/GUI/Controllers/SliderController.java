package GUI.Controllers;

import GUI.Model;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

/* Change listener for the mode sliders */
public class SliderController extends Controller {

  private final ScaleType scaleType;
  private final JSlider slider;

  public SliderController(Model transposerModel, JSlider slider,
      ScaleType scaleType) {
    super(transposerModel);

    this.slider = slider;
    this.scaleType = scaleType;
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    //Unused, does nothing as an action listener
  }

  @Override
  public void stateChanged(ChangeEvent changeEvent) {
    int val = (3 * slider.getValue() + 6) % 7;
    switch (scaleType) {
      case INPUT:
        transposerModel.setInputMode(val);
        break;
      case OUTPUT:
        transposerModel.setOutputMode(val);
        break;
      case LIVE:
        transposerModel.setLiveMode(val);
    }
  }
}
