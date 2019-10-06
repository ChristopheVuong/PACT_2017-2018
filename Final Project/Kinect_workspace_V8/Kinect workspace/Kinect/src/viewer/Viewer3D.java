package viewer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import utils.listeners.MouseListenerImplementation;
import utils.listeners.MouseMotionListenerImplementation;
import utils.listeners.MouseWheelListenerImplementation;

public final class Viewer3D extends Canvas implements ViewerInterface, ViewerListenerElements {

	private static final long serialVersionUID = 6390026539917345488L;
	
	private final Dimension size;
	private final ModelView modelview;
	private final Projection projection;
	private final Windowing windowing;
	private Graphics2D g2d;
	private final List<DrawInterface> draws;
	private final int w;
	private final int h;
	
	private boolean leftDragInitialized;
	private boolean rightDragInitialized;
	
	private boolean isInitialized;
	private BufferStrategy bufferStrategy;
	private BufferedImage offscreenBuffer;
	
	public Viewer3D() {
		this(10f,30f,90f,0f,0f,400,400);
	}

	public Viewer3D(final float near, final float far, final float fieldOfView, final float w, final float h) {
		this(near,far,fieldOfView,0f,0f,w,h);
	}

	public Viewer3D(float near, float far, final float fieldOfView, final float x, final float y, float w, float h) {
		super();
		if(near < 0f) {
			near = 0f;
		}
		if(far < 0f) {
			far = 0f;
		}
		if(far < near) {
			final float temp = far;
			far = near;
			near = temp;
		}
		if(w < 1f) {
			w = 1f;
		}
		if(h < 1f) {
			h = 1f;
		}
		this.w = (int)(w+0.5f);
		this.h = (int)(h+0.5f);
		setSize(this.w,this.h);
		size = new Dimension(this.w,this.h);
		modelview = new ModelView();
		modelview.translateZ((far+near)/2f);
		projection = new Projection(near,far,fieldOfView);
		//projection = new Projection(Projection.ORTHOGRAPHIC,near,far,15f,15f,-15f,-15f);
		windowing = new Windowing(x,y,w,h);
		draws = new ArrayList<DrawInterface>();
		addMouseMotionListener(new MouseMotionListenerImplementation(this));
		addMouseListener(new MouseListenerImplementation(this));
		addMouseWheelListener(new MouseWheelListenerImplementation(this));
		setIgnoreRepaint(true);
	}
	
	public void init(final Container container, final String constraints) {
		Window window = SwingUtilities.getWindowAncestor(container);
		container.add(this,constraints);
		if(window == null) {
			if(!container.isVisible()) {
				container.setVisible(true);
				init();
				container.setVisible(false);
			} else {
				init();
			}
		} else {
			if(!window.isVisible()) {
				window.setVisible(true);
				init();
				window.setVisible(false);
			} else {
				init();
			}
		}
	}
	
	public void init(final Container container) {
		Window window = SwingUtilities.getWindowAncestor(container);
		container.add(this);
		if(window == null) {
			if(!container.isVisible()) {
				container.setVisible(true);
				init();
				container.setVisible(false);
			} else {
				init();
			}
		} else {
			if(!window.isVisible()) {
				window.setVisible(true);
				init();
				window.setVisible(false);
			} else {
				init();
			}
		}
	}
	
	private void init() {
		createBufferStrategy(2);
		bufferStrategy = getBufferStrategy();
		offscreenBuffer = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(this.w,this.h);
		isInitialized = true;
	}

	@Override
	public void addDraws(final DrawInterface draw) {
		draws.add(draw);
	}
	
	@Override
	public void flush() {
		draws.clear();
	}

	@Override
	public ModelView getModelView() {
		return modelview;
	}

	@Override
	public Projection getProjection() {
		return projection;
	}

	@Override
	public Windowing getWindowing() {
		return windowing;
	}

	@Override
	public Graphics2D getGraphics2D() {
		return g2d;
	}
	
	@Override
	public Point2D getRenderingPoint(final Vertexf4D vertex) {
		return getWindowing().renderPoint(getProjection().normalize(getModelView().dot(vertex)));
	}
	
