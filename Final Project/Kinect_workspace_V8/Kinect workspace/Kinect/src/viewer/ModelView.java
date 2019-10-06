package viewer;

import java.io.PrintWriter;
import java.util.List;

public final class ModelView {

	private final Matrixf4D modelview;
	
	public ModelView() {
		modelview = Matrixf4D.identity();
	}
	
	public Matrixf4D getModelView() {
		return modelview;
	}
	
	public Vertexf4D dot(final Vertexf4D vertex) {
		return modelview.dotKeep(vertex);
	}
	
	public Vertexf4D[] dot(final Vertexf4D[] vertices) {
		return modelview.dotKeep(vertices);
	}
	
	public List<Vertexf4D> dot(final List<Vertexf4D> vertices) {
		return modelview.dotKeep(vertices);
	}

	public void scale(final float scale, final int mode) {
		modelview.dot(Transformf.scale(scale,mode));
	}

	public void scaleX(final float scale) {
		modelview.dot(Transformf.scaleX(scale));
	}

	public void scaleY(final float scale) {
		modelview.dot(Transformf.scaleY(scale));
	}

	public void scaleZ(final float scale) {
		modelview.dot(Transformf.scaleZ(scale));
	}

	public void scale(final float scaleX, final float scaleY, final float scaleZ) {
		modelview.dot(Transformf.scale(scaleX,scaleY,scaleZ));
	}
	
	public void scale(final float scale) {
		modelview.dot(Transformf.scale(scale));
	}
	
	public void scale(final Vertexf3D scale) {
		modelview.dot(Transformf.scale(scale));
	}

	public void scale(final float factor, final float scaleX, final float scaleY, final float scaleZ) {
		modelview.dot(Transformf.scale(factor,scaleX,scaleY,scaleZ));
	}
	
	public void scale(final float factor, final Vertexf3D scale) {
		modelview.dot(Transformf.scale(factor,scale));
	}

	public void translate(final float translation, final int mode) {
		modelview.dot(Transformf.translate(translation,mode));
	}

	public void translateX(final float translation) {
		modelview.dot(Transformf.translateX(translation));
	}

	public void translateY(final float translation) {
		modelview.dot(Transformf.translateY(translation));
	}

	public void translateZ(final float translation) {
		modelview.dot(Transformf.translateZ(translation));
	}

	public void translate(final float translationX, final float translationY, final float translationZ) {
		modelview.dot(Transformf.translate(translationX,translationY,translationZ));
	}
	
	public void translate(final Vertexf3D translation) {
		modelview.dot(Transformf.translate(translation));
	}

	public void translate(final float factor, final float translationX, final float translationY, final float translationZ) {
		modelview.dot(Transformf.translate(factor,translationX,translationY,translationZ));
	}
	
	public void translate(final float factor, final Vertexf3D translation) {
		modelview.dot(Transformf.translate(factor,translation));
	}

	public void rotate(final float rotation, final int mode, final int angleType) {
		modelview.dot(Transformf.rotate(rotation,mode,angleType));
	}

	public void rotate(final float rotation, final int mode) {
		modelview.dot(Transformf.rotate(rotation,mode));
	}

	public void rotateX(final float rotation) {
		modelview.dot(Transformf.rotateX(rotation));
	}

	public void rotateY(final float rotation) {
		modelview.dot(Transformf.rotateY(rotation));
	}

	public void rotateZ(final float rotation) {
		modelview.dot(Transformf.rotateZ(rotation));
	}

	public void rotate(final float rotationX, final float rotationY, final float rotationZ) {
		modelview.dot(Transformf.rotate(rotationX,rotationY,rotationZ));
	}
	
	public void rotate(final Vertexf3D rotation) {
		modelview.dot(Transformf.rotate(rotation));
	}

	public void rotate(final float factor, final float rotationX, final float rotationY, final float rotationZ) {
		modelview.dot(Transformf.rotate(factor,rotationX,rotationY,rotationZ));
	}
	
	public void rotate(final float factor, final Vertexf3D rotation) {
		modelview.dot(Transformf.rotate(factor,rotation));
	}
	
	public void rotate(final Vertexf3D center, float rotation, final int mode, final int angleType) {
		modelview.dot(Transformf.rotate(center,rotation,mode,angleType));
	}
	
	public void rotate(final Vertexf3D center, final float rotation, final int mode) {
		modelview.dot(Transformf.rotate(center,rotation,mode));
	}

	public void rotateX(final Vertexf3D center, final float rotation) {
		modelview.dot(Transformf.rotateX(center,rotation));
	}

	public void rotateY(final Vertexf3D center, final float rotation) {
		modelview.dot(Transformf.rotateY(center,rotation));
	}

	public void rotateZ(final Vertexf3D center, final float rotation) {
		modelview.dot(Transformf.rotateZ(center,rotation));
	}

	public void rotate(final Vertexf3D center, final float rotationX, final float rotationY, final float rotationZ) {
		modelview.dot(Transformf.rotate(center,rotationX,rotationY,rotationZ));
	}
	
	public void rotate(final Vertexf3D center, final Vertexf3D rotation) {
		modelview.dot(Transformf.rotate(center,rotation));
	}

	public void rotate(final Vertexf3D center, final float factor, final float rotationX, final float rotationY, final float rotationZ) {
		modelview.dot(Transformf.rotate(center,factor,rotationX,rotationY,rotationZ));
	}
	
	public void rotate(final Vertexf3D center, final float factor, final Vertexf3D rotation) {
		modelview.dot(Transformf.rotate(factor,rotation));
	}
}