import java.io.IOException;
import java.nio.file.*;
import java.security.*;
import java.math.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.io.*;

enum NodeType {
    FOLDER, FILE;
}

 // Node property: only first pool, only second pool, equal in both pools, not equal
enum Uniqueness {
    O1, O2, EQ, NE;
}

class CloneFinder {
	public static ArrayList<Node> makeNodeSet(Node tree) {
		ArrayList<Node> nodeSet = new ArrayList<Node>();
		runThrough(tree, nodeSet);
		return nodeSet;
	}
		
	public static void runThrough(Node tree, ArrayList<Node> nodeSet) {
		if (tree.type == NodeType.FOLDER) {
			for (Node child : tree.children) {
				runThrough(child, nodeSet);
			}
		} 
		nodeSet.add(tree);
		//System.out.println("runThrough: " + ph);
	}
	
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
		
    public static void main(String[] args) throws IOException {

		CreateTreeVisitor treeVisitor = new CreateTreeVisitor();
		String path;
		if (args.length == 0) {
			path = "D:\\Documents and Settings\\nurmagambetov_ta\\CloneFinder\\test";
		} else {
			path = args[0];
		}
		Files.walkFileTree(Paths.get(path), treeVisitor);
		Node tree = treeVisitor.getNode();
			//tree.computeHashes();
		tree.show(0);
		
		//findDuplicates(makeNodeSet(tree));
		
		//FileWriter fw = new FileWriter("tree.htm");
		//tree.writeHtml(fw, 0);
        //fw.close();
		
		
        /*Node tree = createTree("N:\\_Video\\Game movies", null);
        
        //Node treeCopy = tree.copy();
        //tree.writexml(treesw);
        tree.show();*/
        
    }
 }
