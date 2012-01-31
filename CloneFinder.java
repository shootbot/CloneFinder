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
		
    public static Node init() {
		String path = "c:\\foresttd\\1";
		String path2 = "c:\\foresttd\\2\\";
		
		CreateTreeVisitor treeVisitor = new CreateTreeVisitor();
		try {
			Files.walkFileTree(Paths.get(path), treeVisitor);
		} catch(Exception e) {
			System.out.println("Error: " + e);
		}
		Node tree1 = treeVisitor.getNode();

		CreateTreeVisitor treeVisitor2 = new CreateTreeVisitor();
		try {
			Files.walkFileTree(Paths.get(path2), treeVisitor2);
		} catch(Exception e) {
			System.out.println("Error: " + e);
		}
		Node tree2 = treeVisitor2.getNode();
		tree1.show(0);
		tree2.show(0);
		Node result = Node.merge(tree1, tree2);
		result.show(0);
		Node.compare(tree1, tree2, result);
		
		try {
			FileWriter fw = new FileWriter("tree.htm");
			result.writeHtml(fw, 0);
			fw.close();
		} catch(Exception e) {
			System.out.println("Error: " + e);
		}
		
		return result;
		
		//findDuplicates(tree.makeNodeSet());
    }
	
	public static void main(String[] args) {
		new CloneFinder(args);
	}

	public CloneFinder(String[] args) {
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
		
		Node result = init();
		/*String path;
		if (args.length == 0) {
			path = "n:\\_vcd";
		} else {
			path = args[0];
		}		
		CreateTreeVisitor treeVisitor = new CreateTreeVisitor();
		try {
			Files.walkFileTree(Paths.get(path), treeVisitor);
		} catch(Exception e) {
			System.out.println("Error: " + e);
		}
		Node tree = treeVisitor.getNode();*/
		
		JTree jtree = result.makeJTree();
		MyDefaultTreeCellRenderer renderer = new MyDefaultTreeCellRenderer();
		renderer.setOpenIcon(null);
		renderer.setClosedIcon(null);
		renderer.setLeafIcon(null);
		renderer.setBackgroundSelectionColor(Color.white);
		jtree.setCellRenderer(renderer);
		//tree2.setRootVisible(false);
		content.add(jtree);
		
		setSize(500, 500);
		//pack();
		setVisible(true);
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
