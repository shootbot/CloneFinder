import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;              //for layout managers and more
import java.awt.event.*;        //for action events
import java.net.URL;
import java.io.IOException;
import java.io.File;

public class SwingTest extends JPanel implements ActionListener {

    protected static final String browseBtn1String = "browseButton1";
	protected static final String browseBtn2String = "browseButton2";
	protected static final String runBtnString = "runButton";
	protected JLabel legendLabel1;
	protected JFileChooser fc;
	protected JButton browseBtn1;

    public SwingTest() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		fc = new JFileChooser();

        JTextField textField1 = new JTextField(10);
		JTextField textField2 = new JTextField(10);
        JLabel textFieldLabel1 = new JLabel("Первая папка:");
		JLabel textFieldLabel2 = new JLabel("Вторaя папка:");
		legendLabel1 = new JLabel("Одинаковые");
		legendLabel1.setForeground(Color.black);
		JLabel legendLabel2 = new JLabel("Разные");
		legendLabel2.setForeground(Color.red);
		JLabel legendLabel3 = new JLabel("Только в первой");
		legendLabel3.setForeground(new Color(0, 160, 0));
		JLabel legendLabel4 = new JLabel("Только во второй");
		legendLabel4.setForeground(Color.blue);
        //textFieldLabel.setLabelFor(textField);
		
		browseBtn1 = new JButton("Обзор");
		browseBtn1.setActionCommand(browseBtn1String);
        browseBtn1.addActionListener(this);
		JButton browseBtn2 = new JButton("Обзор");
		browseBtn2.setActionCommand(browseBtn2String);
        browseBtn2.addActionListener(this);
		JButton runBtn = new JButton("Сравнить");
		runBtn.setActionCommand(runBtnString);
        runBtn.addActionListener(this);

		JPanel dir1Pane = new JPanel(new FlowLayout());
		dir1Pane.add(textFieldLabel1);
		dir1Pane.add(textField1);
		dir1Pane.add(browseBtn1);
		dir1Pane.setBorder(BorderFactory.createLineBorder(Color.blue));
		
		JPanel dir2Pane = new JPanel(new FlowLayout());
		dir2Pane.add(textFieldLabel2);
		dir2Pane.add(textField2);
		dir2Pane.add(browseBtn2);
		dir2Pane.setBorder(BorderFactory.createLineBorder(Color.blue));
		
		JPanel dirsPane = new JPanel();
		dirsPane.setLayout(new BoxLayout(dirsPane, BoxLayout.Y_AXIS));
		dirsPane.add(dir1Pane);
		dirsPane.add(dir2Pane);
		dirsPane.setBorder(BorderFactory.createLineBorder(Color.blue));
		
		JPanel runPane = new JPanel(new FlowLayout());
		runPane.add(runBtn);
		runPane.setBorder(BorderFactory.createLineBorder(Color.blue));
		
		JPanel legendsPane = new JPanel(new GridLayout(2, 2));
		legendsPane.add(legendLabel1);
		legendsPane.add(legendLabel3);
		legendsPane.add(legendLabel2);
		legendsPane.add(legendLabel4);
		legendsPane.setBorder(BorderFactory.createLineBorder(Color.blue));//BorderFactory.createTitledBorder("Легенда"));
		
		JPanel topPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		topPane.add(dirsPane);
		topPane.add(runPane);
		topPane.add(legendsPane);
		topPane.setBorder(BorderFactory.createLineBorder(Color.blue));
		
		JPanel bottomPane = new JPanel(new FlowLayout());
		bottomPane.setBorder(BorderFactory.createLineBorder(Color.blue));
		
		add(topPane);
		add(bottomPane);
		//setMinimumSize(new Dimension(500, 100));
		
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
        if (browseBtn1String.equals(e.getActionCommand())) {
            legendLabel1.setText("btn1");
        } else if (browseBtn2String.equals(e.getActionCommand())) {
            legendLabel1.setText("btn2");
        } else if (runBtnString.equals(e.getActionCommand())) {
            legendLabel1.setText("runBtn");
        }
		
		if (e.getSource() == browseBtn1) {
			int returnVal = fc.showOpenDialog(SwingTest.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
				legendLabel1.setText(file.getName());
			}
		}
    }
	
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("TextSamplerDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new SwingTest());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                 //Turn off metal's use of bold fonts
				//UIManager.put("swing.boldMetal", Boolean.FALSE);
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch(Exception e) {
					System.out.println("Error setting Java LAF: " + e);
				}
				createAndShowGUI();
            }
        });
    }
}