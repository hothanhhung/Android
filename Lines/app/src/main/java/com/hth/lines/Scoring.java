package com.hth.lines;

import java.util.ArrayList;

import android.graphics.Point;

public class Scoring {

	private int[][] matrix = null;
	public ArrayList<Point> arrayList = new ArrayList<Point>();
	private ArrayList<Point> array = null;

	public Scoring(int a[][]) {
		this.matrix = a;
	}

	// 1
	int LeftMark(int i, int j, int CurPlayer) {
		int dem = 0;
		do {
			if (j > 0) {
				j--;
				if (matrix[i][j] == CurPlayer) {
				} else
					break;
			}
		} while (j > 0);
		if (matrix[i][j] == CurPlayer)
			dem = RightMark(i, j, CurPlayer);
		else
			dem = RightMark(i, j + 1, CurPlayer);
		return dem;
	}

	// 2
	int RightMark(int i, int j, int CurPlayer) {
		int dem = 0;
		array.add(new Point(i, j));

		do {
			if (j < 8) {
				j++;
				if (matrix[i][j] == CurPlayer) {
					dem++;
					array.add(new Point(i, j));
				} else
					break;
			}
		} while (j < 8);
		return dem;
	}

	// CheckUpDown
	int UpMark(int i, int j, int CurPlayer) {
		int dem;
		dem = 0;
		do {
			if (i > 0) {
				i--;
				if (matrix[i][j] == CurPlayer) {
				} else
					break;
			}
		} while (i > 0);
		if (matrix[i][j] == CurPlayer)
			dem = DownMark(i, j, CurPlayer);
		else
			dem = DownMark(i + 1, j, CurPlayer);
		return dem;
	}

	int DownMark(int i, int j, int CurPlayer) {
		int dem = 0;
		array.add(new Point(i, j));
		do {
			if (i < 8) {
				i++;
				if (matrix[i][j] == CurPlayer) {
					dem++;
					array.add(new Point(i, j));
				} else
					break;
			}
		} while (i < 8);
		return dem;
	}

	// LeftUp_RightDown
	int LeftUpMark(int i, int j, int CurPlayer) {
		int dem = 0;
		do {
			if (i > 0 && j > 0) {
				i--;
				j--;
				if (matrix[i][j] == CurPlayer) {
				} else
					break;
			}
		} while (i > 0 && j > 0);
		if (matrix[i][j] == CurPlayer)
			dem = RightDownMark(i, j, CurPlayer);
		else {
			dem = RightDownMark(i + 1, j + 1, CurPlayer);
		}
		return dem;
	}

	int RightDownMark(int i, int j, int CurPlayer) {
		int dem = 0;
		array.add(new Point(i, j));
		do {
			if (i < 8 && j < 8) {
				i++;
				j++;
				if (matrix[i][j] == CurPlayer) {
					dem++;
					array.add(new Point(i, j));
				} else
					break;
			}
		} while (i < 8 && j < 8);
		return dem;
	}

	// LeftDown_RightUp
	int LeftDownMark(int i, int j, int CurPlayer) {
		int dem = 0;
		do {
			if (j > 0 && i < 8) {
				j--;
				i++;
				if (matrix[i][j] == CurPlayer) {
				} else
					break;
			}
		} while (j > 0 && i < 8);
		if (matrix[i][j] == CurPlayer)
			dem = RightUpMark(i, j, CurPlayer);
		else {
			dem = RightUpMark(i - 1, j + 1, CurPlayer);
		}
		return dem;
	}

	int RightUpMark(int i, int j, int CurPlayer) {
		int dem = 0;
		array.add(new Point(i, j));
		do {
			if (i > 0 && j < 8) {
				i--;
				j++;
				if (matrix[i][j] == CurPlayer) {
					dem++;
					array.add(new Point(i, j));
				} else
					break;
			}
		} while (i > 0 && j < 8);
		return dem;
	}

	private ArrayList<Point> getArrayList(ArrayList<Point> arr1,
			ArrayList<Point> arr2) {
		for (Point p : arr2) {
			arr1.add(p);
		}
		return arr1;
	}

	public boolean TinhDiem(int x, int y, int CurPlayer) {
		array = new ArrayList<Point>();
		int diemmax1, diemmax2, diemmax3, diemmax4;
		boolean finish = false;
		diemmax1 = LeftMark(x, y, CurPlayer) + 1;
		if (diemmax1 >= 5) {
			arrayList = getArrayList(arrayList, array);
		}
		// ketQua += CongDiem(diemmax1);
		array.clear();
		diemmax2 = UpMark(x, y, CurPlayer) + 1;
		if (diemmax2 >= 5) {
			arrayList = getArrayList(arrayList, array);
		}
		// ketQua += CongDiem(diemmax2);
		array.clear();
		diemmax3 = LeftUpMark(x, y, CurPlayer) + 1;
		if (diemmax3 >= 5) {
			arrayList = getArrayList(arrayList, array);
		}
		// ketQua += CongDiem(diemmax3);
		array.clear();
		diemmax4 = LeftDownMark(x, y, CurPlayer) + 1;
		if (diemmax4 >= 5) {
			arrayList = getArrayList(arrayList, array);
		}
		// ketQua += CongDiem(diemmax4);
		if (diemmax1 >= 5 || diemmax2 >= 5 || diemmax3 >= 5 || diemmax4 >= 5)
			finish = true;
		else
			finish = false;
		return finish;
	}

	private int AdditionMark(ArrayList<Point> arr) {
		int TMark = 0;
		int Curmark = arr.size();

		switch (Curmark) {
		case 5:
			TMark = Curmark * 1;
			break;
		case 6:
			TMark = Curmark * 2;
			break;
		case 7:
			TMark = Curmark * 3;
			break;
		case 8:
			TMark = Curmark * 4;
			break;
		case 9:
			TMark = Curmark * 5;
			break;
		case 10:
			TMark = Curmark * 6;
			break;
		case 11:
			TMark = Curmark * 7;
			break;
		case 12:
			TMark = Curmark * 8;
			break;
		case 13:
			TMark = Curmark * 9;
			break;
		case 14:
			TMark = Curmark * 10;
			break;
		case 15:
			TMark = Curmark * 11;
			break;
		case 16:
			TMark = Curmark * 12;
			break;
		case 17:
			TMark = Curmark * 13;
			break;
		case 18:
			TMark = Curmark * 14;
			break;
		case 19:
			TMark = Curmark * 15;
			break;
		case 20:
			TMark = Curmark * 16;
			break;
		case 21:
			TMark = Curmark * 17;
			break;
		case 22:
			TMark = Curmark * 18;
			break;
		case 23:
			TMark = Curmark * 19;
			break;
		case 24:
			TMark = Curmark * 20;
			break;
		case 25:
			TMark = Curmark * 21;
			break;
		case 26:
			TMark = Curmark * 22;
			break;
		case 27:
			TMark = Curmark * 23;
			break;
		case 28:
			TMark = Curmark * 24;
			break;
		case 29:
			TMark = Curmark * 25;
			break;
		default:
			TMark = 1000;
			break;

		}

		return TMark;
	}

	public int KetQua() {
		return AdditionMark(arrayList);
	}

}
