package GUI;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class CustomScaleController implements ItemListener {
  private Model transposerModel;
  private boolean isDiatonic;

  public CustomScaleController(Model transposerModel, boolean isDiatonic) {
    this.transposerModel = transposerModel;
    this.isDiatonic = isDiatonic;
  }

  @Override
  public void itemStateChanged(ItemEvent itemEvent) {
    boolean useCustom = itemEvent.getStateChange() == 1;
    if (isDiatonic) {
      transposerModel.setUseCustomDiatonic(useCustom);
    } else {
      transposerModel.setUseCustomChromatic(useCustom);
    }
    transposerModel.updateObservers();
  }
}
