import java.util.ArrayList;
import java.util.Iterator;
import java.io.IOException;
import java.io.FileWriter;
import javax.swing.tree.DefaultMutableTreeNode;


public class Node implements Comparable<Node> {
	public Node parent;
	public NodeType type; // folder or file
	public Uniqueness uniqns; //
	public String name;
	public String hash;
	public String path;
	public ArrayList<Node> children;

	public Node(NodeType type, String name) {
		this.type = type;
		this.name = name;
		this.children = new ArrayList<Node>();
	}

	public void addChild(Node child) {
		children.add(child);
		child.parent = this;
	}

	public Node copy() { // copied node has same parent as original. original node's parent doesnt have copied node as a child
		Node nodeCopy = new Node(type, name);
		nodeCopy.parent = parent;
		nodeCopy.hash = hash;
		nodeCopy.uniqns = uniqns;
		nodeCopy.path = path;

		for (Node child : children) {
			nodeCopy.addChild(child.copy());
		}
		return nodeCopy;
	}

	public void delchild(Node n) {
		Node p = n.parent;
		if (p.children.contains(n)) {
			p.children.remove(n);
		} else {
			System.out.println("error: attempt to remove nonexistent child");
		}
	}

	public void setUniqnsRecursively(Uniqueness uni) {
		this.uniqns = uni;
		for (Node child : this.children) {
			child.setUniqnsRecursively(uni);
		}
	}

	public static void compare(Node tree1, Node tree2, Node result) {
		if (tree1.hash.equals(tree2.hash)) {
			result.setUniqnsRecursively(Uniqueness.EQ); // set whole branch to EQ
		} else {
			result.uniqns = Uniqueness.NE;
			for (Node child : result.children) {
				Node fpChild = tree1.getChildByName(child.name);
				Node spChild = tree2.getChildByName(child.name);
				if (fpChild == null) {
					child.setUniqnsRecursively(Uniqueness.O2);
				} else if (spChild == null) {
					child.setUniqnsRecursively(Uniqueness.O1);
				} else {
					compare(fpChild, spChild, child);
				}
			}
		}
	}

	public Node getChildByName(String name) {
		Iterator<Node> i = children.iterator();
		while (i.hasNext()) {
			Node next = i.next();
			if (next.name.equals(name)) return next;
		}
		return null;
	}

	public DefaultMutableTreeNode makeJNode() {
		DefaultMutableTreeNode jnode = new DefaultMutableTreeNode(this);
		for (Node child : this.children) {
			jnode.add(child.makeJNode());
		}
		return jnode;
	}

	public ArrayList<Node> makeNodeSet() {
		ArrayList<Node> nodeSet = new ArrayList<Node>();
		this.runThrough(nodeSet);
		return nodeSet;
	}

	public static Node merge(Node tree1, Node tree2) {
		Node result = tree1.copy();
		tryCopy(result, tree2);
		result.name = "***";
		return result;
	}

	public void runThrough(ArrayList<Node> nodeSet) {
		if (this.type == NodeType.FOLDER) {
			for (Node child : this.children) {
				child.runThrough(nodeSet);
			}
		} 
		nodeSet.add(this);
		//System.out.println("runThrough: " + ph);
	}

	public void show(int depth) {
		for (int j = 0; j < depth; j++) {
			System.out.print("  ");
		}
		System.out.println(this.name + " " + this.hash);

		for (Node c : this.children) {
			c.show(depth + 1);
		}
		if (depth == 0) System.out.println();
	}

	public static Node tryCopy(Node result, Node tree2) {
		for (Node child : tree2.children) {
			Node resultChild = result.getChildByName(child.name);
			if (resultChild == null) {
				result.addChild(child.copy());
			} else {
				tryCopy(resultChild, child);
			}
		}
		return result;
	}

	public void writeHtml(FileWriter fw, int depth) {
		String blue = "<font color=\"blue\">";
		String green = "<font color=\"green\">";
		String black = "<font color=\"black\">";
		String fontEnd = "</font>";
		String sp = "&nbsp;";
		String vert = "|";
		String plus = "+";
		String p = "<p>";
		String pEnd = "</p>";
		try {
			if (depth == 0)	{
				fw.write("<html><head>\n<style type=\"text/css\">\n");
				fw.write("p {line-height:0%;font-family:Lucida Console;}\n");
				fw.write("p.b {color:blue; line-height:0%;font-family:Lucida Console;}\n");
				fw.write("p.g {color:green; line-height:0%;font-family:Lucida Console;}\n");
				fw.write("p.r {color:red; line-height:0%;font-family:Lucida Console;}\n");
				fw.write("p.v {color:violet; line-height:0%;font-family:Lucida Console;}\n");
				fw.write("p.black {color:black; line-height:0%;font-family:Lucida Console;}\n");
				fw.write("</style><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
				fw.write("</head>\n<body>\n<br \\>");
			}
			fw.write(p);
			fw.write(black);
			for (int j = 0; j < depth; j++) {
				if (j < depth - 1) {
					fw.write(vert + sp);
				} else {
					fw.write(plus + sp);
				}
			}
			fw.write(fontEnd);
			if (type == NodeType.FOLDER) {
				fw.write(blue);// folder
			} else {
				fw.write(green);// file
			}
			fw.write(name + fontEnd + pEnd + "\n");
			for (Node c : children)	{
				c.writeHtml(fw, depth + 1);
			}
			if (depth == 0)	{
				fw.write("</body></html>");
			}
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
	}

	@Override
	public String toString() {
		return name;
	}	

	@Override
    public int compareTo(Node o) {
		return hash.compareTo(o.hash);
    }
}