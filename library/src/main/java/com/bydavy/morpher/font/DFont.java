package com.bydavy.morpher.font;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import com.bydavy.morpher.DrawingHelper;
import com.bydavy.morpher.Font;

public class DFont implements Font {
	private static final int POINTS_PER_GLYPH = 13 * 2;
	private static final int DIGITS = 10;

	private static final float KAPPA = (float) (4 * ((Math.sqrt(2) - 1) / 3));

	private final int mSize;
	private final int mThickness;

	private final Path mPath;
	private final Paint mPaint;

	private static boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	private static boolean isColumn(char c) {
		return c == ':';
	}

	private static int getDigitIndex(char c) {
		return c - '0';
	}

	private final float[] mGlyphBounds;
	private final float[][] mDigitBounds;
	private final float[][] mDigits;

	private final float mColumnWidth;

	public DFont(int size, int thickness) {
		mPath = new Path();

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.argb(255, 255, 255, 255));
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(thickness);
		mPaint.setStrokeJoin(Paint.Join.MITER);

		mSize = size;
		mThickness = thickness;

		mGlyphBounds = new float[2];
		mGlyphBounds[0] = boundsX();
		mGlyphBounds[1] = boundsY();

		mDigitBounds = new float[DIGITS][];
		mDigitBounds[0] = getZeroBounds();
		mDigitBounds[1] = getOneBounds();
		mDigitBounds[2] = getTwoBounds();
		mDigitBounds[3] = getThreeBounds();
		mDigitBounds[4] = getFourBounds();
		mDigitBounds[5] = getFiveBounds();
		mDigitBounds[6] = getSixBounds();
		mDigitBounds[7] = getSevenBounds();
		mDigitBounds[8] = getEightBounds();
		mDigitBounds[9] = getNineBounds();

		mDigits = new float[DIGITS][];
		mDigits[0] = getZero();
		mDigits[1] = getOne();
		mDigits[2] = getTwo();
		mDigits[3] = getThree();
		mDigits[4] = getFour();
		mDigits[5] = getFive();
		mDigits[6] = getSix();
		mDigits[7] = getSeven();
		mDigits[8] = getEight();
		mDigits[9] = getNine();

