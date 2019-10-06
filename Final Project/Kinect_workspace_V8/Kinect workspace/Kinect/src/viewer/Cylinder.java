package viewer;

public final class Cylinder implements DrawInterface {

	private final float radius;
	private final int slices;
	private final Matrixf4D transformBase;
	private final Matrixf4D transformTop;
	
	public Cylinder(final Vertexf3D base, final Vertexf3D top, final float radius, final int slices) {
		if(base == null || top == null || top.equals(base)) {
			this.radius = 0f;
			this.slices = 0;
			transformBase = null;
			transformTop = null;
		} else {
			this.radius = radius;
			this.slices = slices;
			final float teta = (float)Math.atan2(top.getY() - base.getY(), top.getX() - base.getX());
			final float tetaDeg = teta * 180f / (float)Math.PI;
			final float phi = (float)Math.acos((top.getZ() - base.getZ()) / (Math.sqrt((top.getX() - base.getX()) * (top.getX() - base.getX()) + (top.getY() - base.getY()) * (top.getY() - base.getY()) + (top.getZ() - base.getZ()) * (top.getZ() - base.getZ()))));
			//final float phi = (float)Math.atan2(Math.sqrt((top.getX() - base.getX())*(top.getX() - base.getX()) + (top.getY() - base.getY())*(top.getY() - base.getY())), top.getZ() - base.getZ());
			final float phiDeg = phi * 180f / (float)Math.PI;
			final Matrixf4D rotate = Transformf.rotateZ(tetaDeg).dot(Transformf.rotateY(-phiDeg));
			transformBase = Transformf.translate(base).dotKeep(rotate);
			transformTop = Transformf.translate(top).dotKeep(rotate);
		}
	}
	
	public Cylinder(final Vertexf3D base, final Vertexf3D top, final float radius) {
		this(base,top,radius,16);
	}
	
	public Cylinder(final float heigth, final float radius, final int slices) {
		this(new Vertexf3D(),new Vertexf3D(0f,0f,heigth),radius,slices);
	}

	public Cylinder(final float heigth, final float radius) {
		this(heigth,radius,16);
	}

	public void draw(final ViewerInterface viewer) {
		if(viewer.getGraphics2D() != null) {
			if(slices > 1) {
				final Vertexf4D[] verticesBase4D = new Vertexf4D[slices + 1];
				final Vertexf4D[] verticesTop4D = new Vertexf4D[slices + 1];
				final double angleDelta = 2 * Math.PI / slices;
				double angle = 0;
				for(int i = 0; i < slices; i++) {
					angle = i * angleDelta;
					final Vertexf4D vertex = new Vertexf4D(radius*(float)Math.cos(angle), radius*(float)Math.sin(angle), 0f);
					verticesBase4D[slices-i] = viewer.getModelView().dot(transformBase.dotKeep(vertex));
					verticesTop4D[i+1] = viewer.getModelView().dot(transformTop.dotKeep(vertex));
				}
				verticesBase4D[0] = viewer.getModelView().dot(transformBase.dotKeep(new Vertexf4D()));
				verticesTop4D[0] = viewer.getModelView().dot(transformTop.dotKeep(new Vertexf4D()));
				
				if(viewer.getProjection().isClipped(verticesBase4D) && viewer.getProjection().isClipped(verticesTop4D)) {
					final Vertexi2D[] verticesBase = viewer.getRenderingVertexProjection(verticesBase4D);
					final Vertexi2D[] verticesTop = viewer.getRenderingVertexProjection(verticesTop4D);
					
					for(int i = 0; i < slices - 1; i++) {
						if(!FaceCulling.isCullable(new Vertexi2D[] {verticesBase[slices-i],verticesBase[slices-i-1],verticesTop[i+2]})) {
							final int xPoly[] = new int[4];
							final int yPoly[] = new int[4];
							xPoly[0] = verticesBase[slices-i].getX();
							yPoly[0] = verticesBase[slices-i].getY();
							xPoly[1] = verticesBase[slices-i-1].getX();
							yPoly[1] = verticesBase[slices-i-1].getY();
							xPoly[2] = verticesTop[i+2].getX();
							yPoly[2] = verticesTop[i+2].getY();
							xPoly[3] = verticesTop[i+1].getX();
							yPoly[3] = verticesTop[i+1].getY();
							viewer.getGraphics2D().fillPolygon(xPoly,yPoly,4);
						}
					}
					if(!FaceCulling.isCullable(new Vertexi2D[] {verticesBase[1],verticesBase[slices],verticesTop[1]})) {
						final int xPoly[] = new int[4];
						final int yPoly[] = new int[4];
						xPoly[0] = verticesBase[1].getX();
						yPoly[0] = verticesBase[1].getY();
						xPoly[1] = verticesBase[slices].getX();
						yPoly[1] = verticesBase[slices].getY();
						xPoly[2] = verticesTop[1].getX();
						yPoly[2] = verticesTop[1].getY();
						xPoly[3] = verticesTop[slices].getX();
						yPoly[3] = verticesTop[slices].getY();
						viewer.getGraphics2D().fillPolygon(xPoly,yPoly,4);
					}
					new TriangleFan(verticesBase,true,true,true).draw(viewer);
					new TriangleFan(verticesTop,true,true,true).draw(viewer);
				}
			}
		}
	}
}