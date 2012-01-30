import java.io.IOException;
import java.nio.file.*;
import java.security.*;
import java.math.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.io.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;

enum NodeType {
    FOLDER, FILE;
}

// Node property: only first pool, only second pool, equal in both pools, not equal
enum Uniqueness {
    O1, O2, EQ, NE;
}

class CloneFinder extends JFrame {
	public static void findDuplicates(ArrayList<Node> nodeSet) {
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
	}
		
    public static void init() throws IOException {
		String path = "D:\\Documents and Settings\\nurmagambetov_ta\\CloneFinder\\test";
		String path2 = "D:\\Documents and Settings\\nurmagambetov_ta\\CloneFinder\\com";
		
		CreateTreeVisitor treeVisitor = new CreateTreeVisitor();
		Files.walkFileTree(Paths.get(path), treeVisitor);
		Node tree1 = treeVisitor.getNode();

		CreateTreeVisitor treeVisitor2 = new CreateTreeVisitor();
		Files.walkFileTree(Paths.get(path2), treeVisitor2);
		Node tree2 = treeVisitor2.getNode();
		tree1.show(0);
		tree2.show(0);
		Node result = Node.merge(tree1, tree1);
		result.show(0);
		
		//findDuplicates(tree.makeNodeSet());
		
		//FileWriter fw = new FileWriter("tree.htm");
		//result.writeHtml(fw, 0);
        //fw.close();
		
        tree.show();
        
    }
	
	public static void main(String[] args) {
		new CloneFinder();
	}

	public CloneFinder() {
		super("CloneFinder");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			System.out.println("Error setting Java LAF: " + e);
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container content = getContentPane();
		content.setLayout(new FlowLayout());
		//frame.getContentPane().add(tree, BorderLayout.CENTER);
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
		DefaultMutableTreeNode child;
		DefaultMutableTreeNode grandChild;
		for(int childIndex = 1; childIndex < 4; childIndex++) {
			child = new DefaultMutableTreeNode("Child " + childIndex);
			root.add(child);
			for(int grandChildIndex = 1; grandChildIndex < 4; grandChildIndex++) {
				grandChild = new DefaultMutableTreeNode("Grandchild " + childIndex + "." + grandChildIndex);
				child.add(grandChild);
			}
		}
		
		JTree tree2 = new JTree(root);
		MyDefaultTreeCellRenderer renderer2 = new MyDefaultTreeCellRenderer();
		renderer2.setOpenIcon(null);
		renderer2.setClosedIcon(null);
		renderer2.setLeafIcon(null);
		renderer2.setBackgroundSelectionColor(Color.white);
		tree2.setCellRenderer(renderer2);
		//tree2.setRootVisible(false);
		content.add(tree2);
		
		setSize(500, 500);
		//pack();
		setVisible(true);
	}
	
	private class MyDefaultTreeCellRenderer extends DefaultTreeCellRenderer {
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			String s = ((DefaultMutableTreeNode) value).getUserObject().toString();
			if (s.length() > 5) {
				textNonSelectionColor = Color.red;
				textSelectionColor = Color.red;
			} else if (s.length() > 4) {
				textNonSelectionColor = Color.blue;
				textSelectionColor = Color.blue;
			} else {
				textNonSelectionColor = new Color(0, 192, 0);
				textSelectionColor = new Color(0, 192, 0);
			}
			return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		}
	}
 }
