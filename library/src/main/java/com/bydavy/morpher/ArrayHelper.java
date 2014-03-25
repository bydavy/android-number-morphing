package com.bydavy.morpher;

public class ArrayHelper {

	private ArrayHelper() {
		throw new IllegalAccessError();
	}

	public static float[] expandIfRequired(float[] a, int newSize) {
		if (a == null || a.length != newSize) {
			a = new float[newSize];
		}

		return a;
	}

	public static float[][] expandIfRequired(float[][] a, int newSize) {
		if (a == null || a.length != newSize) {
			a = new float[newSize][];
		}

		return a;
	}

	public static float[][] expandIfRequired(float[][] a, int newSizeDim1, int newSizeDim2) {
		a = expandIfRequired(a, newSizeDim1);

		for (int i = 0; i < newSizeDim1; i++) {
			a[i] = expandIfRequired(a[i], newSizeDim2);
		}

		return a;
	}

	public static boolean[] expandIfRequired(boolean[] a, int newSize) {
		if (a == null || a.length != newSize) {
			a = new boolean[newSize];
		}

		return a;
	}
}
