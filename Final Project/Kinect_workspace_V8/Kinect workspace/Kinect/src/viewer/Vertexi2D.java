package viewer;

import java.util.Arrays;

public final class Vertexi2D implements Cloneable {
	
	private final int[] vertex;

	public Vertexi2D() {
		Arrays.fill(vertex = new int[2], 0);
	}

	public Vertexi2D(final int x, final int y) {
		vertex = new int[] {x, y};
	}

	public int getX() {
		return vertex[0];
	}

	public int getY() {
		return vertex[1];
	}

	public int[] getVertex() {
		return vertex;
	}
	
	@Override
	public Vertexi2D clone() {
		return new Vertexi2D(vertex[0],vertex[1]);
	}
}