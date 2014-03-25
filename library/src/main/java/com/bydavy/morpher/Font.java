package com.bydavy.morpher;

import android.graphics.Canvas;

/**
 * Font is an immutable object.
 * Be careful to not edit arrays that are exposed by this class (encapsulation is not respected
 * for performance reasons)
 */
public interface Font {

	public static final float REFERENCE_HEIGHT = 1;

	/**
	 * Test if the font supports the character
	 *
	 * @param c character
	 * @return true if character supported, false otherwise
	 */
	boolean hasGlyph(char c);

	/**
	 * Returns the number of points defining each glyph. This is a constant per font
	 * in order to generate smooth transitions.
	 *
	 * @return
	 */
	int getPointsCount();

	/**
	 * Returns an array of points to draw (x1, y1, x2, y2, etc).
	 * <p/>
	 * The first point is the starting point and then each point is prefixed by two control
	 * points (Bezier curves).
	 *
	 * @param c character
	 * @return array of points
	 */
	float[] getGlyphPoints(char c);

	/**
	 * Return the glyph's bounds
	 *
	 * @param c character
	 * @return x, y
	 */
	float[] getGlyphBounds(char c);

	/**
	 * Return the biggest glyph's bounds
	 *
	 * @return x, y
	 */
	float[] getGlyphMaximalBounds();

	/**
	 * Return the space that should be added between each character
	 * <p/>
	 * In the current implementation the space in-between character is fixed.
	 * Maybe in the future I will push it to the glyph definition.
	 * <p/>
	 *
	 * @return
	 */
	float getGlyphSeparatorWidth();

	void draw(Canvas canvas, float[] array);

	void draw(Canvas canvas, float[] a, float[] b, float percent);

	void save(float[] a, float[] b, float percent, float[] result);

	void drawColumn(Canvas canvas);

	float getColumnWidth();

	float computeWidth(float[] a, float[] b, float percent);

	void saveWidth(float[] floats, float[] mChar, float percent, float[] result);
}
