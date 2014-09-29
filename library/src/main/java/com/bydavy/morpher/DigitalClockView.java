package com.bydavy.morpher;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.bydavy.morpher.font.DFont;

public class DigitalClockView extends View {

	public static int DEFAULT_MORPHING_DURATION = 300;

	public static final int DEFAULT_FONT_SIZE = 170;
	public static final int DEFAULT_FONT_THICKNESS = 10;
	public static final Font DEFAULT_FONT = new DFont(DEFAULT_FONT_SIZE, DEFAULT_FONT_THICKNESS);

	private Font mFont;
	private int mMorphingDurationInMs;

	private float mMorphingPercent;

	// Local variables (opposed to mPreviousChars and mChars that hold immutable content "leaked" from the Font object)
	private float[][] mLocalChars;
	private float[][] mLocalWidth;

	// Store previous values (origin)
	private String mPreviousText;
	private float[][] mPreviousChars;
	private float[][] mPreviousWidth;

	// Store new values (destination)
	private String mText;
	private float[][] mChars;
	private float[][] mWidth;

	// FIXME The colon char doesn't fit in my current design
	private boolean[] mIsColonChar;

	private ObjectAnimator mMorphingAnimation;

	public DigitalClockView(Context context) {
		super(context);
		init(context, null, 0);
	}

