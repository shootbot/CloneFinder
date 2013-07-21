import java.io.IOException;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.net.URL;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.JTree;
import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import net.miginfocom.swing.MigLayout;


enum NodeType {
    FOLDER, FILE;
}

// Node property: only first pool, only second pool, equal in both pools, not equal
enum Uniqueness {
    O1, O2, EQ, NE;
}

class CloneFinder extends JFrame implements ActionListener {
	protected JFileChooser fc;
	protected JButton btnBrowse1;
	protected JButton btnBrowse2;
	protected JButton btnRun;
	protected JTextField dirPath1;
	protected JTextField dirPath2;
	protected JPanel pnBottom;
	protected MyTreeCellRenderer renderer;
	private static final Color MY_DARK_GREEN = new Color(0, 150, 0);
	
	
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
        JFrame frame = new CloneFinder();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setLocation(200, 100);
		frame.setPreferredSize(new Dimension(800, 500));
        frame.pack();
		frame.setVisible(true);
		
    }
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnBrowse1) {
			if (fc.showOpenDialog(CloneFinder.this) == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
				dirPath1.setText(file.getAbsolutePath());
			}
		} else if(e.getSource() == btnBrowse2) {
			if (fc.showOpenDialog(CloneFinder.this) == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
				dirPath2.setText(file.getAbsolutePath());
			}
		} else if(e.getSource() == btnRun) {
			runComparison();
		}
    }
	
	private void runComparison() {
		String path1 = dirPath1.getText();
		String path2 = dirPath2.getText();
		
		TreeVisitor treeVisitor = new TreeVisitor();
		try {
			Files.walkFileTree(Paths.get(path1), treeVisitor);
		} catch(Exception e) {
			//JOptionPane.showMessageDialog(getTopLevelAncestor(), e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		}
		Node tree1 = treeVisitor.getNode();

		TreeVisitor treeVisitor2 = new TreeVisitor();
		try {
			Files.walkFileTree(Paths.get(path2), treeVisitor2);
		} catch(Exception e) {
			//JOptionPane.showMessageDialog((JFrame) getTopLevelAncestor(), e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
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
		
		jtree.setCellRenderer(renderer);
		pnBottom.add(new JScrollPane(jtree), "grow");
		pnBottom.revalidate();
	}
		
	public CloneFinder() {
		super("Clonefinder");
		renderer = new MyTreeCellRenderer();
		
		setLayout(new MigLayout());
		
		JPanel pnTop = new JPanel(new MigLayout());
		add(pnTop, "wrap");
		
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
		
		JLabel lblDir1 = new JLabel("Первая папка:");
		pnTop.add(lblDir1);
		
		dirPath1 = new JTextField();
		pnTop.add(dirPath1, "w 100!");
		
		btnBrowse1 = new JButton("Обзор");
        btnBrowse1.addActionListener(this);
		pnTop.add(btnBrowse1);
		
		btnRun = new JButton("Сравнить");
		btnRun.addActionListener(this);
		pnTop.add(btnRun, "wrap");
		
		JLabel lblDir2 = new JLabel("Вторaя папка:");
		pnTop.add(lblDir2);
		
		dirPath2 = new JTextField();
		pnTop.add(dirPath2, "w 100!");
		
		btnBrowse2 = new JButton("Обзор");
        btnBrowse2.addActionListener(this);
		pnTop.add(btnBrowse2);
		
		final JDialog dialog = new JDialog(this, "Легенда");
		JPanel pnDialog = new JPanel(new MigLayout());
		JLabel lblLegend1 = new JLabel("Одинаковые");
		lblLegend1.setForeground(Color.black);
		JLabel lblLegend2 = new JLabel("Разные");
		lblLegend2.setForeground(Color.red);
		JLabel lblLegend3 = new JLabel("Только в первой");
		lblLegend3.setForeground(MY_DARK_GREEN);
		JLabel lblLegend4 = new JLabel("Только во второй");
		lblLegend4.setForeground(Color.blue);
		pnDialog.add(lblLegend1, "wrap");
		pnDialog.add(lblLegend2, "wrap");
		pnDialog.add(lblLegend3, "wrap");
		pnDialog.add(lblLegend4, "wrap");
		dialog.setContentPane(pnDialog);
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.setLocationRelativeTo(this);
		dialog.pack();
		
		JButton btnLegend = new JButton("Легенда");
		btnLegend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				dialog.setVisible(true);
			}	
		});
		pnTop.add(btnLegend, "growx");
		
		pnBottom = new JPanel(new MigLayout("", "[grow]", "[grow]"));
		add(pnBottom);
	}
	
	private class MyTreeCellRenderer extends DefaultTreeCellRenderer {		
		public MyTreeCellRenderer() {
			super();
			setOpenIcon(null);
			setClosedIcon(null);
			setLeafIcon(null);
			setBackgroundSelectionColor(Color.white);
		}
		
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
				textNonSelectionColor = MY_DARK_GREEN;
				textSelectionColor = MY_DARK_GREEN;
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
