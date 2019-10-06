package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class Move {

	private static boolean IS_NOT_FIRST_MOVE;
	private static List<FileComboItem> INDEX_LIST;
	private static final int ANGLES_LEN = Constants.LIMBS_ANGLES.length;

	private int id;
	private List<ComboItem> positionsAndStatus;
	private List<float[]> DTW;
	private List<float[]> DTWAngles;

	public Move(final int id) {
		this.id = id;
		if(!IS_NOT_FIRST_MOVE) {
			IS_NOT_FIRST_MOVE = true;

			final File index = new File(Constants.BASE_MOVE + "index.moveroot");
			if(index.exists()) {
				Scanner sc = null;

				try {
					sc = new Scanner(index);
					int indexCount = 0;

					INDEX_LIST = new ArrayList<FileComboItem>();

					while(sc.hasNextLine()) {
						final String str = sc.nextLine();
						if(ValidationMethods.isValidFile(str)) {
							final String[] indexFile = str.split(" => ",2);
							if(Integer.parseInt(indexFile[0]) == indexCount) {
								INDEX_LIST.add(new FileComboItem(indexFile[1].split("&",2)));
								indexCount++;
							} else {
								INDEX_LIST = null;
								break;
							}
						} else {
							INDEX_LIST = null;
							return;
						}
					}
				} catch(final IOException e) {
					INDEX_LIST = null;
				} finally {
					if(sc != null) {
						sc.close();
					}
				}
			}
		}

		if(INDEX_LIST == null) {
			notValid();
		} else {
			Scanner sc = null;
			try {
				final File file = new File(Constants.BASE_MOVE + INDEX_LIST.get(id).getFileName());

				if(file.isFile()) {
					DTW = new ArrayList<float[]>();
					DTWAngles = new ArrayList<float[]>();

					sc = new Scanner(file);

					int numberOfMoves = 0;
					exit:
						while(sc.hasNextLine()) {
							String str = sc.nextLine();

							if(!ValidationMethods.isMoveName(str)) {
								break;
							}

							int numberOfDTW = 0;
							DTW.add(new float[20]);
							DTWAngles.add(new float[ANGLES_LEN]);
							while(sc.hasNextLine()) {
								str = sc.nextLine();
								if(ValidationMethods.isDTW(str)) {
									if(numberOfDTW<20) {
										DTW.get(numberOfMoves)[numberOfDTW++] = Float.parseFloat(str.split("=>",2)[1]);
									} else {
										if(numberOfDTW<20+ANGLES_LEN) {
											DTW.get(numberOfMoves)[numberOfDTW++ - 20] = Float.parseFloat(str.split("=>",2)[1]);
										} else {
											break;
										}
									}
								} else {
									if(!ValidationMethods.isMessage(str)) {
										break;
									}
								}
							}

							if(numberOfDTW != 20+ANGLES_LEN || !sc.hasNextLine()) {
								notValid();
								break exit;
							} else {
								if(numberOfMoves == 0) {
									final StringBuilder buffer = new StringBuilder(str);
									if(str.equals("Enregistrement d'un squelette Kinect")) {
										positionsAndStatus = new ArrayList<ComboItem>();
										ComboItem positionsTemp = null;
										boolean withStatus = false;
										boolean start = false;
										boolean validSqueleton = true;

										while(sc.hasNextLine() && validSqueleton) {
											str = sc.nextLine();
											if(!str.isEmpty()) {
												buffer.append(str);
												final float[] arrayFloat = Handlers.decodeLine(str);

												if(arrayFloat != null) {
													switch(arrayFloat.length) {
													case 20:
														if(start && positionsTemp != null) {
															positionsTemp.setJointStatus(arrayFloat);
															withStatus = true;
															positionsTemp = null;
														} else {
															validSqueleton = false;
														}
														break;
													case 60:
														if(start) {
															if((withStatus && positionsTemp == null) || !withStatus) {
																positionsTemp = new ComboItem(arrayFloat, (byte[])null);
																positionsAndStatus.add(positionsTemp);
															}
														} else {
															start = true;
															positionsTemp = new ComboItem(arrayFloat, (byte[])null);
															positionsAndStatus.add(positionsTemp);
														}
														break;
													}
												} else {
													validSqueleton = false;
												}
											} else {
												break;
											}
										}
										if(!validSqueleton || positionsAndStatus.isEmpty()) {
											notValid();
											break exit;
										}
									} else {
										notValid();
										break exit;
									}
								} else {
									while(sc.hasNextLine()) {
										if(sc.nextLine().isEmpty()) {
											break;
										}
									}
								}
							}
							numberOfMoves++;
						}
				} else {
					notValid();
				}
			} catch(final FileNotFoundException e) {

			} finally {
				if(sc != null) {
					sc.close();
				}
			}
		}
	}

	public boolean isNotValid() {
		return id == -1;
	}

	public int getId() {
		return id;
	}

	public List<ComboItem> getPositionsAndStatus() {
		return positionsAndStatus;
	}

	public List<float[]> getDTW() {
		return DTW;
	}

	public List<float[]> getDTWAngles() {
		return DTWAngles;
	}

	private void notValid() {
		id = -1;
		positionsAndStatus = null;
		DTW = null;
		DTWAngles = null;
	}
}