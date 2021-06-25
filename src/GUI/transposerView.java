package GUI;

import Transposition.Note;
import Transposition.TransposeTrack;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class transposerView implements Updatable{

  private static int NUM_ROWS = 12;
  private static int NUM_COLUMNS = 8;
  private final JScrollPane scroller;
  private final JFrame frame;
  private JSlider inputModeSlider = new JSlider(JSlider.HORIZONTAL, 0, 6, 5);
  private JSlider outputModeSlider = new JSlider(JSlider.HORIZONTAL, 0, 6, 5);
  private JTextField inputRootField = new JTextField(12);
  private JTextField outputRootField = new JTextField(12);
  private JButton inputRootButton = new JButton("Submit");
  private JButton outputRootButton = new JButton("Submit");
  private JFileChooser inputFileChooser = new JFileChooser();
  private JFileChooser outputFileChooser = new JFileChooser();
  private JButton inputFileBrowse = new JButton("Browse");
  private JButton outputFileBrowse = new JButton("Browse");
  private JTextField inputFileName = new JTextField(50);
  private JTextField outputFileName = new JTextField(50);
  private JLabel inputRootLabel = new JLabel("Input");
  private JLabel outputRootLabel = new JLabel("Output");
  private JLabel rootsLabel = new JLabel("Set Roots");
  private JLabel inputFileLabel = new JLabel("Input File");
  private JLabel outputFileLabel = new JLabel("Output File");
  private JLabel inputModeLabel = new JLabel("Input Mode");
  private JLabel outputModeLabel = new JLabel("Output Mode");
  private JLabel settingsLabel = new JLabel("Settings");
  private String darkestLabel = "Dark";
  private String brightestLabel = "Bright";
  private JButton transposeButton = new JButton("Transpose to File");
  private JButton playButton = new JButton("▶️︎");
  private JButton stopButton = new JButton("◼︎");
  private JButton fastForwardButton = new JButton("▶︎▶︎");
  private JButton rewindButton = new JButton("◀︎◀︎");
  private JButton stepForwardButton = new JButton("➡️");
  private JButton stepBackwardButton = new JButton("⬅️");
  private JComboBox<String> sequencerSelector = new JComboBox<>();
  private JCheckBox filesOnBox = new JCheckBox("View File Select", true);
  private JCheckBox rootsOnBox = new JCheckBox("View Root Setting", true);
  private JCheckBox playOnBox = new JCheckBox("View Midi Controls", true);
  private JCheckBox customDiatonicBox, customChromaticBox;
  private ToggleablePanel filePanel = new ToggleablePanel(new JPanel());
  private ToggleablePanel rootsPanel = new ToggleablePanel(new JPanel());
  private JPanel inputModePanel = new JPanel();
  private JPanel outputModePanel = new JPanel();
  private ToggleablePanel playPanel = new ToggleablePanel(new JPanel());
  private JPanel settingsPanel = new JPanel();
  private JPanel guiPanel = new JPanel();

  public transposerView(Model model) {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    //frame that the the main window is in
    frame = new JFrame(transposerGUI.VERSION_NAME);
    frame.setSize((int) (screenSize.getWidth() / 2.8),
        (int) (screenSize.getHeight() / 2.2));

    URL iconURL = getClass().getResource("transupposer icon.png");

    if (iconURL != null) {
      frame.setIconImage(new ImageIcon(iconURL).getImage());
    }

    //setting layouts for global panels
    filePanel.setLayout(new GridBagLayout());
    rootsPanel.setLayout(new GridBagLayout());
    inputModePanel.setLayout(new GridBagLayout());
    outputModePanel.setLayout(new GridBagLayout());

    //adding listeners to buttons
    inputFileBrowse.addActionListener(FileOpenController.newInputFileOpenController(model));

    outputFileBrowse.addActionListener(FileOpenController.newOutputFileOpenController(model));

    inputRootButton.addActionListener(new SubmitController(model, true, inputRootField));
    outputRootButton.addActionListener(new SubmitController(model, false, outputRootField));

    transposeButton.addActionListener(new transposeController(model));

    //setting up midi playback buttons
    Sequencer defaultSequencer = null;

    try {
      defaultSequencer = MidiSystem.getSequencer();
    } catch (MidiUnavailableException e) {
      model.addStatus(e.getMessage() + ", midi playback will likely not work");
    }

    addSequencerController(playButton, SequencerCommand.PLAY, model, defaultSequencer);
    addSequencerController(stopButton, SequencerCommand.STOP, model, defaultSequencer);
    addSequencerController(fastForwardButton, SequencerCommand.FAST_FORWARD, model, defaultSequencer);
    addSequencerController(rewindButton, SequencerCommand.REWIND, model, defaultSequencer);
    addSequencerController(stepForwardButton, SequencerCommand.STEP_FORWARD, model, defaultSequencer);
    addSequencerController(stepBackwardButton, SequencerCommand.STEP_BACKWARD, model, defaultSequencer);

    //Setting up sequencer selector
    List<String> sequencers = new ArrayList<>();
    MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
    MidiDevice device;

    for (int i = 0; i < infos.length; i++) {
      try {
        device = MidiSystem.getMidiDevice(infos[i]);
        if (device instanceof Sequencer) {
          sequencerSelector.addItem(device.getDeviceInfo().getName());
        }
      } catch (MidiUnavailableException e) {
        model.addStatus(e.getMessage());
      }
    }

    SequencerSelectorController ssc = new SequencerSelectorController(model, sequencerSelector);
    ssc.addUser((SequencerController) playButton.getActionListeners()[0]);
    ssc.addUser((SequencerController) stopButton.getActionListeners()[0]);
    sequencerSelector.addActionListener(ssc);

    //File choosers
    placeInGridAnchor(inputFileLabel, filePanel.getPanel(),0,0,1,1, GridBagConstraints.LINE_START);
    placeInGridFill(inputFileName, filePanel.getPanel(),1,0,6,1, GridBagConstraints.HORIZONTAL);
    placeInGridAnchor(inputFileBrowse, filePanel.getPanel(),7,0,1,1, GridBagConstraints.LINE_END);

    placeInGridAnchor(outputFileLabel, filePanel.getPanel(),0,1,1,1, GridBagConstraints.LINE_START);
    placeInGridFill(outputFileName, filePanel.getPanel(),1,1,6,1, GridBagConstraints.HORIZONTAL);
    placeInGridAnchor(outputFileBrowse, filePanel.getPanel(),7,1,1,1, GridBagConstraints.LINE_END);

    //Root selectors
    placeInGridAnchor(rootsLabel, rootsPanel.getPanel(), 0, 0, NUM_COLUMNS, 1, GridBagConstraints.CENTER);
    placeInGridAnchor(inputRootLabel, rootsPanel.getPanel(), 0,1,1,1, GridBagConstraints.LINE_START);
    placeInGridFill(inputRootField, rootsPanel.getPanel(),1,1,2,1, GridBagConstraints.HORIZONTAL);
    placeInGrid(inputRootButton, rootsPanel.getPanel(), 3,1,1,1);
    placeInGrid(outputRootLabel, rootsPanel.getPanel(), 4,1,1,1);
    placeInGridFill(outputRootField, rootsPanel.getPanel(),5,1,2,1, GridBagConstraints.HORIZONTAL);
    placeInGridAnchor(outputRootButton, rootsPanel.getPanel(), 7,1,1,1, GridBagConstraints.LINE_END);

    //Labels for modes
    Dictionary<Integer, JComponent> modes = new Hashtable<Integer, JComponent>();
    modes.put(0, new JTextArea("Locrian"));
    modes.put(1, new JTextArea("Phrygian"));
    modes.put(2, new JTextArea("Aeolian\n(Minor)"));
    modes.put(3, new JTextArea("Dorian"));
    modes.put(4, new JTextArea("Mixolydian"));
    modes.put(5, new JTextArea("Ionian\n(Major)"));
    modes.put(6, new JTextArea("Lydian"));

    //creating and placing mode sliders
    setUpSlider(modes, inputModeSlider, model, true);
    setUpSlider(modes, outputModeSlider, model, false);

    placeInGrid(inputModeLabel, inputModePanel, 0, 0, NUM_COLUMNS, 1);
    placeInGridFill(inputModeSlider, inputModePanel, 0, 1, NUM_COLUMNS, 1, GridBagConstraints.HORIZONTAL);
    placeInGridAnchor(new JLabel(darkestLabel), inputModePanel, 0,2, 1,1, GridBagConstraints.FIRST_LINE_START);
    placeInGridAnchor(new JLabel(brightestLabel), inputModePanel, 7, 2,1,1, GridBagConstraints.FIRST_LINE_END);

    placeInGrid(outputModeLabel, outputModePanel, 0, 0, NUM_COLUMNS, 1);
    placeInGridFill(outputModeSlider, outputModePanel, 0, 1, NUM_COLUMNS, 1, GridBagConstraints.HORIZONTAL);
    placeInGridAnchor(new JLabel(darkestLabel), outputModePanel, 0,2, 1,1, GridBagConstraints.FIRST_LINE_START);
    placeInGridAnchor(new JLabel(brightestLabel), outputModePanel, 7, 2,1,1, GridBagConstraints.FIRST_LINE_END);

    //Midi Playback buttons
    playPanel.add(stepBackwardButton);
    playPanel.add(rewindButton);
    playPanel.add(stopButton);
    playPanel.add(playButton);
    playPanel.add(fastForwardButton);
    playPanel.add(stepForwardButton);

    //TODO: Fix bug here where changing sequencer stops playback working
    //placeInGrid(sequencerSelector, guiPanel,0, 10,NUM_COLUMNS, 1);

    scroller = new JScrollPane(model.getTextField(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    settingsPanel = new JPanel();
    settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.PAGE_AXIS));

    customDiatonicBox = new JCheckBox("Diatonic Note Customisation", model.isUseCustomDiatonic());
    customChromaticBox = new JCheckBox("Chromatic Note Customisation", model.isUseCustomChromatic());

    //Setting up panel enable checkboxes
    filesOnBox.addItemListener(new PanelOnController(model, filePanel));
    rootsOnBox.addItemListener(new PanelOnController(model, rootsPanel));
    playOnBox.addItemListener(new PanelOnController(model, playPanel));
    customDiatonicBox.addItemListener(new CustomScaleController(model, true));
    customChromaticBox.addItemListener(new CustomScaleController(model, false));

    settingsPanel.add(settingsLabel);
    settingsPanel.add(filesOnBox);
    settingsPanel.add(rootsOnBox);
    settingsPanel.add(playOnBox);
    settingsPanel.add(customDiatonicBox);
    settingsPanel.add(customChromaticBox);

    //gui panel given layout
    guiPanel.setLayout(new GridBagLayout());

    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setVisible(true);
    update(model);
  }

  private void addSequencerController(JButton button, SequencerCommand command, Model model,
      Sequencer defaultSequencer) {
    button.addActionListener(new SequencerController(model, defaultSequencer, command));
  }

  /* Sets up a slider with correct snapping, tick spacing and labels and gives them a change listener */
  private void setUpSlider(Dictionary<Integer, JComponent> modes, JSlider slider,
      Model model, boolean isInput) {
    slider.setMajorTickSpacing(1);
    slider.setSnapToTicks(true);
    slider.setLabelTable(modes);
    slider.setPaintLabels(true);
    slider.addChangeListener(new SliderController(model, slider, isInput));
  }

  /* Places an item onto grid bag layout panel */
  private void placeInGrid(JComponent component, JPanel panel,
      int gridx, int gridy, int width, int height) {
    GridBagConstraints constraints = new GridBagConstraints();

    gridPlace(component, panel, gridx, gridy, width, height, constraints);
  }

  /* Places an item onto grid bag layout panel and fills horizontally */
  private void placeInGridFill(JComponent component, JPanel panel,
      int gridx, int gridy, int width, int height, int fill) {
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = fill;

    gridPlace(component, panel, gridx, gridy, width, height, constraints);
  }

  /* Places an item onto grid bag layout panel and anchors to the given value */
  private void placeInGridAnchor(JComponent component, JPanel panel,
      int gridx, int gridy, int width, int height, int anchor) {
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.anchor = anchor;

    gridPlace(component, panel, gridx, gridy, width, height, constraints);
  }

  /* Places an item onto final frame panel panel with a given weight */
  private void placeInFrame(JComponent component, JPanel panel,
      int gridx, int width, double weight) {
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.weightx = weight;
    constraints.weighty = 1;
    constraints.gridx = gridx;
    constraints.gridy = 0;
    constraints.gridwidth = width;
    constraints.gridheight = 1;
    constraints.fill = GridBagConstraints.BOTH;

    panel.add(component, constraints);
  }

  //helper for grid placement
  private void gridPlace(JComponent component, JPanel panel, int gridx, int gridy, int width,
      int height, GridBagConstraints constraints) {
    constraints.gridx = gridx;
    constraints.gridy = gridy;
    constraints.gridwidth = width;
    constraints.gridheight = height;
    constraints.weightx = (double) width / NUM_COLUMNS;
    constraints.weighty = (double) height / NUM_ROWS;

    panel.add(component, constraints);
  }


  @Override
  public void update(Model model) {

    int gridy = 0;

    inputFileName.setText(model.getInputFile());
    outputFileName.setText(model.getOutputFile());

    guiPanel.removeAll();

    if (filePanel.isVisibile()) {
      placeInGridFill(filePanel.getPanel(), guiPanel, 0, gridy, 3, 2, GridBagConstraints.BOTH);
      gridy += 2;
    }

    if (rootsPanel.isVisibile()) {
      placeInGridFill(rootsPanel.getPanel(), guiPanel, 0, gridy, 3, 2, GridBagConstraints.HORIZONTAL);
      gridy += 2;
    }

    placeInGridFill(inputModePanel, guiPanel, 0, gridy, 3, 3, GridBagConstraints.HORIZONTAL);
    gridy += 3;
    placeInGridFill(outputModePanel, guiPanel, 0, gridy, 3,3, GridBagConstraints.HORIZONTAL);
    gridy += 3;

    JPanel adjustDiatonicPanel = new JPanel(), adjustChromaticPanel = new JPanel();
    int root = model.getInputRoot().getNoteNumber();
    int nextDiatonic = root, numDiatonics = 0;
    Note currentNote;
    JPanel currentPanel;
    TransposeTrack tt = new TransposeTrack(model.getInputRoot(), model.getInputMode(), model.getOutputRoot(), model.getOutputMode());

    //Step through each note and create an interval for either the diatonic or chromatic panel
    if (model.isUseCustomDiatonic() || model.isUseCustomChromatic()) {
      for (int i = root; i < root + 12; i++) {
        currentNote = new Note(i);

        if (!model.containsCustomNote(currentNote)) {
          currentPanel = new NoteAdjuster(model, currentNote, tt.transposeNote(currentNote))
              .getPanel();
        } else {
          currentPanel = new NoteAdjuster(model, currentNote, model.getCustomNote(currentNote))
              .getPanel();
        }
        if (i == nextDiatonic) {
          adjustDiatonicPanel
              .add(currentPanel);
          nextDiatonic += Note.getScaleNote(numDiatonics + model.getInputMode());
          numDiatonics++;
        } else {
          adjustChromaticPanel
              .add(currentPanel);
        }
      }

      if (model.isUseCustomDiatonic()) {
        placeInGridFill(adjustDiatonicPanel, guiPanel, 0, gridy++, 3, 1,
            GridBagConstraints.HORIZONTAL);
      }

      if (model.isUseCustomChromatic()) {
        placeInGridFill(adjustChromaticPanel, guiPanel, 0, gridy++, 3, 1,
            GridBagConstraints.HORIZONTAL);
      }
    }

    if (playPanel.isVisibile()) {
      placeInGridFill(playPanel.getPanel(), guiPanel, 0, gridy, 3, 1, GridBagConstraints.HORIZONTAL);
      gridy++;
    }

    placeInGrid(transposeButton, guiPanel, 1, gridy++, 1, 1);
    placeInGridFill(scroller, guiPanel, 0, gridy, 3,1, GridBagConstraints.BOTH);

    JPanel framePanel = new JPanel();
    framePanel.setLayout(new GridBagLayout());

    placeInFrame(new JSeparator(SwingConstants.VERTICAL), framePanel, 1,0, 0);
    placeInFrame(guiPanel, framePanel, 0,1,0.8);
    placeInFrame(settingsPanel, framePanel, 1,1, 0.2);

    framePanel.setVisible(false);
    frame.getContentPane().removeAll();
    frame.getContentPane().add(framePanel);
    framePanel.setVisible(true);
  }
}
