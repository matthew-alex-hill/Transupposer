package GUI;

import java.util.Dictionary;
import java.util.Hashtable;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

public class transposerView implements Updatable{

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
  private String inputFileLabel ="Input File";
  private String outputFileNameLabel ="Output File Name";
  private String outputFileDirLabel ="Output File Directory";
  private JLabel inputModeLabel = new JLabel("Input Mode");
  private JLabel outputModeLabel = new JLabel("Output Mode");

  private JButton runButton = new JButton("Transpose");

  public transposerView(Model model) {
    JFrame frame = new JFrame(transposerGUI.VERSION_NAME);
    frame.setSize(900, 600);

    JPanel rootsPanel = new JPanel();
    rootsPanel.add(inputRootLabel);
    rootsPanel.add(inputRootField);
    rootsPanel.add(inputRootButton);
    rootsPanel.add(outputRootLabel);
    rootsPanel.add(outputRootField);
    rootsPanel.add(outputRootButton);

    inputRootButton.addActionListener(new SubmitController(model, true, inputRootField));
    outputRootButton.addActionListener(new SubmitController(model, false, outputRootField));

    JPanel guiPanel = new JPanel();
    guiPanel.setLayout(new BoxLayout(guiPanel, BoxLayout.PAGE_AXIS));


    FileNameExtensionFilter midiFilter = new FileNameExtensionFilter("Standard Midi Files", "mid");
    FileNameExtensionFilter allFilter = new FileNameExtensionFilter("All Files", "please kill me");
    inputFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    inputFileChooser.setFileFilter(midiFilter);

    outputFileChooser.setAcceptAllFileFilterUsed(true);
    outputFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    inputFileBrowse.addActionListener(FileOpenController.newInputFileOpenController(model,
        JFileChooser.FILES_AND_DIRECTORIES, midiFilter));

    outputFileBrowse.addActionListener(FileOpenController.newOutputFileOpenController(model,
        JFileChooser.DIRECTORIES_ONLY, allFilter, outputFileName));

    FileSelectPanel inputFilePanel = new FileSelectPanel(inputFileLabel, inputFileName, inputFileBrowse);
    FileSelectPanel outputFileDirPanel = new FileSelectPanel(outputFileDirLabel, outputFileDir, outputFileBrowse);
    FileSelectPanel outputFileNamePanel = new FileSelectPanel(outputFileNameLabel, outputFileName);

    guiPanel.add(inputFilePanel.getPanel());
    guiPanel.add(outputFileNamePanel.getPanel());
    guiPanel.add(outputFileDirPanel.getPanel());
    guiPanel.add(rootsPanel);

    Dictionary<Integer, JComponent> modes = new Hashtable<Integer, JComponent>();
    modes.put(0, new JTextArea("Locrian"));
    modes.put(1, new JTextArea("Phrygian"));
    modes.put(2, new JTextArea("Aeolian\n(Minor)"));
    modes.put(3, new JTextArea("Dorian"));
    modes.put(4, new JTextArea("Mixolydian"));
    modes.put(5, new JTextArea("Ionian\n(Major)"));
    modes.put(6, new JTextArea("Lydian"));

    runButton.addActionListener(new transposeController(model));

    setUpSlider(modes, inputModeSlider, model, true);
    setUpSlider(modes, outputModeSlider, model, false);
    guiPanel.add(inputModeLabel);
    guiPanel.add(inputModeSlider);
    guiPanel.add(outputModeLabel);
    guiPanel.add(outputModeSlider);
    guiPanel.add(runButton);

    JScrollPane scroller = new JScrollPane(model.getTextField(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    guiPanel.add(scroller);

    frame.getContentPane().add(guiPanel);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setVisible(true);

  }

  private void setUpSlider(Dictionary<Integer, JComponent> modes, JSlider slider,
      Model model, boolean isInput) {
    slider.setMajorTickSpacing(1);
    slider.setSnapToTicks(true);
    slider.setLabelTable(modes);
    slider.setPaintLabels(true);
    slider.addChangeListener(new SliderController(model, slider, isInput));
  }


  @Override
  public void update(Model model) {
    inputFileName.setText(model.getInputFile());
    outputFileDir.setText(model.getOutputFile());

  }
}
