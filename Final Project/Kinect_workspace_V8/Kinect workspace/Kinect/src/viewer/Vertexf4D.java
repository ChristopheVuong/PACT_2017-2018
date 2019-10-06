package viewer;

import java.util.Arrays;

public final class Vertexf4D implements Cloneable {

	private float[] vertex;

	public Vertexf4D() {
		vertex = new float[] {0f, 0f, 0f, 1f};
	}

	public Vertexf4D(final float x, final float y, final float z, final float w) {
		vertex = new float[] {x, y, z, w};
	}

	public Vertexf4D(final Vertexf3D vertex3D, final float w) {
		if(vertex3D == null) {
			vertex = new float[] {0f, 0f, 0f, w};
		} else {
			vertex = new float[] {vertex3D.getX(), vertex3D.getY(), vertex3D.getZ(), w};
		}
	}

	public Vertexf4D(final float[] vertex) {
		setVertex(vertex);
	}

	public Vertexf4D(final float x, final float y, final float z) {
		vertex = new float[] {x, y, z, 1f};
	}

	public Vertexf4D(final Vertexf3D vertex3D) {
		if(vertex3D == null) {
			vertex = new float[] {0f, 0f, 0f, 1f};
		} else {
			vertex = new float[] {vertex3D.getX(), vertex3D.getY(), vertex3D.getZ(), 1f};
		}
	}
	
	public Vertexf4D(final float x, final float y) {
		vertex = new float[] {x, y, 0f, 1f};
	}

	public float getX() {
		return vertex[0];
	}

	public float getY() {
		return vertex[1];
	}

	public float getZ() {
		return vertex[2];
	}

	public float getW() {
		return vertex[3];
	}

	public float[] getVertex() {
		return vertex;
	}
	
	public void setVertex(final float[] vertex) {
		if(vertex == null || vertex.length != 4) {
			Arrays.fill(this.vertex = new float[4], 0f);
		} else {
			this.vertex = Arrays.copyOf(vertex, vertex.length);
		}
	}

	public Vertexf3D to3D() {
		return new Vertexf3D(getX(), getY(), getZ());
	}
	
	public Vertexf4D scale(final float scaleX, final float scaleY, final float scaleZ) {
		vertex[0] *= scaleX;
		vertex[1] *= scaleY;
		vertex[2] *= scaleZ;
		return this;
	}

	public Vertexf4D scaleKeep(final float scaleX, final float scaleY, final float scaleZ) {
		final float[] vertexTemp = new float[3];
		vertexTemp[0] = vertex[0] * scaleX;
		vertexTemp[1] = vertex[1] * scaleY;
		vertexTemp[2] = vertex[2] * scaleZ;
		vertexTemp[3] = vertex[3];
		return new Vertexf4D(vertexTemp);
	}

	public Vertexf4D scale(final float scale) {
		return scale(scale,scale,scale);
	}

	public Vertexf4D scaleKeep(final float scale) {
		return scaleKeep(scale,scale,scale);
	}
	
	public Vertexf4D normalize(final float norme) {
		return scale(norme/norme3D());
	}

	public Vertexf4D normalizeKeep(final float norme) {
		return scaleKeep(norme/norme3D());
	}
	
	public float norme3D() {
		return (float)Math.sqrt(vertex[0]*vertex[0]+vertex[1]*vertex[1]+vertex[2]*vertex[2]);
	}

	@Override
	public boolean equals(final Object object) {
		if(this == object) {
			return true;
		}
		if(object == null) {
			return false;
		}
		if(getClass() != object.getClass()) {
			return false;
		}
		final Vertexf4D other = (Vertexf4D) object;
		if(!Arrays.equals(vertex, other.getVertex())) {
			return false;
		}
		return true;
	}
	
	@Override
	public Vertexf4D clone() {
		return new Vertexf4D(vertex);
	}
}