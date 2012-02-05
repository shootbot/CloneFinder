import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;              //for layout managers and more
import java.awt.event.*;        //for action events
import java.net.URL;
import java.io.IOException;
import java.io.File;
import javax.swing.filechooser.*;
import javax.swing.tree.*;

public class SwingTest extends JPanel implements ActionListener {

	protected JLabel legendLabel1;
	protected JLabel legendLabel2;
	protected JFileChooser fc;
	protected JButton browseBtn1;
	protected JButton browseBtn2;
	protected JButton runBtn;

    public SwingTest() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//
		// file chooser
		//
		fc = new JFileChooser();
		fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.addChoosableFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isDirectory();
			}
			@Override
			public String getDescription() {
				return "Только папки";
			}
		});
		//
		// choose textfields
		//
        JTextField textField1 = new JTextField(10);
		JTextField textField2 = new JTextField(10);
        JLabel textFieldLabel1 = new JLabel("Первая папка:");
		JLabel textFieldLabel2 = new JLabel("Вторaя папка:");
		//
		// legend labels
		//
		legendLabel1 = new JLabel("Одинаковые");
		legendLabel1.setForeground(Color.black);
		legendLabel2 = new JLabel("Разные");
		legendLabel2.setForeground(Color.red);
		JLabel legendLabel3 = new JLabel("Только в первой");
		legendLabel3.setForeground(new Color(0, 160, 0));
		JLabel legendLabel4 = new JLabel("Только во второй");
		legendLabel4.setForeground(Color.blue);
        //textFieldLabel.setLabelFor(textField);
		//
		// browse buttons
		//
		browseBtn1 = new JButton("Обзор");
        browseBtn1.addActionListener(this);
		browseBtn2 = new JButton("Обзор");
        browseBtn2.addActionListener(this);
		//
		// run button
		//
		runBtn = new JButton("Сравнить");
		runBtn.addActionListener(this);
		//
		// pane for label, textfield and button for first directory
		//
		JPanel dir1Pane = new JPanel(new FlowLayout());
		dir1Pane.add(textFieldLabel1);
		dir1Pane.add(textField1);
		dir1Pane.add(browseBtn1);
		dir1Pane.setBorder(BorderFactory.createLineBorder(Color.blue));
		//
		// pane for label, textfield and button for first directory
		//
		JPanel dir2Pane = new JPanel(new FlowLayout());
		dir2Pane.add(textFieldLabel2);
		dir2Pane.add(textField2);
		dir2Pane.add(browseBtn2);
		dir2Pane.setBorder(BorderFactory.createLineBorder(Color.blue));
		//
		// pane for directory panes
		//
		JPanel dirsPane = new JPanel();
		dirsPane.setLayout(new BoxLayout(dirsPane, BoxLayout.Y_AXIS));
		dirsPane.add(dir1Pane);
		dirsPane.add(dir2Pane);
		dirsPane.setBorder(BorderFactory.createLineBorder(Color.blue));
		//
		// pane for run button
		//
		JPanel runPane = new JPanel(new FlowLayout());
		runPane.add(runBtn);
		runPane.setBorder(BorderFactory.createLineBorder(Color.blue));
		//
		// pane for legend labels
		//
		JPanel legendsPane = new JPanel(new GridLayout(2, 2));
		legendsPane.add(legendLabel1);
		legendsPane.add(legendLabel3);
		legendsPane.add(legendLabel2);
		legendsPane.add(legendLabel4);
		legendsPane.setBorder(BorderFactory.createLineBorder(Color.blue));//BorderFactory.createTitledBorder("Легенда"));
		//
		// pane for all top controls
		//
		JPanel topPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		topPane.add(dirsPane);
		topPane.add(runPane);
		topPane.add(legendsPane);
		topPane.setBorder(BorderFactory.createLineBorder(Color.blue));
		topPane.setPreferredSize(new Dimension(600, 100));
		//
		// pane for resulting directory tree
		//
		JPanel bottomPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		bottomPane.setBorder(BorderFactory.createLineBorder(Color.blue));
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
		DefaultMutableTreeNode child1 = new DefaultMutableTreeNode("child1");
		DefaultMutableTreeNode child2 = new DefaultMutableTreeNode("child2");
		DefaultMutableTreeNode grandchild = new DefaultMutableTreeNode("grand");
		root.add(child1);
		root.add(child2);
		child2.add(grandchild);
		
		
		bottomPane.add(new JScrollPane(new JTree(root)));
		
		
		add(topPane);
		add(bottomPane);
		//setMinimumSize(new Dimension(500, 100));
		
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == browseBtn1) {
			if (fc.showOpenDialog(SwingTest.this) == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
				legendLabel1.setText(file.getName());
			}
		} else if(e.getSource() == browseBtn2) {
			if (fc.showOpenDialog(SwingTest.this) == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
				legendLabel2.setText(file.getName());
			}
		} else if(e.getSource() == runBtn) {
			legendLabel1.setText("run");
		}
    }
	
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("TextSamplerDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new SwingTest());
		frame.setLocation(200, 100);
		frame.setMinimumSize(new Dimension(600, 500));
		frame.setPreferredSize(new Dimension(600, 500));
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