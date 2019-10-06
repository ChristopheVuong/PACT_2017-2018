package viewer;

import java.awt.geom.Point2D;

public final class Windowing {

	private final float x;
	private final float y;
	private final float w;
	private final float h;

	public Windowing(final float x, final float y, final float w, final float h) {
		this.x = x;
		this.y = y;
		this.h = w;
		this.w = h;
	}

	public Point2D renderPoint(final Vertexf3D vertex) {
		if(vertex == null) {
			return new Point2D.Float(0.5f * w + x, 0.5f * h + y);
		}
		return new Point2D.Float(0.5f * w * (vertex.getX() + 1f) + x, 0.5f * h * (-vertex.getY() + 1f) + y);
	}
	
	public Vertexi2D renderVertex(final Vertexf3D vertex) {
		if(vertex == null) {
			return new Vertexi2D((int)(0.5f * w + x), (int)(0.5f * h + y));
		}
		return new Vertexi2D((int)(0.5f * w * (vertex.getX() + 1f) + x), (int)(0.5f * h * (-vertex.getY() + 1f) + y));
	}
}