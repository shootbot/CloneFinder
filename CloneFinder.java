import java.io.IOException;
import java.nio.file.*;
import java.security.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.text.*;
import javax.swing.filechooser.*;
import java.awt.event.*;        //for action events
import java.net.URL;
import java.io.File;
import net.miginfocom.swing.MigLayout;


enum NodeType {
    FOLDER, FILE;
}

// Node property: only first pool, only second pool, equal in both pools, not equal
enum Uniqueness {
    O1, O2, EQ, NE;
}

class CloneFinder extends JPanel implements ActionListener {
	protected JLabel legendLabel1;
	protected JLabel legendLabel2;
	protected JFileChooser fc;
	protected JButton browseBtn1;
	protected JButton browseBtn2;
	protected JButton runBtn;
	protected JTextField dirPath1;
	protected JTextField dirPath2;
	
	
	/*public static void findDuplicates(ArrayList<Node> nodeSet) {
		Collections.sort(nodeSet);
		Node phPrev = null, phCur;
		Iterator<Node> i = nodeSet.iterator();
		while (i.hasNext()) {
			phCur = i.next();
			if (phPrev == null) {
				phPrev = phCur;
				continue;
			} else {
				if (phCur.compareTo(phPrev) == 0) {
					System.out.println("Duplicate: \n" + phPrev.path + "\n" + phCur.path);
				}
				phPrev = phCur;
			}
			
		}
	}*/
		
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
				//UIManager.put("swing.boldMetal", Boolean.FALSE);//Turn off metal's use of bold fonts
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch(Exception e) {
					System.out.println("Error setting Java LAF: " + e);
				}
				createAndShowGUI();
            }
        });
    }
	
	private static void createAndShowGUI() {
        JFrame frame = new JFrame("CloneFinder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new CloneFinder());
		frame.setLocation(200, 100);
		frame.setPreferredSize(new Dimension(600, 500));
        frame.pack();
        frame.setVisible(true);
    }
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == browseBtn1) {
			if (fc.showOpenDialog(CloneFinder.this) == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
				dirPath1.setText(file.getAbsolutePath());
			}
		} else if(e.getSource() == browseBtn2) {
			if (fc.showOpenDialog(CloneFinder.this) == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
				dirPath2.setText(file.getAbsolutePath());
			}
		} else if(e.getSource() == runBtn) {
			runComparison();
		}
    }
	
	private void runComparison() {
		String path1 = dirPath1.getText();
		String path2 = dirPath2.getText();
		
		CreateTreeVisitor treeVisitor = new CreateTreeVisitor();
		try {
			Files.walkFileTree(Paths.get(path1), treeVisitor);
		} catch(Exception e) {
			//JOptionPane.showMessageDialog(getTopLevelAncestor(), e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		System.out.println("after return");
		Node tree1 = treeVisitor.getNode();

		CreateTreeVisitor treeVisitor2 = new CreateTreeVisitor();
		try {
			Files.walkFileTree(Paths.get(path2), treeVisitor2);
		} catch(Exception e) {
			//JOptionPane.showMessageDialog((JFrame) getTopLevelAncestor(), e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		Node tree2 = treeVisitor2.getNode();
		//tree1.show(0);
		//tree2.show(0);
		Node result = Node.merge(tree1, tree2);
		//result.show(0);
		Node.compare(tree1, tree2, result);
		
		/*try {
			FileWriter fw = new FileWriter("tree.htm");
			result.writeHtml(fw, 0);
			fw.close();
		} catch(Exception e) {
			System.out.println("Error: " + e);
		}*/
		
		JTree jtree = new JTree(result.makeJNode());
		MyDefaultTreeCellRenderer renderer = new MyDefaultTreeCellRenderer();
		renderer.setOpenIcon(null);
		renderer.setClosedIcon(null);
		renderer.setLeafIcon(null);
		renderer.setBackgroundSelectionColor(Color.white);
		jtree.setCellRenderer(renderer);
		add(new JScrollPane(jtree));
		this.revalidate();
	}
		

	public CloneFinder() {
		setLayout(new MigLayout());
		//
		// file chooser
		//
		fc = new JFileChooser();
		fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
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
        dirPath1 = new JTextField(10);
		dirPath2 = new JTextField(10);
        JLabel lblDir1 = new JLabel("Первая папка:");
		JLabel lblDir2 = new JLabel("Вторaя папка:");
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
		//
		// browse buttons
		//
		browseBtn1 = new JButton("Обзор");
        browseBtn1.addActionListener(this);
		browseBtn2 = new JButton("Обзор");
        browseBtn2.addActionListener(this);
		
		runBtn = new JButton("Сравнить");
		runBtn.addActionListener(this);
		
		add(lblDir1);
		add(dirPath1);
		add(browseBtn1);
		add(runBtn, "span 1 2");
		
		JPanel legendsPane = new JPanel(new GridLayout(2, 2));
		legendsPane.add(legendLabel1);
		legendsPane.add(legendLabel3);
		legendsPane.add(legendLabel2);
		legendsPane.add(legendLabel4);
		legendsPane.setBorder(BorderFactory.createLineBorder(Color.blue));//BorderFactory.createTitledBorder("Легенда"));
		add(legendsPane, "span 1 2, wrap");
		
		add(lblDir2);
		add(dirPath2);
		add(browseBtn2, "wrap");
		
		
    }
	
	private class MyDefaultTreeCellRenderer extends DefaultTreeCellRenderer {
		Color green = new Color(0, 160, 0); // darker than Color.green
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			Node node = (Node) ((DefaultMutableTreeNode) value).getUserObject();
			switch (node.uniqns) {
			case EQ:
				textNonSelectionColor = Color.black;
				textSelectionColor = Color.black;
				break;
			case NE:
				textNonSelectionColor = Color.red;
				textSelectionColor = Color.red;
				break;
			case O1:
				textNonSelectionColor = green;
				textSelectionColor = green;
				break;
			case O2:
				textNonSelectionColor = Color.blue;
				textSelectionColor = Color.blue;
				break;
			}
			return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		}
	}
 }
