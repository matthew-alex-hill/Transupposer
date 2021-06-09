package GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Dictionary;
import java.util.Hashtable;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

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
  private JTextField outputFileDir = new JTextField(50);
  private JTextField outputFileName = new JTextField(50);

  private JLabel inputRootLabel = new JLabel("Input Root");
  private JLabel outputRootLabel = new JLabel("Output Root");
  private JLabel inputFileLabel = new JLabel("Input File");
  private JLabel outputFileNameLabel = new JLabel("Output File Name");
  private JLabel outputFileDirLabel = new JLabel("Output File Directory");
  private JLabel inputModeLabel = new JLabel("Input Mode");
  private JLabel outputModeLabel = new JLabel("Output Mode");

  private JButton runButton = new JButton("Transpose");

  public transposerView(Model model) {
    //frame that the the main window is in
    JFrame frame = new JFrame(transposerGUI.VERSION_NAME);
    frame.setSize(700, 800);

    //panel for main window
    JPanel guiPanel = new JPanel();
    guiPanel.setLayout(new GridBagLayout());

    //setting up file choosers
    FileNameExtensionFilter midiFilter = new FileNameExtensionFilter("Standard Midi Files", "mid");
    FileNameExtensionFilter allFilter = new FileNameExtensionFilter("All Files", "please kill me");
    inputFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    inputFileChooser.setFileFilter(midiFilter);

    outputFileChooser.setAcceptAllFileFilterUsed(true);
    outputFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    //adding listeners to buttons
    inputFileBrowse.addActionListener(FileOpenController.newInputFileOpenController(model,
        JFileChooser.FILES_AND_DIRECTORIES, midiFilter));

    outputFileBrowse.addActionListener(FileOpenController.newOutputFileOpenController(model,
        JFileChooser.DIRECTORIES_ONLY, allFilter, outputFileName));

    inputRootButton.addActionListener(new SubmitController(model, true, inputRootField));
    outputRootButton.addActionListener(new SubmitController(model, false, outputRootField));

    runButton.addActionListener(new transposeController(model));

    //File choosers
    placeInGridAnchor(inputFileLabel, guiPanel,0,0,1,1, GridBagConstraints.LINE_START);
    placeInGridFill(inputFileName, guiPanel,1,0,6,1, GridBagConstraints.HORIZONTAL);
    placeInGridAnchor(inputFileBrowse, guiPanel,7,0,1,1, GridBagConstraints.LINE_END);

    placeInGridAnchor(outputFileNameLabel, guiPanel,0,1,1,1, GridBagConstraints.LINE_START);
    placeInGridFill(outputFileName, guiPanel,1,1,6,1, GridBagConstraints.HORIZONTAL);

    placeInGridAnchor(outputFileDirLabel, guiPanel,0,2,1,1, GridBagConstraints.LINE_START);
    placeInGridFill(outputFileDir, guiPanel,1,2,6,1, GridBagConstraints.HORIZONTAL);
    placeInGridAnchor(outputFileBrowse, guiPanel,7,2,1,1, GridBagConstraints.LINE_END);

    //Root selectors
    placeInGridAnchor(inputRootLabel, guiPanel, 0,3,1,1, GridBagConstraints.LINE_START);
    placeInGridFill(inputRootField, guiPanel,1,3,2,1, GridBagConstraints.HORIZONTAL);
    placeInGrid(inputRootButton, guiPanel, 3,3,1,1);
    placeInGrid(outputRootLabel, guiPanel, 4,3,1,1);
    placeInGridFill(outputRootField, guiPanel,5,3,2,1, GridBagConstraints.HORIZONTAL);
    placeInGridAnchor(outputRootButton, guiPanel, 7,3,1,1, GridBagConstraints.LINE_END);

    //Labels for modes
    Dictionary<Integer, JComponent> modes = new Hashtable<Integer, JComponent>();
    modes.put(0, new JLabel("Locrian", SwingConstants.CENTER));
    modes.put(1, new JLabel("Phrygian", SwingConstants.CENTER));
    modes.put(2, new JLabel("Aeolian (Minor)", SwingConstants.CENTER));
    modes.put(3, new JLabel("Dorian", SwingConstants.CENTER));
    modes.put(4, new JLabel("Mixolydian", SwingConstants.CENTER));
    modes.put(5, new JLabel("Ionian (Major)", SwingConstants.CENTER));
    modes.put(6, new JLabel("Lydian", SwingConstants.CENTER));

    //creating and placing mode sliders
    setUpSlider(modes, inputModeSlider, model, true);
    setUpSlider(modes, outputModeSlider, model, false);

    placeInGrid(inputModeLabel, guiPanel, 0, 4, NUM_COLUMNS, 1);
    placeInGridFill(inputModeSlider, guiPanel, 0, 5, NUM_COLUMNS, 1, GridBagConstraints.HORIZONTAL);

    placeInGrid(outputModeLabel, guiPanel, 0, 6, NUM_COLUMNS, 1);
    placeInGridFill(outputModeSlider, guiPanel, 0, 7, NUM_COLUMNS, 1, GridBagConstraints.HORIZONTAL);

    //placing transpose button and error log
    placeInGrid(runButton, guiPanel, 0,8,NUM_COLUMNS,1);

    JScrollPane scroller = new JScrollPane(model.getTextField(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    placeInGridFill(scroller, guiPanel, 0, 9,NUM_COLUMNS,3, GridBagConstraints.BOTH);

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
    outputFileDir.setText(model.getOutputFile());

  }
}
