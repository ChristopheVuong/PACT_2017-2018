package utils;

public final class ComboItem {
	
	float[] positions;
	byte[] jointStatus;
	
	public ComboItem(final float[] positions, final byte[] jointStatus) {
		this.positions = positions;
		this.jointStatus = jointStatus;
	}
	
	public ComboItem(final float[] positions, final float[] jointStatus) {
		this(positions, toByte(jointStatus));
	}
	
	public float[] getPositions() {
		return positions;
	}
	
	public byte[] getJointStatus() {
		return jointStatus;
	}
	
	public void setPositions(final float[] positions) {
		this.positions = positions;
	}
	
	public void setJointStatus(final byte[] jointStatus) {
		this.jointStatus = jointStatus;
	}
	
	public void setJointStatus(final float[] jointStatus) {
		setJointStatus(toByte(jointStatus));
	}
	
	public ComboItem clone() {
		return new ComboItem(positions.clone(), jointStatus.clone());
	}
	
	private static byte[] toByte(final float[] floatArray) {
		final byte[] byteArray = new byte[floatArray.length];
		for(int i = 0; i<floatArray.length; i++) {
			byteArray[i] = (byte)floatArray[i];
		}
		return byteArray;
	}
}