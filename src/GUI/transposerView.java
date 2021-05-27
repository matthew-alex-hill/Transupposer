package GUI;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class transposerView implements Updatable{

  private JSlider inputModeSlider = new JSlider(JSlider.HORIZONTAL, 0, 7, 0);
  private JSlider outputModeSlider = new JSlider(JSlider.HORIZONTAL, 0, 7, 0);
  private JTextField inputRootField = new JTextField(10);
  private JTextField outputRootField = new JTextField(10);
  private JButton inputRootButton = new JButton("Submit");
  private JButton outputRootButton = new JButton("Submit");
  private JFileChooser inputFileChooser = new JFileChooser();

  public transposerView(Model model) {
    JFrame frame = new JFrame("Transupposer v0.8");
    frame.setSize(800, 600);

    JPanel rootsPanel = new JPanel();
    rootsPanel.add(inputRootField);
    rootsPanel.add(inputRootButton);
    rootsPanel.add(outputRootField);
    rootsPanel.add(outputRootButton);

    inputRootButton.addActionListener(new SubmitController(model, true, inputRootField));
    outputRootButton.addActionListener(new SubmitController(model, false, outputRootField));

    JPanel guiPanel = new JPanel();
    guiPanel.setLayout(new BoxLayout(guiPanel, BoxLayout.PAGE_AXIS));

    guiPanel.add(rootsPanel);
    guiPanel.add(inputModeSlider);
    guiPanel.add(outputModeSlider);

    frame.getContentPane().add(guiPanel);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setVisible(true);

  }



  @Override
  public void update(Model model) {

  }
}
