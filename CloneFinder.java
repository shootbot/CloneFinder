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
	static class PrintVisitor implements FileVisitor<Path> {
		int depth = 0;
		
		@Override
		public FileVisitResult visitFile(Path file,	BasicFileAttributes attrs) throws IOException {
			for (int i = 0; i < depth; i++) System.out.print(" ");
			System.out.println(file.getFileName());
			return FileVisitResult.CONTINUE;
		}
		
		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) {
			System.err.println(exc);
			return FileVisitResult.CONTINUE;
		}
		
		@Override
		public FileVisitResult preVisitDirectory(Path file,	BasicFileAttributes attrs) throws IOException {
			for (int i = 0; i < depth; i++) {
				System.out.print(" ");
			}
			System.out.println(file.getFileName());
			depth++;
			return FileVisitResult.CONTINUE;
		}
		
		@Override
		public FileVisitResult postVisitDirectory(Path file, IOException exc) throws IOException {
			depth--;
			return FileVisitResult.CONTINUE;
		}
	}

    public static void main(String[] args) throws IOException {

		CreateTreeVisitor treeVisitor = new CreateTreeVisitor();
		Files.walkFileTree(Paths.get("d:\\_music"), treeVisitor);
		//treeVisitor.current.show(0);
		
        /*Node tree = createTree("N:\\_Video\\Game movies", null);
        FileStream treefs = new FileStream("tree.xml", FileMode.Create);
        StreamWriter treesw = new StreamWriter(treefs);
        //Node treeCopy = tree.copy();
        //tree.writexml(treesw);
        tree.show();
        treesw.Close();*/
    }
 }
