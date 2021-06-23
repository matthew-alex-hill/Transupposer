package GUI;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class CustomScaleController implements ItemListener {
  private Model transposerModel;

  public CustomScaleController(Model transposerModel) {
    this.transposerModel = transposerModel;
  }

  @Override
  public void itemStateChanged(ItemEvent itemEvent) {
    transposerModel.setUseCustomIntervals(itemEvent.getStateChange() == 1);
    transposerModel.updateObservers();
  }
}
