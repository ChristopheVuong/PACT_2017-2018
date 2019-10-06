package viewer;

import java.util.List;

public final class Projection {

	public static final int PERSPECTIVE = 0;
	public static final int ORTHOGRAPHIC = 1;
	
	private boolean isPerspective;
	private boolean isOrthographic;
	
	private final float near;
	private final float far;
	private final float top;
	private final float right;
	private final float bottom;
	private final float left;

	private final Matrixf4D projection;

	public Projection(final float near, final float far, final float fieldOfView) {
		this(PERSPECTIVE, near, far, (float)Math.tan(fieldOfView * (float)Math.PI/360f) * near, (float)Math.tan(fieldOfView * (float)Math.PI/360f) * near, -(float)Math.tan(fieldOfView * (float)Math.PI/360f) * near, -(float)Math.tan(fieldOfView * (float)Math.PI/360f) * near);
	}

	public Projection(final float near, final float far, final float top, final float right, final float bottom, final float left) {
		this(PERSPECTIVE, near, far, top, left, bottom, right);
	}

	public Projection(final int type, float near, float far, final float top, final float right, final float bottom, final float left) {
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
		this.near = near;
		this.far = far;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.left = left;
		if(type == PERSPECTIVE) {
			isPerspective = true;
			projection = initPerspective(near, far, top, right, bottom, left);
		} else if(type == ORTHOGRAPHIC) {
			isOrthographic = true;
			projection = initOrthographic(near, far, top, right, bottom, left);
		} else {
			projection = null;
		}
	}
	
	public boolean isClipped(final Vertexf4D vertex) {
		if(isPerspective) {
			return vertex.getZ() >= near && vertex.getZ() <= far && vertex.getX()/vertex.getZ() >= left/near && vertex.getX()/vertex.getZ() <= right/near && vertex.getY()/vertex.getZ() >= bottom/near && vertex.getY()/vertex.getZ() <= top/near;
		} else if(isOrthographic) {
			return vertex.getZ() >= near && vertex.getZ() <= far && vertex.getX() >= left && vertex.getX() <= right && vertex.getY() >= bottom && vertex.getY() <= top;
		} else {
			return false;
		}
	}
	
	public boolean isClipped(final Vertexf4D[] vertices) {
		if(vertices == null) {
			return false;
		}
		for(final Vertexf4D vertex : vertices) {
			if(!isClipped(vertex)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isClipped(final List<Vertexf4D> vertices) {
		if(vertices == null) {
			return false;
		}
		for(final Vertexf4D vertex : vertices) {
			if(!isClipped(vertex)) {
				return false;
			}
		}
		return true;
	}

	public Vertexf4D clip(final Vertexf4D vertex) {
		if(vertex == null) {
			return new Vertexf4D();
		}
		return projection.dotKeep(vertex);
	}
	
	public Vertexf3D normalize(final Vertexf4D vertex) {
		if(vertex == null) {
			return new Vertexf3D();
		}
		final Vertexf4D vertexTemp = projection.dotKeep(vertex);
		final Vertexf3D vertex3D = vertexTemp.to3D();
		vertex3D.scale(-1f/vertexTemp.getW(),-1f/vertexTemp.getW(),1f);
		return vertex3D;
	}

	private static Matrixf4D initPerspective(final float near, final float far, final float top, final float right, final float bottom, final float left) {
		final Matrixf4D projection = new Matrixf4D();
		final float[][] projectionBuild = projection.getMatrix();
		projectionBuild[0][0] = 2 * near / (right - left);
		projectionBuild[0][2] = (right + left) / (right - left);
		projectionBuild[1][1] = 2 * near / (top - bottom);
		projectionBuild[1][2] = (top + bottom) / (top - bottom);
		projectionBuild[2][2] = (far + near) / (near - far);
		projectionBuild[2][3] = 2 * far * near / (near - far);
		projectionBuild[3][2] = -1;
		return projection;
	}
	
	private static Matrixf4D initOrthographic(final float near, final float far, final float top, final float right, final float bottom, final float left) {
		final Matrixf4D projection = new Matrixf4D();
		final float[][] projectionBuild = projection.getMatrix();
		projectionBuild[0][0] = 2 / (left - right);
		projectionBuild[0][3] = (right + left) / (right - left);
		projectionBuild[1][1] = 2 / (bottom - top);
		projectionBuild[1][3] = (top + bottom) / (top - bottom);
		projectionBuild[2][2] = 2 / (near - far);
		projectionBuild[2][3] = (far + near) / (near - far);
		projectionBuild[3][3] = 1;
		return projection;
	}
}