	@Override
	public Point2D getRenderingPointProjection(final Vertexf4D vertex) {
		return getWindowing().renderPoint(getProjection().normalize(vertex));
	}

	@Override
	public Vertexi2D getRenderingVertex(final Vertexf4D vertex) {
		return getWindowing().renderVertex(getProjection().normalize(getModelView().dot(vertex)));
	}
	
	@Override
	public Vertexi2D[] getRenderingVertex(final Vertexf4D[] vertices) {
		if(vertices == null) {
			return new Vertexi2D[0];
		}
		final int len = vertices.length;
		final Vertexi2D[] verticesFace = new Vertexi2D[len];
		for(int i = 0; i<len; i++) {
			verticesFace[i] = getWindowing().renderVertex(getProjection().normalize(getModelView().dot(vertices[i])));
		}
		return verticesFace;
	}
	
	@Override
	public Vertexi2D[] getRenderingVertex(final List<Vertexf4D> vertices) {
		if(vertices == null) {
			return new Vertexi2D[0];
		}
		final int len = vertices.size();
		final Vertexi2D[] verticesFace = new Vertexi2D[len];
		for(int i = 0; i<len; i++) {
			verticesFace[i] = getWindowing().renderVertex(getProjection().normalize(getModelView().dot(vertices.get(i))));
		}
		return verticesFace;
	}
	
	@Override
	public Vertexi2D getRenderingVertexProjection(final Vertexf4D vertex) {
		return getWindowing().renderVertex(getProjection().normalize(vertex));
	}
	
	@Override
	public Vertexi2D[] getRenderingVertexProjection(final Vertexf4D[] vertices) {
		if(vertices == null) {
			return new Vertexi2D[0];
		}
		final int len = vertices.length;
		final Vertexi2D[] verticesFace = new Vertexi2D[len];
		for(int i = 0; i<len; i++) {
			verticesFace[i] = getWindowing().renderVertex(getProjection().normalize(vertices[i]));
		}
		return verticesFace;
	}
	
	@Override
	public Vertexi2D[] getRenderingVertexProjection(final List<Vertexf4D> vertices) {
		if(vertices == null) {
			return new Vertexi2D[0];
		}
		final int len = vertices.size();
		final Vertexi2D[] verticesFace = new Vertexi2D[len];
		for(int i = 0; i<len; i++) {
			verticesFace[i] = getWindowing().renderVertex(getProjection().normalize(vertices.get(i)));
		}
		return verticesFace;
	}
	
	@Override
	public void updateDisplay(final Vertexf3D center) {
		if(isInitialized) {
			g2d = offscreenBuffer.createGraphics();
			g2d.setColor(Color.BLACK);
			g2d.fillRect(0,0,w,h);
			g2d.setColor(Color.WHITE);

			getModelView().translate(center.scaleKeep(-1f));
			final int len = draws.size();
			for(int i = 0; i<len; i++) {
				draws.get(i).draw(this);
			}
			getModelView().translate(center);
			
			Graphics graphics = bufferStrategy.getDrawGraphics();
			graphics.drawImage(offscreenBuffer, 0, 0, null);
			
			if(!bufferStrategy.contentsLost()) {
				bufferStrategy.show();
			}
		}
	}
	
	@Override
	public Dimension getPreferredSize() {
		return size;
	}
	
	@Override
	public Dimension getMaximumSize() {
		return size;
	}
	
	@Override
	public Dimension getMinimumSize() {
		return size;
	}

	@Override
	public void setLeftDragInitialized(final boolean leftDragInitialized) {
		this.leftDragInitialized = leftDragInitialized;
	}

	@Override
	public void setRightDragInitialized(final boolean rightDragInitialized) {
		this.rightDragInitialized = rightDragInitialized;
	}

	@Override
	public boolean getLeftDragInitialized() {
		return leftDragInitialized;
	}

	@Override
	public boolean getRightDragInitialized() {
		return rightDragInitialized;
	}
}