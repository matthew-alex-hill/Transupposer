package GUI;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class PanelOnController implements ItemListener {
  
  private ToggleablePanel panel;
  private Model transposerModel;

  public PanelOnController(Model transposerModel, ToggleablePanel panel) {
    this.transposerModel = transposerModel;
    this.panel = panel;
  }

  @Override
  public void itemStateChanged(ItemEvent itemEvent) {
    panel.setVisibility(itemEvent.getStateChange() == 1);
    transposerModel.updateObservers();
  }
}