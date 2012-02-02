import java.io.IOException;
//import java.io.File;
import java.nio.file.*;
//import java.security.*;
//import java.math.*;
import java.nio.file.attribute.BasicFileAttributes;
//import java.util.*;
//import com.twmacinta.util.MD5;

public class CounterVisitor implements FileVisitor<Path> {
	private int total = 0;
	
	public int getTotal() {
		return total;
	}
	
	@Override
	public FileVisitResult visitFile(Path path,	BasicFileAttributes attrs) throws IOException {
		total++;//System.out.print("\r" + ++total);
		return FileVisitResult.CONTINUE;
	}
	
	@Override
	public FileVisitResult visitFileFailed(Path path, IOException e) {
		System.err.println(e);
		return FileVisitResult.CONTINUE;
	}
	
	@Override
	public FileVisitResult preVisitDirectory(Path path,	BasicFileAttributes attrs) throws IOException {
		return FileVisitResult.CONTINUE;
	}
	
	@Override
	public FileVisitResult postVisitDirectory(Path path, IOException exc) throws IOException {
		total++;//System.out.print("\r" + ++total);
		return FileVisitResult.CONTINUE;
	}
}