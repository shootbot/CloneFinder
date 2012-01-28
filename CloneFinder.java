import java.io.IOException;
import java.nio.file.*;
import java.security.*;
import java.math.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

enum NodeType {
    FOLDER, FILE;
}

 // Node property: only first pool, only second pool, equal in both pools, not equal
enum Uniqueness {
    O1, O2, EQ, NE;
}

class CloneFinder {
	public static ArrayList<PathHash> makeSet(Node tree) {
		ArrayList<PathHash> set = new ArrayList<PathHash>();
		runThrough(tree, set);
		return set;
	}
		
	public static void runThrough(Node tree, ArrayList<PathHash> set) {
		if (tree.type == NodeType.FOLDER) {
			for (Node child : tree.children) {
				runThrough(child, set);
			}
		} 
		PathHash ph = new PathHash(tree.path, tree.hash);
		set.add(ph);
		//System.out.println("runThrough: " + ph);
	}
	
	public static void findDuplicates(ArrayList<PathHash> set) {
		Collections.sort(set);
		PathHash phPrev = null, phCur;
		Iterator<PathHash> i = set.iterator();
		while (i.hasNext()) {
			phCur = i.next();
			if (phPrev == null) {
				phPrev = phCur;
				continue;
			} else {
				if (phCur.hash.compareTo(phPrev.hash) == 0) {
					System.out.println("Duplicate: \n" + phPrev.toString() + "\n" + phCur.toString());
				}
				phPrev = phCur;
			}
			
		}
	}
		
    public static void main(String[] args) throws IOException {

		CreateTreeVisitor treeVisitor = new CreateTreeVisitor();
		String path;
		if (args.length == 0) {
			path = "C:\\_Games\\Counter-Strike\\AntiCheats";
		} else {
			path = args[0];
		}
		Files.walkFileTree(Paths.get(path), treeVisitor);
		Node tree = treeVisitor.getNode();
		tree.computeHashes();
		tree.show(0);
		
		findDuplicates(makeSet(tree));
		
		
		
		
        /*Node tree = createTree("N:\\_Video\\Game movies", null);
        FileStream treefs = new FileStream("tree.xml", FileMode.Create);
        StreamWriter treesw = new StreamWriter(treefs);
        //Node treeCopy = tree.copy();
        //tree.writexml(treesw);
        tree.show();
        treesw.Close();*/
    }
 }
