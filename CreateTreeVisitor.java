import java.io.IOException;
import java.io.File;
import java.nio.file.*;
import java.security.*;
import java.math.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import com.twmacinta.util.MD5;

public class CreateTreeVisitor implements FileVisitor<Path> {
	Node current;
	//public int total;
	
	public Node getNode() {
		return current;
	}
	
	@Override
	public FileVisitResult visitFile(Path path,	BasicFileAttributes attrs) throws IOException {
		Node newNode = new Node(NodeType.FILE, path.getFileName().toString());
		newNode.path = path.toAbsolutePath().toString();
		newNode.hash = MD5.asHex(MD5.getHash(new File(path.toAbsolutePath().toString())));
		current.addChild(newNode);
		//System.out.println(++total);
		//if (total > 1000) return FileVisitResult.TERMINATE;
		return FileVisitResult.CONTINUE;
	}
	
	@Override
	public FileVisitResult visitFileFailed(Path path, IOException e) {
		System.err.println(e);
		return FileVisitResult.CONTINUE;
	}
	
	@Override
	public FileVisitResult preVisitDirectory(Path path,	BasicFileAttributes attrs) throws IOException {
		Node newNode = new Node(NodeType.FOLDER, path.getFileName().toString());
		newNode.path = path.toAbsolutePath().toString();
		if (current == null) {
			current = newNode;
		} else {
			current.addChild(newNode);
			current = newNode;
		}
		return FileVisitResult.CONTINUE;
	}
	
	@Override
	public FileVisitResult postVisitDirectory(Path path, IOException exc) throws IOException {
		if (current.parent != null) {
			current = current.parent;
		}
		return FileVisitResult.CONTINUE;
	}
}