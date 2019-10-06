package viewer;

import java.util.Arrays;

public final class Vertexf3D implements Cloneable {

	private float[] vertex;

	public Vertexf3D() {
		Arrays.fill(vertex = new float[3], 0f);
	}

	public Vertexf3D(final float x, final float y, final float z) {
		vertex = new float[] {x, y, z};
	}

	public Vertexf3D(final float[] vertex) {
		setVertex(vertex);
	}
	
	public Vertexf3D(final float x, final float y) {
		vertex = new float[] {x, y, 0f};
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

	public float[] getVertex() {
		return vertex;
	}
	
	public void setVertex(final float[] vertex) {
		if(vertex == null || vertex.length != 3) {
			Arrays.fill(this.vertex = new float[3], 0f);
		} else {
			this.vertex = Arrays.copyOf(vertex, vertex.length);
		}
	}
	
	public Vertexf3D scale(final float scaleX, final float scaleY, final float scaleZ) {
		vertex[0] *= scaleX;
		vertex[1] *= scaleY;
		vertex[2] *= scaleZ;
		return this;
	}

	public Vertexf3D scaleKeep(final float scaleX, final float scaleY, final float scaleZ) {
		final float[] vertexTemp = new float[3];
		vertexTemp[0] = vertex[0] * scaleX;
		vertexTemp[1] = vertex[1] * scaleY;
		vertexTemp[2] = vertex[2] * scaleZ;
		return new Vertexf3D(vertexTemp);
	}

	public Vertexf3D scale(final float scale) {
		return scale(scale,scale,scale);
	}

	public Vertexf3D scaleKeep(final float scale) {
		return scaleKeep(scale,scale,scale);
	}
	
	public Vertexf3D normalize(final float norme) {
		return scale(norme/norme());
	}

	public Vertexf3D normalizeKeep(final float norme) {
		return scaleKeep(norme/norme());
	}
	
	public float norme() {
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
		final Vertexf3D other = (Vertexf3D) object;
		if(!Arrays.equals(vertex, other.getVertex())) {
			return false;
		}
		return true;
	}
	
	@Override
	public Vertexf3D clone() {
		return new Vertexf3D(vertex);
	}
}