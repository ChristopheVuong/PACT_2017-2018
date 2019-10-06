package viewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class TriangleFan implements DrawInterface {

	private List<Vertexf4D> vertices;
	private Vertexi2D[] vertices2D;
	private boolean cullable;
	private boolean samePlan;
	private boolean isClosed;

	public TriangleFan(final Vertexi2D[] vertices2D, final boolean cullable, final boolean samePlan, final boolean isClosed) {
		this.cullable = cullable;
		this.samePlan = samePlan;
		this.isClosed = isClosed;
		if(vertices2D == null) {
			this.vertices = new ArrayList<Vertexf4D>();
		} else {
			boolean error = false;
			final int len = vertices2D.length;
			for(int i = 0; i < len; i++) {
				if(vertices2D[i] == null) {
					error = true;
					break;
				}
			}
			if(error) {
				this.vertices = new ArrayList<Vertexf4D>();
			} else {
				this.vertices2D = vertices2D;
			}
		}
	}
	
	public TriangleFan(final Vertexi2D[] vertices2D, final boolean cullable, final boolean samePlan) {
		this(vertices2D,cullable,false,false);
	}
	
	public TriangleFan(final Vertexi2D[] vertices2D, final boolean cullable) {
		this(vertices2D,cullable,false);
	}
	
	public TriangleFan(final Vertexi2D[] vertices2D) {
		this(vertices2D,false);
	}
	
	public TriangleFan() {
		this((Vertexi2D[])null);
	}

	public TriangleFan(final Vertexf4D[] vertices) {
		if(vertices == null) {
			this.vertices = new ArrayList<Vertexf4D>();
		} else {
			boolean error = false;
			final int len = vertices.length;
			for(int i = 0; i < len; i++) {
				if(vertices[i] == null) {
					error = true;
					break;
				}
			}
			if(error) {
				this.vertices = new ArrayList<Vertexf4D>();
			} else {
				this.vertices = new ArrayList<Vertexf4D>(Arrays.asList(vertices));
			}
		}
	}

	public TriangleFan(final Vertexf3D[] vertices) {
		if(vertices == null) {
			this.vertices = new ArrayList<Vertexf4D>();
		} else {
			boolean error = false;
			final int len = vertices.length;
			for(int i = 0; i < len; i++) {
				if(vertices[i] == null) {
					error = true;
					break;
				}
			}
			if(error) {
				this.vertices = new ArrayList<Vertexf4D>();
			} else {
				this.vertices = new ArrayList<Vertexf4D>();
				for(int i = 0; i < len; i++) {
					this.vertices.add(new Vertexf4D(vertices[i]));
				}
			}
		}
	}

	public TriangleFan(final List<Vertexf4D> vertices) {
		if(vertices == null) {
			this.vertices = new ArrayList<Vertexf4D>();
		} else {
			boolean error = false;
			final int len = vertices.size();
			for(int i = 0; i < len; i++) {
				if(vertices.get(i) == null) {
					error = true;
					break;
				}
			}
			if(error) {
				this.vertices = new ArrayList<Vertexf4D>();
			} else {
				this.vertices = vertices;
			}
		}
	}

	public TriangleFan(final List<Vertexf3D> vertices, final boolean flag3D) {
		if(vertices == null) {
			this.vertices = new ArrayList<Vertexf4D>();
		} else {
			boolean error = false;
			final int len = vertices.size();
			for(int i = 0; i < len; i++) {
				if(vertices.get(i) == null) {
					error = true;
					break;
				}
			}
			if(error) {
				this.vertices = new ArrayList<Vertexf4D>();
			} else {
				this.vertices = new ArrayList<Vertexf4D>();
				for(int i = 0; i < len; i++) {
					this.vertices.add(new Vertexf4D(vertices.get(i)));
				}
			}
		}
	}

	public void add(final Vertexf4D vertex) {
		if(vertices != null) {
			if(vertex != null) {
				vertices.add(vertex);
			}
		}
	}

	public void add(final Vertexf3D vertex) {
		if(vertices != null) {
			if(vertex != null) {
				vertices.add(new Vertexf4D(vertex));
			}
		}
	}

	public void add(final Vertexf4D[] vertices) {
		if(this.vertices != null) {
			boolean error = false;
			final int len = vertices.length;
			for(int i = 0; i < len; i++) {
				if(vertices[i] == null) {
					error = true;
					break;
				}
			}
			if(!error) {
				this.vertices.addAll(Arrays.asList(vertices));
			}
		}
	}

	public void add(final Vertexf3D[] vertices) {
		if(this.vertices != null) {
			boolean error = false;
			final int len = vertices.length;
			for(int i = 0; i < len; i++) {
				if(vertices[i] == null) {
					error = true;
					break;
				}
			}
			if(!error) {
				for(int i = 0; i < len; i++) {
					this.vertices.add(new Vertexf4D(vertices[i]));
				}
			}
		}
	}

	public void add(final List<Vertexf4D> vertices) {
		if(this.vertices != null) {
			boolean error = false;
			final int len = vertices.size();
			for(int i = 0; i < len; i++) {
				if(vertices.get(i) == null) {
					error = true;
					break;
				}
			}
			if(!error) {
				this.vertices.addAll(vertices);
			}
		}
	}

	public void add(final List<Vertexf3D> vertices, final boolean flag3D) {
		if(this.vertices != null) {
			boolean error = false;
			final int len = vertices.size();
			for(int i = 0; i < len; i++) {
				if(vertices.get(i) == null) {
					error = true;
					break;
				}
			}
			if(!error) {
				for(int i = 0; i < len; i++) {
					this.vertices.add(new Vertexf4D(vertices.get(i)));
				}
			}
		}
	}

	public void draw(final ViewerInterface viewer) {
		if(viewer.getGraphics2D() != null) {
			if(vertices != null) {
				final int len = vertices.size();
				if(len > 2) {
					final List<Vertexf4D> vertices4D = viewer.getModelView().dot(vertices);
					if(viewer.getProjection().isClipped(vertices4D)) {
						final Vertexi2D[] vertices2D = viewer.getRenderingVertexProjection(vertices);
						for(int i = 0; i < len - 2; i++) {
							final int xPoly[] = new int[3];
							final int yPoly[] = new int[3];
							xPoly[0] = vertices2D[0].getX();
							yPoly[0] = vertices2D[0].getY();
							xPoly[1] = vertices2D[i+1].getX();
							yPoly[1] = vertices2D[i+1].getY();
							xPoly[2] = vertices2D[i+2].getX();
							yPoly[2] = vertices2D[i+2].getY();
							viewer.getGraphics2D().fillPolygon(xPoly,yPoly,3);
						}
					}
				}
			}
			if(vertices2D != null) {
				final int len = vertices2D.length;
				if(len > 2) {
					if(isClosed) {
						if(cullable) {
							if(samePlan) {
								if(!FaceCulling.isCullable(new Vertexi2D[] {vertices2D[0],vertices2D[1],vertices2D[2]})) {
									for(int i = 0; i < len - 2; i++) {
										final int xPoly[] = new int[3];
										final int yPoly[] = new int[3];
										xPoly[0] = vertices2D[0].getX();
										yPoly[0] = vertices2D[0].getY();
										xPoly[1] = vertices2D[i+1].getX();
										yPoly[1] = vertices2D[i+1].getY();
										xPoly[2] = vertices2D[i+2].getX();
										yPoly[2] = vertices2D[i+2].getY();
										viewer.getGraphics2D().fillPolygon(xPoly,yPoly,3);
									}
									final int xPoly[] = new int[3];
									final int yPoly[] = new int[3];
									xPoly[0] = vertices2D[0].getX();
									yPoly[0] = vertices2D[0].getY();
									xPoly[1] = vertices2D[len-1].getX();
									yPoly[1] = vertices2D[len-1].getY();
									xPoly[2] = vertices2D[1].getX();
									yPoly[2] = vertices2D[1].getY();
									viewer.getGraphics2D().fillPolygon(xPoly,yPoly,3);
								}
							} else {
								for(int i = 0; i < len - 2; i++) {
									if(!FaceCulling.isCullable(new Vertexi2D[] {vertices2D[0],vertices2D[i+1],vertices2D[i+2]})) {
										final int xPoly[] = new int[3];
										final int yPoly[] = new int[3];
										xPoly[0] = vertices2D[0].getX();
										yPoly[0] = vertices2D[0].getY();
										xPoly[1] = vertices2D[i+1].getX();
										yPoly[1] = vertices2D[i+1].getY();
										xPoly[2] = vertices2D[i+2].getX();
										yPoly[2] = vertices2D[i+2].getY();
										viewer.getGraphics2D().fillPolygon(xPoly,yPoly,3);
									}
								}
								final int xPoly[] = new int[3];
								final int yPoly[] = new int[3];
								xPoly[0] = vertices2D[0].getX();
								yPoly[0] = vertices2D[0].getY();
								xPoly[1] = vertices2D[len-1].getX();
								yPoly[1] = vertices2D[len-1].getY();
								xPoly[2] = vertices2D[1].getX();
								yPoly[2] = vertices2D[1].getY();
								viewer.getGraphics2D().fillPolygon(xPoly,yPoly,3);
							}
						} else {
							for(int i = 0; i < len - 2; i++) {
								final int xPoly[] = new int[3];
								final int yPoly[] = new int[3];
								xPoly[0] = vertices2D[0].getX();
								yPoly[0] = vertices2D[0].getY();
								xPoly[1] = vertices2D[i+1].getX();
								yPoly[1] = vertices2D[i+1].getY();
								xPoly[2] = vertices2D[i+2].getX();
								yPoly[2] = vertices2D[i+2].getY();
								viewer.getGraphics2D().fillPolygon(xPoly,yPoly,3);
							}
							final int xPoly[] = new int[3];
							final int yPoly[] = new int[3];
							xPoly[0] = vertices2D[0].getX();
							yPoly[0] = vertices2D[0].getY();
							xPoly[1] = vertices2D[len-1].getX();
							yPoly[1] = vertices2D[len-1].getY();
							xPoly[2] = vertices2D[1].getX();
							yPoly[2] = vertices2D[1].getY();
							viewer.getGraphics2D().fillPolygon(xPoly,yPoly,3);
						}
					} else {
						if(cullable) {
							if(samePlan) {
								if(!FaceCulling.isCullable(new Vertexi2D[] {vertices2D[0],vertices2D[1],vertices2D[2]})) {
									for(int i = 0; i < len - 2; i++) {
										final int xPoly[] = new int[3];
										final int yPoly[] = new int[3];
										xPoly[0] = vertices2D[0].getX();
										yPoly[0] = vertices2D[0].getY();
										xPoly[1] = vertices2D[i+1].getX();
										yPoly[1] = vertices2D[i+1].getY();
										xPoly[2] = vertices2D[i+2].getX();
										yPoly[2] = vertices2D[i+2].getY();
										viewer.getGraphics2D().fillPolygon(xPoly,yPoly,3);
									}
								}
							} else {
								for(int i = 0; i < len - 2; i++) {
									if(!FaceCulling.isCullable(new Vertexi2D[] {vertices2D[0],vertices2D[i+1],vertices2D[i+2]})) {
										final int xPoly[] = new int[3];
										final int yPoly[] = new int[3];
										xPoly[0] = vertices2D[0].getX();
										yPoly[0] = vertices2D[0].getY();
										xPoly[1] = vertices2D[i+1].getX();
										yPoly[1] = vertices2D[i+1].getY();
										xPoly[2] = vertices2D[i+2].getX();
										yPoly[2] = vertices2D[i+2].getY();
										viewer.getGraphics2D().fillPolygon(xPoly,yPoly,3);
									}
								}
							}
						} else {
							for(int i = 0; i < len - 2; i++) {
								final int xPoly[] = new int[3];
								final int yPoly[] = new int[3];
								xPoly[0] = vertices2D[0].getX();
								yPoly[0] = vertices2D[0].getY();
								xPoly[1] = vertices2D[i+1].getX();
								yPoly[1] = vertices2D[i+1].getY();
								xPoly[2] = vertices2D[i+2].getX();
								yPoly[2] = vertices2D[i+2].getY();
								viewer.getGraphics2D().fillPolygon(xPoly,yPoly,3);
							}
						}
					}
				}
			}
		}
	}
}