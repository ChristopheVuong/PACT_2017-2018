package viewer;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public final class Matrixf4D {

	private float[][] matrix;

	public Matrixf4D() {
		matrix = new float[4][4];
		for(int i = 0; i<4; i++) {
			Arrays.fill(matrix[i], 0f);
		}
	}

	public Matrixf4D(final Vertexf4D L0, final Vertexf4D L1, final Vertexf4D L2, final Vertexf4D L3) {
		if(L0 == null || L1 == null || L2 == null || L3 == null) {
			matrix = new float[4][4];
			for(int i = 0; i<4; i++) {
				Arrays.fill(matrix[i], 0f);
			}
		} else {
			matrix = new float[][] {L0.getVertex(), L1.getVertex(), L2.getVertex(), L3.getVertex()};
		}
	}

	public Matrixf4D(final float[][] matrix) {
		if(matrix == null || matrix.length != 4 || matrix[0].length != 4 || matrix[1].length != 4 || matrix[2].length != 4 || matrix[3].length != 4) {
			this.matrix = new float[4][4];
			for(int i = 0; i<4; i++) {
				Arrays.fill(this.matrix[i], 0f);
			}
		} else {
			this.matrix = matrix;
		}
	}

	public float[][] getMatrix() {
		return matrix;
	}
	
	public float det() {
		return matrix[0][0]*(matrix[1][1]*matrix[2][2]*matrix[3][3] + matrix[1][2]*matrix[2][3]*matrix[3][1] + matrix[1][3]*matrix[2][1]*matrix[3][2] - matrix[1][3]*matrix[2][2]*matrix[3][1] - matrix[1][2]*matrix[2][1]*matrix[3][3] - matrix[1][1]*matrix[2][3]*matrix[3][2])
		- matrix[0][1]*(matrix[1][0]*matrix[2][2]*matrix[3][3] + matrix[1][2]*matrix[2][3]*matrix[3][0] + matrix[1][3]*matrix[2][0]*matrix[3][2] - matrix[1][3]*matrix[2][2]*matrix[3][0] - matrix[1][2]*matrix[2][0]*matrix[3][3] - matrix[1][0]*matrix[2][3]*matrix[3][2])
		+ matrix[0][2]*(matrix[1][0]*matrix[2][1]*matrix[3][3] + matrix[1][1]*matrix[2][3]*matrix[3][0] + matrix[1][3]*matrix[2][0]*matrix[3][1] - matrix[1][3]*matrix[2][1]*matrix[3][0] - matrix[1][1]*matrix[2][0]*matrix[3][3] - matrix[1][0]*matrix[2][3]*matrix[3][1])
		- matrix[0][3]*(matrix[1][0]*matrix[2][1]*matrix[3][2] + matrix[1][1]*matrix[2][2]*matrix[3][0] + matrix[1][2]*matrix[2][0]*matrix[3][1] - matrix[1][2]*matrix[2][1]*matrix[3][0] - matrix[1][1]*matrix[2][0]*matrix[3][2] - matrix[1][0]*matrix[2][2]*matrix[3][1]);
	}

	public Matrixf4D dot(final Matrixf4D matrixObject) {
		if(matrixObject == null) {
			return new Matrixf4D();
		}
		final float[][] matrixTemp = new float[4][4];
		final float[][] matrix2 = matrixObject.getMatrix();
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				matrixTemp[i][j] = sum(i, j, matrix, matrix2);
			}
		}
		matrix = matrixTemp;
		return this;
	}

	public Matrixf4D dotKeep(final Matrixf4D matrixObject) {
		if(matrixObject == null) {
			return new Matrixf4D();
		}
		final float[][] matrixTemp = new float[4][4];
		final float[][] matrix2 = matrixObject.getMatrix();
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				matrixTemp[i][j] = sum(i, j, matrix, matrix2);
			}
		}
		return new Matrixf4D(matrixTemp);
	}

	public Vertexf4D dot(final Vertexf4D vertexObject) {
		if(vertexObject == null) {
			return new Vertexf4D();
		}
		final float[] vertexTemp = new float[4];
		float[] vertex = vertexObject.getVertex();
		for(int i = 0; i < 4; i++) {
			vertexTemp[i] = sum(i, matrix, vertex);
		}
		vertexObject.setVertex(vertexTemp);
		return vertexObject;
	}

	public Vertexf4D dotKeep(final Vertexf4D vertexObject) {
		if(vertexObject == null) {
			return new Vertexf4D();
		}
		final float[] vertexTemp = new float[4];
		final float[] vertex = vertexObject.getVertex();
		for(int i = 0; i < 4; i++) {
			vertexTemp[i] = sum(i, matrix, vertex);
		}
		return new Vertexf4D(vertexTemp);
	}
	
	public Vertexf4D[] dot(final Vertexf4D[] vertexArray) {
		if(vertexArray == null) {
			return new Vertexf4D[0];
		}
		final int len = vertexArray.length;
		for(int i = 0; i<len; i++) {
			vertexArray[i] = dot(vertexArray[i]);
		}
		return vertexArray;
	}

	public Vertexf4D[] dotKeep(final Vertexf4D[] vertexArray) {
		if(vertexArray == null) {
			return new Vertexf4D[0];
		}
		final int len = vertexArray.length;
		final Vertexf4D[] vertexArrayTemp = new Vertexf4D[len];
		for(int i = 0; i<len; i++) {
			vertexArrayTemp[i] = dotKeep(vertexArray[i]);
		}
		return vertexArrayTemp;
	}
	
	public List<Vertexf4D> dot(final List<Vertexf4D> vertexArray) {
		if(vertexArray == null) {
			return new ArrayList<Vertexf4D>();
		}
		final int len = vertexArray.size();
		for(int i = 0; i<len; i++) {
			vertexArray.set(i,dot(vertexArray.get(i)));
		}
		return vertexArray;
	}

	public List<Vertexf4D> dotKeep(final List<Vertexf4D> vertexArray) {
		if(vertexArray == null) {
			return new ArrayList<Vertexf4D>();
		}
		final int len = vertexArray.size();
		final List<Vertexf4D> vertexArrayTemp = new ArrayList<Vertexf4D>();
		for(int i = 0; i<len; i++) {
			vertexArrayTemp.add(dotKeep(vertexArray.get(i)));
		}
		return vertexArray;
	}

	public static float sum(final int i, final int j, final float[][] matrix, final float[][] matrix2) {
		float sum = 0;
		for(int k = 0; k < 4; k++) {
			sum += matrix[i][k] * matrix2[k][j];
		}
		return sum;
	}

	public static float sum(final int i, final float[][] matrix, final float[] vertex) {
		float sum = 0;
		for(int k = 0; k < 4; k++) {
			sum += matrix[i][k] * vertex[k];
		}
		return sum;
	}

	public static Matrixf4D identity() {
		Matrixf4D identity = new Matrixf4D();
		float[][] identityMatrix = identity.getMatrix();
		for(int i = 0; i<4; i++) {
			identityMatrix[i][i] = 1f;
		}
		return identity;
	}
}