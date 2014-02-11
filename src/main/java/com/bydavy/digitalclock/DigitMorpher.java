package com.bydavy.digitalclock;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class DigitMorpher {

	private static final int POINTS_IN_DIGIT = 13 * 2;

	//FIXME Remove when fully implemented
	public static final int MIN_IMPLEMENTED = 1;
	public static final int MAX_IMPLEMENTED = 9;

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

		// FIXME I should write a tool to tweak the bezier curve and make prettier digits
		digits = new float[10][];
		//digits[0] = createZero();
		digits[1] = createOne();
		digits[2] = createTwo();
		digits[3] = createThree();
		digits[4] = createFour();
		digits[5] = createFive();
		digits[6] = createSix();
		digits[7] = createSeven();
		digits[8] = createEight();
		digits[9] = createNine();

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
		return .6f;
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
		float oneMaxX = maxX() / 2;

		float p1x = originX();
		float p1y = originY();

		float p2x = oneMaxX;
		float p2y = p1y;

		float p3x = p2x;
		float p3y = maxY();

		float soft1x = p2x;
		float soft1y = (p2y + p3y) / 2 * (1f / 3);

		float soft2x = p2x;
		float soft2y = (p2y + p3y) / 2 * (2f / 3);

		float[] one = createDigitArray();

		int i = 0;
		i = addPoint(one, i, p1x, p1y);
		i = addBezierStraightLine(one, i, p2x, p2y);
		i = addBezierStraightLine(one, i, soft1x, soft1y);
		i = addBezierStraightLine(one, i, soft2x, soft2y);
		i = addBezierStraightLine(one, i, p3x, p3y);

		checkSize(one, i);

		return one;
	}

	private static float[] createTwo() {
		float twoMaxX = maxX();

		float p1x = originX();
		float p1y = twoMaxX / 2;

		float p2x = twoMaxX;
		float p2y = p1y;

		float p3x = p2x;
		float p3y = p2y + (1f / 20);

		float p4x = originX();
		float p4y = maxY();

		float p5x = twoMaxX;
		float p5y = maxY();

		float[] two = createDigitArray();

		int i = 0;
		i = addPoint(two, i, p1x, p1y);
		i = addBezierCurve(two, i, p1x, -p1y / 2, p2x, -p2y / 2, p2x, p2y);
		i = addBezierStraightLine(two, i, p3x, p3y);
		i = addBezierStraightLine(two, i, p4x, p4y);
		i = addBezierStraightLine(two, i, p5x, p5y);

		checkSize(two, i);

		return two;
	}

	private static float[] createThree() {
		float threeMaxX = maxX();

		float halfMaxX = threeMaxX / 2;
		float fourthMaxX = halfMaxX / 2;

		float p1x = originX();
		float p1y = halfMaxX;

		float p2x = halfMaxX;
		float p2y = maxY() / 2;

		float p3x = originX();
		float p3y = maxY() - halfMaxX;

		// Between p1 and p2
		float soft1x = threeMaxX;
		float soft1y = p1y;

		// Between p2 and p3
		float soft2x = threeMaxX;
		float soft2y = p3y;

		float[] three = createDigitArray();

		int i = 0;
		i = addPoint(three, i, p1x, p1y);
		i = addBezierCurve(three, i, p1x, -fourthMaxX, soft1x, -fourthMaxX, soft1x, soft1y);
		i = addBezierCurve(three, i, soft1x, soft1y + fourthMaxX, p2x + fourthMaxX, p2y, p2x, p2y);
		i = addBezierCurve(three, i, p2x + fourthMaxX, p2y, soft2x, soft2y - fourthMaxX, soft2x, soft2y);
		i = addBezierCurve(three, i, soft2x, maxY() + fourthMaxX, p3x, maxY() + fourthMaxX, p3x, p3y);

		checkSize(three, i);

		return three;
	}

	private static float[] createFour() {
		float fourMaxX = maxX();

		float p1x = fourMaxX;
		float p1y = maxY() * (2 / 3f);

		float p2x = originX();
		float p2y = p1y;

		float p3x = fourMaxX * (3 / 4f);
		float p3y = originY();

		float p4x = p3x;
		float p4y = maxY();

		// Between p3 and p4
		float soft1x = p3x;
		float soft1y = p1y;

		float[] four = createDigitArray();

		int i = 0;
		i = addPoint(four, i, p1x, p1y);
		i = addBezierStraightLine(four, i, p2x, p2y);
		i = addBezierStraightLine(four, i, p3x, p3y);
		i = addBezierStraightLine(four, i, soft1x, soft1y);
		i = addBezierStraightLine(four, i, p4x, p4y);

		checkSize(four, i);

		return four;
	}

	private static float[] createFive() {
		float fiveMaxX = maxX();

		float diameter = fiveMaxX;
		float radius = fiveMaxX / 2;

		float p1x = fiveMaxX;
		float p1y = originY();

		float p2x = originX();
		float p2y = originY();

		float p3x = originX();
		float p3y = maxY() - diameter;

		float p4x = originX();
		float p4y = maxY();

		// Between p3 and p4
		float soft1x = fiveMaxX;
		float soft1y = p3y + radius;

		float[] five = createDigitArray();

		int i = 0;
		i = addPoint(five, i, p1x, p1y);
		i = addBezierStraightLine(five, i, p2x, p2y);
		i = addBezierStraightLine(five, i, p3x, p3y);
		i = addBezierCurve(five, i, p3x + radius, p3y, soft1x, soft1y - radius, soft1x, soft1y);
		i = addBezierCurve(five, i, soft1x, soft1y + radius, p4x + radius, p4y, p4x, p4y);

		checkSize(five, i);

		return five;
	}

	private static float[] createSix() {
		float sixMaxX = maxX();

		float diameter = sixMaxX;
		float radius = diameter / 2;
		float halfRadius = radius / 2;

		float p1x = sixMaxX;
		float p1y = originY();

		float p2x = originX();
		float p2y = maxY() - radius;

		float p3x = p2x;
		float p3y = p2y;

		// Between p2 and soft2
		float soft1x = radius;
		float soft1y = maxY();

		// Between soft2 and p3
		float soft2x = sixMaxX;
		float soft2y = maxY() - radius;

		float[] six = createDigitArray();

		int i = 0;
		i = addPoint(six, i, p1x, p1y);
		i = addBezierCurve(six, i, p1x / 2, 0, 0, p2y - (p2y / 2), p2x, p2y);
		i = addBezierCurve(six, i, p2x, p2y + halfRadius, soft1x - halfRadius, soft1y, soft1x, soft1y);
		i = addBezierCurve(six, i, soft1x + halfRadius, soft1y, soft2x, soft2y + halfRadius, soft2x, soft2y);
		// FIXME Not a perfect circle
		i = addBezierCurve(six, i, soft2x, soft2y - radius - halfRadius, p3x, p3y - radius - halfRadius, p3x, p3y);
		checkSize(six, i);

		return six;
	}

	private static float[] createSeven() {
		float sevenMaxX = maxX();

		float p1x = originX();
		float p1y = originY();

		float p2x = sevenMaxX;
		float p2y = originY();

		float p3x = originX();
		float p3y = maxY();

		// Between p1 and p2
		float soft1x = (p1x + p2x) / 2;
		float soft1y = (p1y + p2y) / 2;

		// Between p2 and p3
		float soft2x = (p2x + p3x) / 2;
		float soft2y = (p2y + p3y) / 2;

		float[] seven = createDigitArray();

		int i = 0;
		i = addPoint(seven, i, p1x, p1y);
		i = addBezierStraightLine(seven, i, soft1x, soft1y);
		i = addBezierStraightLine(seven, i, p2x, p2y);
		i = addBezierStraightLine(seven, i, soft2x, soft2y);
		i = addBezierStraightLine(seven, i, p3x, p3y);

		checkSize(seven, i);

		return seven;
	}

	private static float[] createEight() {
		float eightMaxX = maxX();
		float halfMaxX = eightMaxX / 2;

		float leftControlPointX = halfMaxX - (2 / 3f) * eightMaxX;
		float rightControlPointX = halfMaxX + (2 / 3f) * eightMaxX;

		float p1x = halfMaxX;
		float p1y = maxY() / 2;

		float p2x = halfMaxX;
		float p2y = originY();

		float p3x = p1x;
		float p3y = p1y;

		float p4x = halfMaxX;
		float p4y = maxY();

		float p5x = p1x;
		float p5y = p1y;

		float[] eight = createDigitArray();

		int i = 0;
		i = addPoint(eight, i, p1x, p1y);
		i = addBezierCurve(eight, i, leftControlPointX, p1y, leftControlPointX, p2y, p2x, p2y);
		i = addBezierCurve(eight, i, rightControlPointX, p2y, rightControlPointX, p3y, p3x, p3y);
		i = addBezierCurve(eight, i, rightControlPointX, p3y, rightControlPointX, p4y, p4x, p4y);
		i = addBezierCurve(eight, i, leftControlPointX, p4y, leftControlPointX, p5y, p5x, p5y);

		checkSize(eight, i);

		return eight;
	}

	private static float[] createNine() {

		float nineMaxX = maxX();

		float diameter = nineMaxX;
		float radius = diameter / 2;
		float halfRadius = radius / 2;
		float radiusControl = (2 / 3f) * nineMaxX;

		float p1x = nineMaxX;
		float p1y = radius;

		float p2x = p1x;
		float p2y = p1y;

		float p3x = nineMaxX;
		float p3y = diameter;

		float p4x = 0;
		float p4y = maxY();

		// Between p1 and p2
		float soft1x = originX();
		float soft1y = radius;

		float[] nine = createDigitArray();

		int i = 0;
		i = addPoint(nine, i, p1x, p1y);
		i = addBezierCurve(nine, i, p1x, p1y + radiusControl, soft1x, soft1y + radiusControl, soft1x, soft1y);
		i = addBezierCurve(nine, i, soft1x, soft1y - radiusControl, p2x, p2y - radiusControl, p2x, p2y);
		i = addBezierStraightLine(nine, i, p3x, p3y);
		// FIXME Doesn't look good
		i = addBezierCurve(nine, i, p3x, p3y + (diameter * (2/3f)), p4x + radius, p4y, p4x, p4y);

		checkSize(nine, i);

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
