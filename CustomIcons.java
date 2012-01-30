import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;

/** JTree with missing or custom icons at the tree nodes.
 *	1999 Marty Hall, http://www.apl.jhu.edu/~hall/java/
 */

public class CustomIcons extends JFrame {
	public static void main(String[] args) {
		new CustomIcons();
	}

	public CustomIcons() {
		super("CloneFinder");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			System.out.println("Error setting Java LAF: " + e);
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container content = getContentPane();
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		content.setLayout(fl);
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
			//DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			//System.out.println(node.type);
			String s = (String) ((DefaultMutableTreeNode) value).getUserObject();
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
