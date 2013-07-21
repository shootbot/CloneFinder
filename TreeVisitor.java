import java.io.IOException;
import java.io.File;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.FileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import com.twmacinta.util.MD5;

public class TreeVisitor implements FileVisitor<Path> {
	private Node current = null;
	private MD5 md5 = new MD5();
	private int total = 0;

	public Node getNode() {
		return current;
	}

	public int getTotal() {
		return total;
	}

	public TreeVisitor() {
		//System.out.println("Computing hashes..");
	}
	
	private String getHash(String path) {
		File f = new File(path);
		if (f.isDirectory()) {
			byte[] folderHash = new byte[16];
			for (Node child : current.children) {
				try {
					byte[] childName = child.name.getBytes("UTF-8");
					for (int i = 0; i < Math.min(16, childName.length); i++) {
						folderHash[i] ^= childName[i];
					}
				} catch (Exception e) {
					System.out.println(e);
				}

				try {
					byte[] childHash = child.hash.getBytes("UTF-8");
					for (int i = 0; i < Math.min(16, childHash.length); i++) {
						folderHash[i] ^= childHash[i];
					}
				} catch (Exception e) {
					System.out.println(e);
				}

			}
			md5.Init();
			md5.Update(folderHash);
			return md5.asHex();
		} else {
			//MD5.asHex(MD5.getHash(new File(path.toAbsolutePath().toString())));
			return String.valueOf(f.length());
		}
	}

	@Override
	public FileVisitResult visitFile(Path path,	BasicFileAttributes attrs) throws IOException {
		Node newNode = new Node(NodeType.FILE, path.getFileName().toString());
		newNode.path = path.toAbsolutePath().toString();
		newNode.hash = getHash(newNode.path);
		current.addChild(newNode);
		System.out.print("\r" + ++total);
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
		current.hash = getHash(current.path);
		System.out.print("\r" + ++total);
		if (current.parent != null) {
			current = current.parent;
		} else {
			//System.out.println("\nDone");
		}		
		return FileVisitResult.CONTINUE;
	}
}