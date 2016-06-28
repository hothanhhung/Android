package com.hth.lines;

import java.util.ArrayList;

import android.graphics.Point;

//Lớp này có nhiệm vụ
/*
 1. Đầu vào là tọa độ X1, Y1, X2, Y2 --> Mục tiêu tìm đường đi ngắn nhất giữa 2 điểm này
 */

public class Algorithm {

	private final int N = 82;
	private int[][] a, b;
	private int[] queue, dd, ddd, kt;
	private int s, t, ss, sst, k = 0;
	private boolean co;

	// Dau vao la 1 mang, va 2 toa do x1y1, x2y2
	// truyen mang[][] du lieu ban co LINE vao, =1 la bong lon,=2 bong nho, =0
	// khong co;
	public Algorithm(int[][] mang, int x1, int y1, int x2, int y2) {
		b = mang;
		b[x1][y1] = 0;// bong bi dieu chuyen di
		s = x1 * 9 + y1;
		t = x2 * 9 + y2;
		ss = s;
		this.initialize();
		this.algorithm_tofind_way();
	}

	public void initialize() {
		int xi = -1, yi = -1;
		int xj = -1, yj = -1;
		a = new int[81][81];
		for (int i = 0; i < 81; i++) {
			xi = i / 9;
			yi = i % 9;
			for (int j = 0; j < 81; j++) {
				xj = j / 9;
				yj = j % 9;
				if (((xi == xj) && (Math.abs(yi - yj) == 1)
						&& (b[xi][yi] >= 0) && (b[xj][yj] >= 0))
						|| ((yi == yj) && (Math.abs(xi - xj) == 1)
								&& (b[xi][yi] >= 0) && (b[xj][yj] >= 0))) {
					a[i][j] = 1;
				} else
					a[i][j] = 0;
			}

		}
		queue = new int[N];
		kt = new int[N];
		dd = new int[N];
		ddd = new int[N];
		for (int i = 0; i < N; i++) {
			dd[i] = 0;
			kt[i] = 0;

		}
		sst = 1;
		queue[sst] = s;
		kt[s] = 1;
	}

	public void algorithm_tofind_way() {
		int d = 0;
		co = false;
		while (d < sst && !co) {
			d++;
			s = queue[d];
			for (int i = 0; i < 81; i++) {
				if (a[s][i] == 1 && kt[i] == 0) {
					sst++;
					queue[sst] = i;
					kt[i] = 1;
					dd[sst] = s;
					if (i == t)
						co = true;
				}
			}
		}
		if (co) {
			int i = t;
			while (i != ss) {
				int j = 1;
				while (queue[j] != i)
					j++;
				k++;
				ddd[k] = dd[j];
				i = dd[j];
			}
			ddd[0] = t;
		}
	}

	public boolean isGo() {
		return co;
	}

	public ArrayList<Point> getWay() {
		Point[] p = new Point[k];
		ArrayList<Point> list = new ArrayList<Point>();
		for (int i = 0; i < k; i++) {
			p[i] = new Point(ddd[i] / 9, ddd[i] % 9);
			list.add(p[i]);
		}
		return list;
	}
}
