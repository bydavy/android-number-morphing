package com.bydavy.digitalclock;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class DigitMorpher {

	private static final int POINTS_IN_DIGIT = 13 * 2;

	//FIXME Remove when fully implemented
	public static final int MIN_IMPLEMENTED = 1;
	public static final int MAX_IMPLEMENTED = 3;

	private final Paint mPaint;
	private final Path mPath;

	private float digits[][];

	public DigitMorpher() {
		mPath = new Path();

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.argb(255, 255, 255, 255));
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(3);

		digits = new float[10][];
		//digits[0] = createZero();
		digits[1] = createOne();
		digits[2] = createTwo();
		digits[3] = createThree();
		/*digits[4] = createFour();
		digits[5] = createFive();
		digits[6] = createSix();
		digits[7] = createSeven();
		digits[8] = createEight();
		digits[9] = createNine();*/

		// FIXME Remove this hack
		scaleUp(100);
	}

	private void scaleUp(int scale) {
		// FIXME Huge design flow.
		// I should keep digits non scaled in memory and not override them
		for (int i = 0; i < digits.length; i++) {
			float[] array = digits[i];
			for (int j = 0; array != null && j < array.length; j++) {
				array[j] *= scale;
			}
		}
	}

	public void setColor(int color) {
		mPaint.setColor(color);
	}

	private static float originX() {
		return 0;
	}

	private static float originY() {
		return 0;
	}

	private static float maxX() {
		return .75f;
	}

	private static float maxY() {
		return 1;
	}

	private static int addPoint(float[] array, int index, float x, float y) {
		array[index++] = x;
		array[index++] = y;

		return index;
	}

	private static int addBezierStraightLine(float[] array, int index, float x, float y) {
		float previousX = array[index - 2];
		float previousY = array[index - 1];

		// Add control points
		index = addPoint(array, index, previousX, previousY);
		index = addPoint(array, index, x, y);

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

	private static float[] createDigitArray() {
		return new float[POINTS_IN_DIGIT];
	}

	private static void checkSize(float[] array, int index) {
		if (index != array.length) {
			throw new RuntimeException("Array of size " + array + " and index is " + index);
		}
	}

	private static float[] createZero() {
		float[] zero = createDigitArray();

		// WIP

		return zero;
	}

	private static float[] createOne() {
		float[] one = createDigitArray();

		float p1x = originX();
		float p1y = originY();

		float p2x = maxX() / 2;
		float p2y = p1y;

		float p3x = p2x;
		float p3y = maxY();

		float soft1y = (p2y + p3y) / 2 * (1f / 3);
		float soft2y = (p2y + p3y) / 2 * (2f / 3);

		int i = 0;
		// First hard point
		i = addPoint(one, i, p1x, p1y);

		// Second hard point
		i = addBezierStraightLine(one, i, p2x, p2y);

		// Soft point (added to animate)
		i = addBezierStraightLine(one, i, p2x, soft1y);
		i = addBezierStraightLine(one, i, p2x, soft2y);

		// Third hard point
		i = addBezierStraightLine(one, i, p3x, p3y);

		checkSize(one, i);

		return one;
	}

	private static float[] createTwo() {
		float[] two = createDigitArray();

		float p1x = originX();
		float p1y = maxX() / 2;

		float p2x = maxX();
		float p2y = p1y;

		float p3x = p2x;
		float p3y = p2y + (1f / 20);

		float p4x = originX();
		float p4y = maxY();

		float p5x = maxX();
		float p5y = maxY();

		int i = 0;
		// First hard point
		i = addPoint(two, i, p1x, p1y);

		// Soft point (added to animate)
		i = addBezierCurve(two, i, p1x, -p1y / 2, p2x, -p2y / 2, p2x, p2y);

		// Second hard point
		i = addBezierStraightLine(two, i, p3x, p3y);
		i = addBezierStraightLine(two, i, p4x, p4y);
		i = addBezierStraightLine(two, i, p5x, p5y);

		checkSize(two, i);

		return two;
	}

	private static float[] createThree() {
		float[] three = createDigitArray();

		// WIP

		return three;
	}

	private static float[] createFour() {
		float[] four = createDigitArray();

		// WIP

		return four;
	}

	private static float[] createFive() {
		float[] five = createDigitArray();

		// WIP

		return five;
	}

	private static float[] createSix() {
		float[] six = createDigitArray();

		// WIP

		return six;
	}

	private static float[] createSeven() {
		float[] seven = createDigitArray();

		// WIP

		return seven;
	}

	private static float[] createEight() {
		float[] eight = createDigitArray();

		// WIP

		return eight;
	}

	private static float[] createNine() {
		float[] nine = createDigitArray();

		// WIP

		return nine;
	}

	public void draw(Canvas canvas, int digit) {
		draw(canvas, digits[digit]);
	}

	public void drawAnimation(Canvas canvas, int digit, float percent) {
		int previousDigit = digit != 0 ? digit - 1 : 9;

		draw(canvas, digits[previousDigit], digits[digit], percent);
	}

	private void draw(Canvas canvas, float[] array) {
		// FIXME remove when all digits are implemented
		if (array == null) return;

		mPath.reset();

		int i = 0;
		mPath.moveTo(array[i++], array[i++]);

		while (i < array.length) {
			mPath.cubicTo(array[i++], array[i++], array[i++], array[i++], array[i++], array[i++]);
		}

		canvas.drawPath(mPath, mPaint);
	}

	private void draw(Canvas canvas, float[] a, float[] b, float percent) {
		// FIXME remove when all digits are implemented
		if (a == null || b == null) return;

		mPath.reset();

		int i = 0;
		mPath.moveTo(animate(a[i], b[i++], percent), animate(a[i], b[i++], percent));

		while (i < a.length) {
			mPath.cubicTo(animate(a[i], b[i++], percent), animate(a[i], b[i++], percent),
					animate(a[i], b[i++], percent), animate(a[i], b[i++], percent),
					animate(a[i], b[i++], percent), animate(a[i], b[i++], percent));
		}

		canvas.drawPath(mPath, mPaint);
	}

	private static float animate(float a, float b, float percent) {
		return a * (1 - percent) + b * percent;
	}


}
