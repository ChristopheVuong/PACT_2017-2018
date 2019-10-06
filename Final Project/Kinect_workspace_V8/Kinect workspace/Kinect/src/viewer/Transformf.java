package viewer;

public final class Transformf {

	public static final int X = 0;
	public static final int Y = 1;
	public static final int Z = 2;

	public static final int RADIAN = 0;
	public static final int DEGREE = 1;

	public static Matrixf4D scale(final float scale, final int mode) {
		final Matrixf4D scaledMatrix = Matrixf4D.identity();
		scaledMatrix.getMatrix()[mode][mode] = scale;
		return scaledMatrix;
	}

	public static Matrixf4D scaleX(final float scale) {
		return scale(scale, X);
	}

	public static Matrixf4D scaleY(final float scale) {
		return scale(scale, Y);
	}

	public static Matrixf4D scaleZ(final float scale) {
		return scale(scale, Z);
	}

	public static Matrixf4D scale(final float scaleX, final float scaleY, final float scaleZ) {
		return scaleX(scaleX).dot(scaleY(scaleY).dot(scaleZ(scaleZ)));
	}
	
	public static Matrixf4D scale(final float scale) {
		return scale(scale,scale,scale);
	}
	
	public static Matrixf4D scale(final Vertexf3D scale) {
		return (scale == null) ? new Matrixf4D() : scaleX(scale.getX()).dot(scaleY(scale.getY()).dot(scaleZ(scale.getZ())));
	}

	public static Matrixf4D scale(final float factor, final float scaleX, final float scaleY, final float scaleZ) {
		return scale(scaleX * factor, scaleY * factor, scaleZ * factor);
	}
	
	public static Matrixf4D scale(final float factor, final Vertexf3D scale) {
		return scale(scale.scaleKeep(factor));
	}

	public static Matrixf4D translate(final float translation, final int mode) {
		final Matrixf4D translationMatrix = Matrixf4D.identity();
		translationMatrix.getMatrix()[mode][3] = translation;
		return translationMatrix;
	}

	public static Matrixf4D translateX(final float translation) {
		return translate(translation, X);
	}

	public static Matrixf4D translateY(final float translation) {
		return translate(translation, Y);
	}

	public static Matrixf4D translateZ(final float translation) {
		return translate(translation, Z);
	}

	public static Matrixf4D translate(final float translationX, final float translationY, final float translationZ) {
		return translateX(translationX).dot(translateY(translationY).dot(translateZ(translationZ)));
	}
	
	public static Matrixf4D translate(final Vertexf3D translation) {
		return (translation == null) ? new Matrixf4D() : translateX(translation.getX()).dot(translateY(translation.getY()).dot(translateZ(translation.getZ())));
	}

	public static Matrixf4D translate(final float factor, final float translationX, final float translationY, final float translationZ) {
		return translate(translationX * factor, translationY * factor, translationZ * factor);
	}
	
	public static Matrixf4D translate(final float factor, final Vertexf3D translation) {
		return translate(translation.scaleKeep(factor));
	}

	public static Matrixf4D rotate(float rotation, final int mode, final int angleType) {
		final Matrixf4D rotationMatrix = Matrixf4D.identity();

		if(angleType == DEGREE) {
			rotation = rotation * (float) Math.PI / 180f;
		}

		if(mode == X) {
			final float[][] matrix = rotationMatrix.getMatrix();
			final float cos = (float) Math.cos(rotation);
			final float sin = (float) Math.sin(rotation);
			matrix[Y][Y] = cos;
			matrix[Z][Z] = cos;
			matrix[Y][Z] = -sin;
			matrix[Z][Y] = sin;
		}
		if(mode == Y) {
			final float[][] matrix = rotationMatrix.getMatrix();
			final float cos = (float) Math.cos(rotation);
			final float sin = (float) Math.sin(rotation);
			matrix[X][X] = cos;
			matrix[Z][Z] = cos;
			matrix[X][Z] = -sin;
			matrix[Z][X] = sin;
		}
		if(mode == Z) {
			final float[][] matrix = rotationMatrix.getMatrix();
			final float cos = (float) Math.cos(rotation);
			final float sin = (float) Math.sin(rotation);
			matrix[X][X] = cos;
			matrix[Y][Y] = cos;
			matrix[X][Y] = -sin;
			matrix[Y][X] = sin;
		}
		return rotationMatrix;
	}

	public static Matrixf4D rotate(final float rotation, final int mode) {
		return rotate(rotation, mode, DEGREE);
	}

	public static Matrixf4D rotateX(final float rotation) {
		return rotate(rotation, X, DEGREE);
	}

	public static Matrixf4D rotateY(final float rotation) {
		return rotate(rotation, Y, DEGREE);
	}

	public static Matrixf4D rotateZ(final float rotation) {
		return rotate(rotation, Z, DEGREE);
	}

	public static Matrixf4D rotate(final float rotationX, final float rotationY, final float rotationZ) {
		return rotateX(rotationX).dot(rotateY(rotationY).dot(rotateZ(rotationZ)));
	}
	
	public static Matrixf4D rotate(final Vertexf3D rotation) {
		return (rotation == null) ? new Matrixf4D() : rotateX(rotation.getX()).dot(rotateY(rotation.getY()).dot(rotateZ(rotation.getZ())));
	}

	public static Matrixf4D rotate(final float factor, final float rotationX, final float rotationY, final float rotationZ) {
		return rotate(rotationX * factor, rotationY * factor, rotationZ * factor);
	}
	
	public static Matrixf4D rotate(final float factor, final Vertexf3D rotation) {
		return rotate(rotation.scaleKeep(factor));
	}
	
	public static Matrixf4D rotate(final Vertexf3D center, float rotation, final int mode, final int angleType) {
		return (center == null) ? new Matrixf4D() : translate(center.scaleKeep(-1f)).dotKeep(rotate(rotation, mode, DEGREE).dotKeep(translate(center)));
	}
	
	public static Matrixf4D rotate(final Vertexf3D center, final float rotation, final int mode) {
		return rotate(center, rotation, mode, DEGREE);
	}

	public static Matrixf4D rotateX(final Vertexf3D center, final float rotation) {
		return rotate(center, rotation, X, DEGREE);
	}

	public static Matrixf4D rotateY(final Vertexf3D center, final float rotation) {
		return rotate(center, rotation, Y, DEGREE);
	}

	public static Matrixf4D rotateZ(final Vertexf3D center, final float rotation) {
		return rotate(center, rotation, Z, DEGREE);
	}

	public static Matrixf4D rotate(final Vertexf3D center, final float rotationX, final float rotationY, final float rotationZ) {
		return rotateX(center, rotationX).dot(rotateY(rotationY).dot(rotateZ(rotationZ)));
	}
	
	public static Matrixf4D rotate(final Vertexf3D center, final Vertexf3D rotation) {
		return rotateX(center, rotation.getX()).dot(rotateY(rotation.getY()).dot(rotateZ(rotation.getZ())));
	}

	public static Matrixf4D rotate(final Vertexf3D center, final float factor, final float rotationX, final float rotationY, final float rotationZ) {
		return rotate(center, rotationX * factor, rotationY * factor, rotationZ * factor);
	}
	
	public static Matrixf4D rotate(final Vertexf3D center, final float factor, final Vertexf3D rotation) {
		return rotate(center, rotation.scaleKeep(factor));
	}
}