import java.io.IOException;
import java.io.File;
import java.nio.file.*;
import java.security.*;
import java.math.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import com.twmacinta.util.MD5;

public class PathHash implements Comparable<PathHash>{
	public String path;
	public String hash;
	
	public PathHash(String path, String hash) {
		this.path = path;
		this.hash = hash;
	}
	
	@Override
    public int compareTo(PathHash o) {
		return hash.compareTo(o.hash);
    }
}