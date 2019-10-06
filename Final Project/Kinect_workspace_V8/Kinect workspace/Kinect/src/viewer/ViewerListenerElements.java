package viewer;

public interface ViewerListenerElements {
	public ModelView getModelView();
	public void setLeftDragInitialized(boolean leftDragInitialized);
	public void setRightDragInitialized(boolean rightDragInitialized);
	public boolean getLeftDragInitialized();
	public boolean getRightDragInitialized();
}