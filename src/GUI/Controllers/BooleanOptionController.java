package GUI.Controllers;

import GUI.Model;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class BooleanOptionController implements ItemListener {

  private final Model transposerModel;
  private final Option option;

  public BooleanOptionController(Model transposerModel, Option option) {
    this.transposerModel = transposerModel;
    this.option = option;
  }

  @Override
  public void itemStateChanged(ItemEvent itemEvent) {
    boolean useCustom = itemEvent.getStateChange() == 1;
    switch (option) {
      case CUSTOM_DIATONIC:
        transposerModel.setUseCustomDiatonic(useCustom);
        break;
      case CUSTOM_CHROMATIC:
        transposerModel.setUseCustomChromatic(useCustom);
        break;
      case AUTO_UPDATE:
        transposerModel.setUseAutoUpdate(useCustom);
        break;
      case FILE_OUTPUT:
        transposerModel.setUseFileOutput(useCustom);
        break;
      case CHANNEL:
        transposerModel.setUseChannel(useCustom);
        break;
    }
    transposerModel.updateObservers();
  }
}
