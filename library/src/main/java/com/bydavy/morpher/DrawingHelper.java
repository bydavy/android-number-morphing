package com.bydavy.morpher;


public class DrawingHelper {

	private DrawingHelper() {
		throw new IllegalAccessError();
	}

	public static int addPoint(float[] array, int index, float x, float y) {
		array[index++] = x;
		array[index++] = y;

		return index;
	}

	public static int addBezierStraightLine(float[] array, int index, float x, float y) {
		float previousX = array[index - 2];
		float previousY = array[index - 1];

		// Generate intermediate points

		float sumX = previousX + x;
		float sumY = previousY + y;

		float x1 = (sumX + previousX) / 3;
		float y1 = (sumY + previousY) / 3;

		float x2= (sumX + x) / 3;
		float y2= (sumY + y) / 3;

		// Add control points
		index = addPoint(array, index, x1, y1);
		index = addPoint(array, index, x2, y2);

		// Add ending point
		index = addPoint(array, index, x, y);

		return index;
	}

	public static int addBezierCurve(float[] array, int index, float x1, float y1, float x2, float y2, float x3, float y3) {
		// Add control points
		index = addPoint(array, index, x1, y1);
		index = addPoint(array, index, x2, y2);

		// Add ending point
		index = addPoint(array, index, x3, y3);

		return index;
	}
}
