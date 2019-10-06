package viewer;

public final class FaceCulling {

	public static boolean isCullable(Vertexi2D[] vertexArray) {
		if(vertexArray == null || vertexArray.length < 3) {
			return false;
		}
		return (vertexArray[1].getX() - vertexArray[0].getX())*(vertexArray[2].getY() - vertexArray[0].getY()) - (vertexArray[1].getY() - vertexArray[0].getY())*(vertexArray[2].getX() - vertexArray[0].getX()) <= 0;
	}
}