	public DigitalClockView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, 0);
	}

	public DigitalClockView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}

	private void init(Context context, AttributeSet attrs, int defStyle) {
		if (attrs != null) {
			TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DigitalClockView, 0, 0);
			int textSize = ta.getDimensionPixelSize(R.styleable.DigitalClockView_textSize, DEFAULT_FONT_SIZE);
			int textThickness = ta.getDimensionPixelSize(R.styleable.DigitalClockView_textThickness, DEFAULT_FONT_THICKNESS);
			mFont = new DFont(textSize, textThickness);
			mMorphingDurationInMs = ta.getInt(R.styleable.DigitalClockView_morphingDuration, DEFAULT_MORPHING_DURATION);
			ta.recycle();
		}
		else {
			mFont = DEFAULT_FONT;
			mMorphingDurationInMs = DEFAULT_MORPHING_DURATION;
		}
	}

	public void setTime(String time) {
		setTime(time, true);
	}

	public void setTime(String time, boolean shouldMorph) {
		// Update only if time changed
		if (time == mText || (mText != null && mText.equals(time))) return;

		// Changing text length is not supported (at least not "morphed")
		if (mText == null || (mPreviousText != null && time != null && (mPreviousText.length() != time.length() || colonCharChangedPosition(mPreviousText, time)))) {
			shouldMorph = false;
		}

		int newSize = time.length();
		int pointsCount = mFont.getPointsCount();

		// Here we might reallocate memory (this view is not designed to change frequently)
		mChars = ArrayHelper.expandIfRequired(mChars, newSize);
		mWidth = ArrayHelper.expandIfRequired(mWidth, newSize);

		mPreviousChars = ArrayHelper.expandIfRequired(mPreviousChars, newSize);
		mPreviousWidth = ArrayHelper.expandIfRequired(mPreviousWidth, newSize);

		mLocalChars = ArrayHelper.expandIfRequired(mLocalChars, newSize, pointsCount);
		mLocalWidth = ArrayHelper.expandIfRequired(mLocalWidth, newSize, pointsCount);

		mIsColonChar = ArrayHelper.expandIfRequired(mIsColonChar, newSize);

		// Save current chars and width in order to generate a morphing animation
		if (shouldMorph) {
			float[][] originChars;
			float[][] originWidths;

			if (!isMorphingAnimationRunning()) {
				originChars = mChars;
				originWidths = mWidth;
			} else {
				// Save current morphing animation accordingly to the current state
				// FIXME Ideally we should not rely on the current mMorphingPercent but rather recompute the percentage
				// based on current time - start time / duration
				int size = mText.length();
				for (int i = 0; i < size; i++) {
					// Save result to mLocalChars and mLocalWidth. Both are local array that can be edited (not the case of
					// mPreviousChars, mPreviousWidth, mChars and mWidth).
					mFont.save(mPreviousChars[i], mChars[i], mMorphingPercent, mLocalChars[i]);
					mFont.saveWidth(mPreviousWidth[i], mWidth[i], mMorphingPercent, mLocalWidth[i]);
				}

				originChars = mLocalChars;
				originWidths = mLocalWidth;
			}
			System.arraycopy(originChars, 0, mPreviousChars, 0, mPreviousChars.length);
			System.arraycopy(originWidths, 0, mPreviousWidth, 0, mPreviousWidth.length);
		}

		fetchGlyphs(time, mChars, mWidth, mIsColonChar);

		mPreviousText = mText;
		mText = time;

		cancelMorphingAnimation();

		if (shouldMorph) {
			resetMorphingAnimation();
			startMorphingAnimation();
		}

		requestLayout();
		invalidate();
	}

	private void resetMorphingAnimation() {
		mMorphingPercent = 0;
	}

	private void startMorphingAnimation() {
		mMorphingAnimation = ObjectAnimator.ofFloat(this, "morphingPercent", 1);
		mMorphingAnimation.setInterpolator(new LinearInterpolator());
		mMorphingAnimation.setDuration(mMorphingDurationInMs);
		mMorphingAnimation.start();
	}

	private void cancelMorphingAnimation() {
		if (mMorphingAnimation != null) {
			mMorphingAnimation.cancel();
			mMorphingAnimation = null;
		}
	}

	public boolean isMorphingAnimationRunning() {
		return mMorphingAnimation != null && mMorphingAnimation.isRunning();
	}

	private void fetchGlyphs(String text, float[][] chars, float[][] widths, boolean[] isColumnChar) {
		int size = text.length();
		for (int i = 0; i < size; i++) {
			char c = text.charAt(i);

			// FIXME Column doesn't fit in my current impl
			boolean isColumn = c == ':';
			isColumnChar[i] = isColumn;
			if (isColumn) {
				continue;
			}

			if (!mFont.hasGlyph(c)) {
				throw new RuntimeException("Character not supported " + c);
			}
			chars[i] = mFont.getGlyphPoints(c);
			widths[i] = mFont.getGlyphBounds(c);
		}
	}

	public void setMorphingDuration(int durationInMs) {
		mMorphingDurationInMs = durationInMs;
	}

	public int getMorphingDuration() {
		return mMorphingDurationInMs;
	}

	public void setFont(Font font) {
		//if (mFont != font) {
		mFont = font;
		cancelMorphingAnimation();

		// Force text changed to repopulate internal arrays
		String time = mText;
		mText = null;
		setTime(time);
		//}
	}

	/*public void setFontColor(int color) {
		if (mColor != color) {
			mColor = color;

			invalidate();
		}
	}

	public void setFontSize(int sizeInPx) {
		if (mFontSize != sizeInPx) {
			mFontSize = sizeInPx;

			requestLayout();
			//invalidate();
		}
	}

	public void setFontThickness(int thicknessInPx) {
		if (mThickness != thicknessInPx) {
			mThickness = thicknessInPx;

			requestLayout();
			//invalidate();
		}
	}*/

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMinimumWidth(computeMinWidth());
		setMinimumHeight(computeMinHeight());

		int minW = getPaddingLeft() + getSuggestedMinimumWidth() + getPaddingRight();
		int w = resolveSizeAndState(minW, widthMeasureSpec, 1);

		int minH = getPaddingTop() + getSuggestedMinimumHeight() + getPaddingBottom();
		int h = resolveSizeAndState(minH, heightMeasureSpec, 0);

		setMeasuredDimension(w, h);
	}

	private int computeMinWidth() {
		float x = 0;

		if (mChars != null && mWidth != null) {
			float xSeparator = mFont.getGlyphSeparatorWidth();

			final int size = mChars.length;
			for (int i = 0; i < size; i++) {
				if (!mIsColonChar[i]) {
					if (!isMorphingAnimationRunning()) {
						x += mWidth[i][0];
					} else {
						x += mFont.computeWidth(mPreviousWidth[i], mWidth[i], mMorphingPercent);
					}
				} else {
					x += mFont.getColonWidth();
				}

				if (i < size - 1) {
					x += xSeparator;
				}
			}
		}

		return (int) x;
	}

	private int computeMinHeight() {
		// Only one line supported for now.
		return (int) mFont.getGlyphMaximalBounds()[1];
	}

	@Override
	protected void onDraw(Canvas canvas) {
		float xSeparator = mFont.getGlyphSeparatorWidth();

		canvas.save();

//		Paint redPen = new Paint();
//		redPen.setColor(Color.RED);
//		redPen.setStrokeWidth(1);
//		canvas.drawLine(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getPaddingTop(), redPen);
//		canvas.drawLine(getPaddingLeft(), getHeight() - getPaddingBottom(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom(), redPen);

		canvas.translate(getPaddingLeft(), getPaddingTop());
		if (mChars != null) {
			float charWidth;
			final int size = mChars.length;
			for (int i = 0; i < size; i++) {
				if (!mIsColonChar[i]) {
					if (!isMorphingAnimationRunning()) {
						mFont.draw(canvas, mChars[i]);
						charWidth = mWidth[i][0];
					} else {
						mFont.draw(canvas, mPreviousChars[i], mChars[i], mMorphingPercent);
						charWidth = mFont.computeWidth(mPreviousWidth[i], mWidth[i], mMorphingPercent);
					}
				} else {
					mFont.drawColon(canvas);
					charWidth = mFont.getColonWidth();
				}

				if (i < size - 1) {
					canvas.translate(charWidth + xSeparator, 0);
				}
			}

		}
		canvas.restore();
	}

	@SuppressWarnings("unused")
	public void setMorphingPercent(float percent) {
		mMorphingPercent = percent;

		requestLayout();
		invalidate();
	}

	@SuppressWarnings("unused")
	public float getMorphingPercent() {
		return mMorphingPercent;
	}

	private static boolean colonCharChangedPosition(String previousTime, String time) {
		if (previousTime.length() != time.length()) {
			return true;
		}

		final int length = previousTime.length();
		for (int i = 0; i < length; i++) {
			char previousC = previousTime.charAt(i);
			if (previousC == ':' && previousC != time.charAt(i)) {
				return true;
			}
		}

		return false;
	}
}