package com.bydavy.morpher;


public class DrawingHelper {

	private int mIndex = 0;
	private float[] mPoints;
	private float mHeight = 1;
	private float mWidth = 1;
	private float mPadding = 0;

	public DrawingHelper(int size, float width, float height, float padding, float startx, float starty) {
		mPoints = new float[size];
		mWidth = width;
		mHeight = height;
		mPadding = padding;
		addPoint(startx, starty);
	}

	public DrawingHelper(int size, float startx, float starty) {
		mPoints = new float[size];
		addPoint(startx, starty);
	}

	// Adjusts point x by a factor of mWidth and mPadding
	private float x(float x1) {
		return x1 * mWidth + mPadding;
	}

	// Adjusts point y by a factor of mHeight and mPadding
	private float y(float y1) {
		return y1 * mHeight + mPadding;
	}

	public DrawingHelper addPoint(float x, float y) {
		mIndex = DrawingHelper.addPoint(mPoints, mIndex, x(x), y(y));
		return this;
	}

	public DrawingHelper addBezierCurve(float x1, float y1, float x2, float y2, float x3, float y3) {
		mIndex = DrawingHelper.addBezierCurve(mPoints, mIndex, x(x1), y(y1), x(x2), y(y2), x(x3), y(y3));
		return this;
	}

	public DrawingHelper addBezierStraightLine(float x, float y) {
		mIndex = DrawingHelper.addBezierStraightLine(mPoints, mIndex, x(x), y(y));
		return this;
	}

	public float[] build() {
		return mPoints;
	}

	// Static Methods

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

		float x2 = (sumX + x) / 3;
		float y2 = (sumY + y) / 3;

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