package GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

  private JLabel inputRootLabel = new JLabel("Input Root");
  private JLabel outputRootLabel = new JLabel("Output Root");
  private JLabel inputFileLabel = new JLabel("Input File");
  private JLabel outputFileLabel = new JLabel("Output File");
  private JLabel inputModeLabel = new JLabel("Input Mode");
  private JLabel outputModeLabel = new JLabel("Output Mode");
  private JLabel settingsLabel = new JLabel("Settings");
  private String darkestLabel = "Dark";
  private String brightestLabel = "Bright";

  private JButton transposeButton = new JButton("Transpose");
  private JButton playButton = new JButton("Play");
  private JButton stopButton = new JButton("Stop");

  private JComboBox<String> sequencerSelector = new JComboBox<>();

  public transposerView(Model model) {
    //frame that the the main window is in
    JFrame frame = new JFrame(transposerGUI.VERSION_NAME);
    frame.setSize(600, 800);

    URL iconURL = getClass().getResource("transupposer icon.png");

    if (iconURL != null) {
      frame.setIconImage(new ImageIcon(iconURL).getImage());
    }

    //panel for main window
    JPanel guiPanel = new JPanel();
    guiPanel.setLayout(new GridBagLayout());

    //setting up file choosers
    /*
    FileNameExtensionFilter midiFilter = new FileNameExtensionFilter("Standard Midi Files", "mid");
    FileNameExtensionFilter allFilter = new FileNameExtensionFilter("All Files", "please kill me");
    inputFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    inputFileChooser.setFileFilter(midiFilter);

    outputFileChooser.setAcceptAllFileFilterUsed(true);
    outputFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    */

    //adding listeners to buttons
    inputFileBrowse.addActionListener(FileOpenController.newInputFileOpenController(model));

    outputFileBrowse.addActionListener(FileOpenController.newOutputFileOpenController(model));

    inputRootButton.addActionListener(new SubmitController(model, true, inputRootField));
    outputRootButton.addActionListener(new SubmitController(model, false, outputRootField));

    transposeButton.addActionListener(new transposeController(model));

    Sequencer defaultSequencer = null;

    try {
      defaultSequencer = MidiSystem.getSequencer();
    } catch (MidiUnavailableException e) {
      model.addStatus(e.getMessage() + ", midi playback will likely not work");
    }
    playButton.addActionListener(new SequencerController(model, defaultSequencer, SequencerCommand.PLAY));
    stopButton.addActionListener(new SequencerController(model, defaultSequencer, SequencerCommand.STOP));

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
    placeInGridAnchor(inputFileLabel, guiPanel,0,0,1,1, GridBagConstraints.LINE_START);
    placeInGridFill(inputFileName, guiPanel,1,0,6,1, GridBagConstraints.HORIZONTAL);
    placeInGridAnchor(inputFileBrowse, guiPanel,7,0,1,1, GridBagConstraints.LINE_END);

    placeInGridAnchor(outputFileLabel, guiPanel,0,1,1,1, GridBagConstraints.LINE_START);
    placeInGridFill(outputFileName, guiPanel,1,1,6,1, GridBagConstraints.HORIZONTAL);
    placeInGridAnchor(outputFileBrowse, guiPanel,7,1,1,1, GridBagConstraints.LINE_END);

    //Root selectors
    placeInGridAnchor(inputRootLabel, guiPanel, 0,2,1,1, GridBagConstraints.LINE_START);
    placeInGridFill(inputRootField, guiPanel,1,2,2,1, GridBagConstraints.HORIZONTAL);
    placeInGrid(inputRootButton, guiPanel, 3,2,1,1);
    placeInGrid(outputRootLabel, guiPanel, 4,2,1,1);
    placeInGridFill(outputRootField, guiPanel,5,2,2,1, GridBagConstraints.HORIZONTAL);
    placeInGridAnchor(outputRootButton, guiPanel, 7,2,1,1, GridBagConstraints.LINE_END);

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

    placeInGrid(inputModeLabel, guiPanel, 0, 3, NUM_COLUMNS, 1);
    placeInGridAnchor(new JLabel(darkestLabel), guiPanel, 0,5, 1,1, GridBagConstraints.FIRST_LINE_START);
    placeInGridAnchor(new JLabel(brightestLabel), guiPanel, 7, 5,1,1, GridBagConstraints.FIRST_LINE_END);
    placeInGridFill(inputModeSlider, guiPanel, 0, 4, NUM_COLUMNS, 1, GridBagConstraints.HORIZONTAL);

    placeInGrid(outputModeLabel, guiPanel, 0, 6, NUM_COLUMNS, 1);
    placeInGridFill(outputModeSlider, guiPanel, 0, 7, NUM_COLUMNS, 1, GridBagConstraints.HORIZONTAL);

    placeInGridAnchor(new JLabel(darkestLabel), guiPanel, 0,8, 1,1, GridBagConstraints.FIRST_LINE_START);
    placeInGridAnchor(new JLabel(brightestLabel), guiPanel, 7, 8,1,1, GridBagConstraints.FIRST_LINE_END);

    //placing transpose button and error log
    placeInGrid(playButton, guiPanel, 0, 9, 2,1);
    placeInGrid(stopButton, guiPanel, 2, 9,2,1);
    placeInGrid(transposeButton, guiPanel, 4,9,4,1);

    //TODO: Fix bug here where changing sequencer stops playback working
    //placeInGrid(sequencerSelector, guiPanel,0, 10,NUM_COLUMNS, 1);
    JScrollPane scroller = new JScrollPane(model.getTextField(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    placeInGridFill(scroller, guiPanel, 0, 11,NUM_COLUMNS,3, GridBagConstraints.BOTH);

    JPanel settingsPanel = new JPanel();
    settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.PAGE_AXIS));

    settingsPanel.add(settingsLabel);

    JPanel framePanel = new JPanel();
    framePanel.setLayout(new GridBagLayout());

    placeInFrame(guiPanel, framePanel, 0,1,0.8);
    placeInFrame(new JSeparator(SwingConstants.VERTICAL), framePanel, 1,0, 0);
    placeInFrame(settingsPanel, framePanel, 1,1, 0.2);

    frame.getContentPane().add(guiPanel);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setVisible(true);

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
    inputFileName.setText(model.getInputFile());
    outputFileName.setText(model.getOutputFile());

  }
}