		mColumnWidth = innerBoxWidth() / 2;
	}

	@Override
	public int getPointsCount() {
		return POINTS_PER_GLYPH;
	}

	@Override
	public boolean hasGlyph(char c) {
		return isDigit(c) || isColumn(c);
	}

	@Override
	public float[] getGlyphPoints(char c) {
		float[] result = null;

		if (isDigit(c)) {
			result = mDigits[getDigitIndex(c)];
		}

		return result;
	}

	@Override
	public float[] getGlyphBounds(char c) {
		float[] result = null;

		if (isDigit(c)) {
			result = mDigitBounds[getDigitIndex(c)];
		}

		return result;
	}

	@Override
	public float[] getGlyphMaximalBounds() {
		return mGlyphBounds;
	}

	@Override
	public float getGlyphSeparatorWidth() {
		return mSize * .10f;
	}

	private float boundsX() {
		return mSize * .6f;
	}

	private float boundsY() {
		return mSize * Font.REFERENCE_HEIGHT;
	}

	private float borderThickness() {
		return mThickness / 2f;
	}

	private float innerBoxStartX() {
		return borderThickness();
	}

	private float innerBoxStartY() {
		return borderThickness();
	}

	private float innerBoxWidth() {
		return boundsX() - mThickness;
	}

	private float innerBoxHeight() {
		return boundsY() - mThickness;
	}

	private float innerBoxEndX() {
		return innerBoxStartX() + innerBoxWidth();
	}

	private float innerBoxEndY() {
		return innerBoxStartY() + innerBoxHeight();
	}

	private float[] createGlyphPointsArray() {
		return new float[getPointsCount()];
	}

	private float[] getZeroBounds() {
		return new float[]{getZeroInnerBoxWidth() + 2 * borderThickness(), boundsY()};
	}

	private float getZeroInnerBoxWidth() {
		return innerBoxWidth();
	}

	private float[] getZero() {
		float width = getZeroInnerBoxWidth();

		float halfHeight = innerBoxHeight() / 2;
		float halfWidth = width / 2;

		float controlPointX = halfWidth / 2 + halfWidth / 4;
		float controlPointY = halfHeight / 2;

		float p1x = innerBoxStartX();
		float p1y = innerBoxStartY() + halfHeight;

		float p2x = innerBoxStartX() + halfWidth;
		float p2y = innerBoxStartY();

		float p3x = innerBoxStartX() + width;
		float p3y = innerBoxStartY() + halfHeight;

		float p4x = innerBoxStartX() + halfWidth;
		float p4y = innerBoxStartY() + innerBoxHeight();

		float p5x = p1x;
		float p5y = p1y;

		final float[] points = createGlyphPointsArray();

		int index = 0;
		index = DrawingHelper.addPoint(points, index, p1x, p1y);
		index = DrawingHelper.addBezierCurve(points, index, p1x, p1y - controlPointY, p2x - controlPointX, p2y, p2x, p2y);
		index = DrawingHelper.addBezierCurve(points, index, p2x + controlPointX, p2y, p3x, p3y - controlPointY, p3x, p3y);
		index = DrawingHelper.addBezierCurve(points, index, p3x, p3y + controlPointY, p4x + controlPointX, p4y, p4x, p4y);
		/*index =*/
		DrawingHelper.addBezierCurve(points, index, p4x - controlPointX, p4y, p5x, p5y + controlPointY, p5x, p5y);

		return points;
	}

	private float[] getOneBounds() {
		return new float[]{getOneInnerWidth() + 2 * borderThickness(), boundsY()};
	}

	private float getOneInnerWidth() {
		return innerBoxWidth() / 2;
	}

	private float[] getOne() {
		float width = getOneInnerWidth();

		float p1x = innerBoxStartX();
		float p1y = innerBoxStartY();

		float p2x = innerBoxStartX() + width;
		float p2y = p1y;

		float p3x = p2x;
		float p3y = innerBoxStartY() + innerBoxHeight() + borderThickness();

		float soft1x = p2x;
		float soft1y = (p2y + p3y) / 2 * (1f / 3);

		float soft2x = p2x;
		float soft2y = (p2y + p3y) / 2 * (2f / 3);

		final float[] points = createGlyphPointsArray();

		int index = 0;
		index = DrawingHelper.addPoint(points, index, p1x, p1y);
		index = DrawingHelper.addBezierStraightLine(points, index, p2x, p2y);
		index = DrawingHelper.addBezierStraightLine(points, index, soft1x, soft1y);
		index = DrawingHelper.addBezierStraightLine(points, index, soft2x, soft2y);
		/*index =*/
		DrawingHelper.addBezierStraightLine(points, index, p3x, p3y);

		return points;
	}

	private float[] getTwoBounds() {
		return new float[]{getTwoInnerWidth() + 2 * borderThickness(), boundsY()};
	}

	private float getTwoInnerWidth() {
		return innerBoxWidth();
	}

	private float[] getTwo() {
		float width = getTwoInnerWidth();
		float radius = width / 2;

		float p1x = innerBoxStartX();
		float p1y = innerBoxStartY() + radius;

		float p2x = innerBoxStartX() + width;
		float p2y = p1y;

		float p3x = p2x;
		float p3y = p2y + width * (1f / 20);

		float p4x = innerBoxStartX();
		float p4y = innerBoxStartY() + innerBoxHeight();

		float p5x = innerBoxStartX() + width;
		float p5y = innerBoxStartY() + innerBoxHeight();

		float controlSegmentHalfCircle = 4 / 3f * radius;

		final float[] points = createGlyphPointsArray();

		int index = 0;
		index = DrawingHelper.addPoint(points, index, p1x, p1y);
		index = DrawingHelper.addBezierCurve(points, index, p1x, p1y - controlSegmentHalfCircle, p2x, p2y - controlSegmentHalfCircle, p2x, p2y);
		index = DrawingHelper.addBezierStraightLine(points, index, p3x, p3y);
		index = DrawingHelper.addBezierStraightLine(points, index, p4x, p4y);
		/*index =*/
		DrawingHelper.addBezierStraightLine(points, index, p5x, p5y);

		return points;
	}

	private float[] getThreeBounds() {
		return new float[]{getThreeInnerWidth() + 2 * borderThickness(), boundsY()};
	}

	private float getThreeInnerWidth() {
		return innerBoxWidth();
	}

	private float[] getThree() {
		float width = getThreeInnerWidth();

		float radius = width / 2;

		float p1x = innerBoxStartX();
		float p1y = innerBoxStartY() + radius;

		float p2x = innerBoxStartX() + radius;
		float p2y = innerBoxStartY() + innerBoxHeight() / 2;

		float p3x = innerBoxStartX();
		float p3y = innerBoxStartY() + innerBoxHeight() - radius;

		// Between p1 and p2
		float soft1x = innerBoxStartX() + width;
		float soft1y = p1y;

		// Between p2 and p3
		float soft2x = innerBoxStartX() + width;
		float soft2y = p3y;

		float controlSegmentHalfCircle = 4 / 3f * radius;
		float controlSegmentQuadrant = KAPPA * radius;

		final float[] points = createGlyphPointsArray();

		int index = 0;
		index = DrawingHelper.addPoint(points, index, p1x, p1y);
		index = DrawingHelper.addBezierCurve(points, index, p1x, p1y - controlSegmentHalfCircle, soft1x, soft1y - controlSegmentHalfCircle, soft1x, soft1y);
		index = DrawingHelper.addBezierCurve(points, index, soft1x, soft1y + controlSegmentQuadrant, p2x + controlSegmentQuadrant, p2y, p2x, p2y);
		index = DrawingHelper.addBezierCurve(points, index, p2x + controlSegmentQuadrant, p2y, soft2x, soft2y - controlSegmentQuadrant, soft2x, soft2y);
		/*index =*/
		DrawingHelper.addBezierCurve(points, index, soft2x, soft2y + controlSegmentHalfCircle, p3x, p3y + controlSegmentHalfCircle, p3x, p3y);

		return points;
	}


	private float[] getFourBounds() {
		return new float[]{getFiveInnerWidth() + 2 * borderThickness(), boundsY()};
	}

	private float getFourWidth() {
		return innerBoxWidth();
	}

	private float[] getFour() {
		float width = getFourWidth();

		float p1x = innerBoxStartX() + width + borderThickness();
		float p1y = innerBoxStartY() + innerBoxHeight() * (2 / 3f);

		float p2x = innerBoxStartX();
		float p2y = p1y;

		float p3x = innerBoxStartX() + width * (3 / 4f);
		float p3y = innerBoxStartY();

		float p4x = p3x;
		float p4y = innerBoxStartY() + innerBoxHeight() + borderThickness();

		// Between p3 and p4
		float soft1x = p3x;
		float soft1y = p1y;

		final float[] points = createGlyphPointsArray();

		int index = 0;
		index = DrawingHelper.addPoint(points, index, p1x, p1y);
		index = DrawingHelper.addBezierStraightLine(points, index, p2x, p2y);
		index = DrawingHelper.addBezierStraightLine(points, index, p3x, p3y);
		index = DrawingHelper.addBezierStraightLine(points, index, soft1x, soft1y);
		/*index =*/
		DrawingHelper.addBezierStraightLine(points, index, p4x, p4y);

		return points;
	}

	private float[] getFiveBounds() {
		return new float[]{getFiveInnerWidth() + 2 * borderThickness(), boundsY()};
	}

	private float getFiveInnerWidth() {
		return innerBoxWidth();
	}

	private float[] getFive() {
		float width = getFiveInnerWidth();

		float diameter = width;
		float radius = width / 2;

		float p1x = innerBoxStartX() + width;
		float p1y = innerBoxStartY();

		float p2x = innerBoxStartX();
		float p2y = innerBoxStartY();

		float p3x = innerBoxStartX();
		float p3y = innerBoxStartY() + innerBoxHeight() - diameter;

		float p4x = innerBoxStartX();
		float p4y = innerBoxStartY() + innerBoxHeight();

		// Between p3 and p4
		float soft1x = innerBoxStartX() + width;
		float soft1y = p3y + radius;

		final float[] points = createGlyphPointsArray();

		int index = 0;
		index = DrawingHelper.addPoint(points, index, p1x, p1y);
		index = DrawingHelper.addBezierStraightLine(points, index, p2x, p2y);
		index = DrawingHelper.addBezierStraightLine(points, index, p3x, p3y);
		index = DrawingHelper.addBezierCurve(points, index, p3x + radius, p3y, soft1x, soft1y - radius, soft1x, soft1y);
		/*index =*/
		DrawingHelper.addBezierCurve(points, index, soft1x, soft1y + radius, p4x + radius, p4y, p4x, p4y);

		return points;
	}

	private float[] getSixBounds() {
		return new float[]{getSixInnerWidth() + 2 * borderThickness(), boundsY()};
	}

	private float getSixInnerWidth() {
		return innerBoxWidth();
	}

	private float[] getSix() {
		float width = getSixInnerWidth();

		float diameter = width;
		float radius = diameter / 2;
		float halfRadius = radius / 2;

		float p1x = innerBoxStartX() + width;
		float p1y = innerBoxStartY();

		float p2x = innerBoxStartX();
		float p2y = innerBoxStartY() + innerBoxHeight() - radius;

		float p3x = p2x;
		float p3y = p2y;

		// Between p2 and soft2
		float soft1x = innerBoxStartX() + radius;
		float soft1y = innerBoxStartY() + innerBoxHeight();

		// Between soft2 and p3
		float soft2x = innerBoxStartX() + width;
		float soft2y = innerBoxStartY() + innerBoxHeight() - radius;

		final float[] points = createGlyphPointsArray();

		int index = 0;
		index = DrawingHelper.addPoint(points, index, p1x, p1y);
		index = DrawingHelper.addBezierCurve(points, index, p1x / 2, 0, 0, p2y - (p2y / 2), p2x, p2y);
		index = DrawingHelper.addBezierCurve(points, index, p2x, p2y + halfRadius, soft1x - halfRadius, soft1y, soft1x, soft1y);
		index = DrawingHelper.addBezierCurve(points, index, soft1x + halfRadius, soft1y, soft2x, soft2y + halfRadius, soft2x, soft2y);
		// FIXME Not a perfect circle
		/*index =*/
		DrawingHelper.addBezierCurve(points, index, soft2x, soft2y - radius - halfRadius, p3x, p3y - radius - halfRadius, p3x, p3y);

		return points;
	}

	private float[] getSevenBounds() {
		return new float[]{getSevenInnerWidth() + 2 * borderThickness(), boundsY()};
	}

	private float getSevenInnerWidth() {
		return innerBoxWidth();
	}

	private float[] getSeven() {
		float width = getSevenInnerWidth();

		float p1x = innerBoxStartX();
		float p1y = innerBoxStartY();

		float p2x = innerBoxStartX() + width;
		float p2y = innerBoxStartY();

		float p3x = innerBoxStartX();
		float p3y = innerBoxStartY() + innerBoxHeight() + borderThickness();

		// Between p1 and p2
		float soft1x = (p1x + p2x) / 2;
		float soft1y = (p1y + p2y) / 2;

		// Between p2 and p3
		float soft2x = (p2x + p3x) / 2;
		float soft2y = (p2y + p3y) / 2;

		final float[] points = createGlyphPointsArray();

		int index = 0;
		index = DrawingHelper.addPoint(points, index, p1x, p1y);
		index = DrawingHelper.addBezierStraightLine(points, index, soft1x, soft1y);
		index = DrawingHelper.addBezierStraightLine(points, index, p2x, p2y);
		index = DrawingHelper.addBezierStraightLine(points, index, soft2x, soft2y);
		/*index =*/
		DrawingHelper.addBezierStraightLine(points, index, p3x, p3y);

		return points;
	}

	private float[] getEightBounds() {
		return new float[]{getEightInnerWidth() + 2 * borderThickness(), boundsY()};
	}

	private float getEightInnerWidth() {
		return innerBoxWidth();
	}

	private float[] getEight() {
		float width = getEightInnerWidth();
		float halfWidth = width / 2;

		float leftControlPointX = innerBoxStartX() + halfWidth - 4 / 3f * halfWidth;
		float rightControlPointX = innerBoxStartX() + halfWidth + 4 / 3f * halfWidth;

		float p1x = innerBoxStartX() + halfWidth;
		float p1y = innerBoxStartY() + innerBoxHeight() / 2;

		float p2x = innerBoxStartX() + halfWidth;
		float p2y = innerBoxStartY();

		float p3x = p1x;
		float p3y = p1y;

		float p4x = innerBoxStartX() + halfWidth;
		float p4y = innerBoxStartY() + innerBoxHeight();

		float p5x = p1x;
		float p5y = p1y;

		final float[] points = createGlyphPointsArray();

		int index = 0;
		index = DrawingHelper.addPoint(points, index, p1x, p1y);
		index = DrawingHelper.addBezierCurve(points, index, leftControlPointX, p1y, leftControlPointX, p2y, p2x, p2y);
		index = DrawingHelper.addBezierCurve(points, index, rightControlPointX, p2y, rightControlPointX, p3y, p3x, p3y);
		index = DrawingHelper.addBezierCurve(points, index, rightControlPointX, p3y, rightControlPointX, p4y, p4x, p4y);
		/*index =*/
		DrawingHelper.addBezierCurve(points, index, leftControlPointX, p4y, leftControlPointX, p5y, p5x, p5y);

		return points;
	}

	private float[] getNineBounds() {
		return new float[]{getNineInnerWidth() + 2 * borderThickness(), boundsY()};
	}

	private float getNineInnerWidth() {
		return innerBoxWidth();
	}

	private float[] getNine() {
		float width = getNineInnerWidth();

		float diameter = width;
		float radius = diameter / 2;
		float radiusControl = (4 / 3f) * radius;

		float p1x = innerBoxStartX() + width;
		float p1y = innerBoxStartY() + radius;

		float p2x = p1x;
		float p2y = p1y;

		float p3x = innerBoxStartX() + width;
		float p3y = innerBoxStartY() + diameter;

		float p4x = 0;
		float p4y = innerBoxStartY() + innerBoxHeight();

		// Between p1 and p2
		float soft1x = innerBoxStartX();
		float soft1y = innerBoxStartY() + radius;

		final float[] points = createGlyphPointsArray();

		int index = 0;
		index = DrawingHelper.addPoint(points, index, p1x, p1y);
		index = DrawingHelper.addBezierCurve(points, index, p1x, p1y + radiusControl, soft1x, soft1y + radiusControl, soft1x, soft1y);
		index = DrawingHelper.addBezierCurve(points, index, soft1x, soft1y - radiusControl, p2x, p2y - radiusControl, p2x, p2y);
		index = DrawingHelper.addBezierStraightLine(points, index, p3x, p3y);
		// FIXME Doesn't look good
		/*index =*/
		DrawingHelper.addBezierCurve(points, index, p3x, p3y + (diameter * (2 / 3f)), p4x + radius, p4y, p4x, p4y);

		return points;
	}

	@Override
	public void draw(Canvas canvas, float[] array) {
		if (array == null) return;

		mPath.reset();

		int i = 0;
		mPath.moveTo(array[i++], array[i++]);

		final int size = array.length;
		while (i < size) {
			mPath.cubicTo(array[i++], array[i++], array[i++], array[i++], array[i++], array[i++]);
		}

		canvas.drawPath(mPath, mPaint);
	}

	@Override
	public void draw(Canvas canvas, float[] a, float[] b, float percent) {
		if (a == null || b == null || a.length != b.length) return;

		mPath.reset();

		int i = 0;
		mPath.moveTo(DrawingHelper.morph(a[i], b[i++], percent), DrawingHelper.morph(a[i], b[i++], percent));

		final int size = a.length;
		while (i < size) {
			mPath.cubicTo(DrawingHelper.morph(a[i], b[i++], percent), DrawingHelper.morph(a[i], b[i++], percent),
					DrawingHelper.morph(a[i], b[i++], percent), DrawingHelper.morph(a[i], b[i++], percent),
					DrawingHelper.morph(a[i], b[i++], percent), DrawingHelper.morph(a[i], b[i++], percent));
		}

		canvas.drawPath(mPath, mPaint);
	}

	@Override
	public void save(float[] a, float[] b, float percent, float[] result) {
		if (a == null || b == null || result == null || a.length != b.length || a.length > result.length) return;

		final int size = a.length;
		for (int i = 0; i < size; i++) {
			result[i] = DrawingHelper.morph(a[i], b[i], percent);
		}
	}

	@Override
	public void drawColumn(Canvas canvas) {
		float halfHeight = innerBoxHeight() / 2f;
		float halfWidth = getColumnWidth() / 2f;
		float radius = mThickness / 2f;

		canvas.drawCircle(halfWidth, halfHeight, radius, mPaint);
		canvas.drawCircle(halfWidth, innerBoxHeight() - radius, radius, mPaint);
	}

	@Override
	public  float getColumnWidth() {
		return mColumnWidth;
	}

	@Override
	public float computeWidth(float[] a, float[] b, float percent) {
		if (a == null || b == null) return 0;

		return DrawingHelper.morph(a[0], b[0], percent);
	}

	@Override
	public void saveWidth(float[] a, float[] b, float percent, float[] result) {
		if (a == null || b == null || result == null) return;

		result[0] = DrawingHelper.morph(a[0], b[0], percent);
	}
}
