


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
	
	@Override
	public String toString() {
		return path;
	}
}