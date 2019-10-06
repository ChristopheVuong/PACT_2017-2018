package viewer;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.List;

public interface ViewerInterface {
	public void addDraws(DrawInterface draw);
	public void flush();
	public ModelView getModelView();
	public Projection getProjection();
	public Windowing getWindowing();
	public Graphics2D getGraphics2D();
	public Point2D getRenderingPoint(Vertexf4D vertex);
	public Point2D getRenderingPointProjection(Vertexf4D vertex);
	public Vertexi2D getRenderingVertex(Vertexf4D vertex);
	public Vertexi2D[] getRenderingVertex(Vertexf4D[] vertices);
	public Vertexi2D[] getRenderingVertex(List<Vertexf4D> vertices);
	public Vertexi2D getRenderingVertexProjection(Vertexf4D vertex);
	public Vertexi2D[] getRenderingVertexProjection(Vertexf4D[] vertices);
	public Vertexi2D[] getRenderingVertexProjection(List<Vertexf4D> vertices);
	public void updateDisplay(Vertexf3D center);
}