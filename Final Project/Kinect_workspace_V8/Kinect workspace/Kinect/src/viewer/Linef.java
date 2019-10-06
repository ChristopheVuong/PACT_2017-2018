package viewer;

import java.awt.geom.Line2D;

public final class Linef implements DrawInterface {

	private final Vertexf4D origin;
	private final Vertexf4D destination;

	public Linef(final Vertexf3D origin, final Vertexf3D destination) {
		if(origin == null || destination == null) {
			this.origin = new Vertexf4D();
			this.destination = new Vertexf4D();
		} else {
			this.origin = new Vertexf4D(origin);
			this.destination = new Vertexf4D(destination);
		}
	}

	public Vertexf4D getOrigin() {
		return origin;
	}

	public Vertexf4D getDestination() {
		return destination;
	}

	@Override
	public void draw(final ViewerInterface viewer) {
		if(viewer.getGraphics2D() != null) {
			final Vertexf4D originModel = viewer.getModelView().dot(origin);
			final Vertexf4D destinationModel = viewer.getModelView().dot(destination);
			if(viewer.getProjection().isClipped(originModel) && viewer.getProjection().isClipped(destinationModel)) {
				viewer.getGraphics2D().draw(new Line2D.Float(viewer.getRenderingPointProjection(originModel), viewer.getRenderingPointProjection(destinationModel)));
			}
		}
	}
}