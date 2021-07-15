package GUI.Controllers;

import GUI.Model;
import GUI.ToggleablePanel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/* Controller for checkbox to toggle on/off panel */
public class PanelOnController implements ItemListener {
  
  protected ToggleablePanel panel;
  protected Model transposerModel;

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
