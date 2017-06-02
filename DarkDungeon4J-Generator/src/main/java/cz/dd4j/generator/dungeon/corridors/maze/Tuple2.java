package cz.dd4j.generator.dungeon.corridors.maze;

public class Tuple2 {

	public final int x;
	public final int y;
	int hashCode;
	
	public Tuple2(int x, int y) {
		this.x = x;
		this.y = y;
		this.hashCode = x * 21 + y;
	}
	
	@Override
	public int hashCode() {
		return hashCode;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Tuple2)) return false;
		Tuple2 other = (Tuple2)obj;
		return x == other.x && y == other.y;
	}
	
}
