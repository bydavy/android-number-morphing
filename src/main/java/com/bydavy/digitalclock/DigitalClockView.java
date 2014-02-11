package com.bydavy.digitalclock;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class DigitalClockView extends View implements Time.TimeListener {
	private DigitMorpher mDigitMorpher;
	private Animator mAnimation;

	private float mAnimationPercent;
	private Time mTime;

	public DigitalClockView(Context context) {
		super(context);
		init();
	}

	public DigitalClockView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DigitalClockView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void init() {
		mDigitMorpher = new DigitMorpher();
		setTime(null);
	}

	public void setTime(Time time) {
		if (mTime != null) {
			mTime.removeListener(this);
		}
		mTime = time;
		mAnimationPercent = 1;
		if (time != null) {
			time.addListener(this);
		}
	}

	public float getAnimationPercent() {
		return mAnimationPercent;
	}

	public void setAnimationPercent(float percent) {
		mAnimationPercent = percent;
		invalidate();
	}

	private void startAnimation() {
		if (mAnimation == null) {
			mAnimation = ObjectAnimator.ofFloat(this, "animationPercent", 1);
			mAnimation.setInterpolator(new LinearInterpolator());
			mAnimation.setDuration(300);
			mAnimation.start();
		}
	}

	private void cancelAnimation() {
		if (mAnimation != null) {
			mAnimation.cancel();
			mAnimation = null;
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// FIXME Return right size
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.argb(255, 255, 0, 0));

		canvas.save();

		// FIXME Remove when all numbers implemented
		debugDrawAllNumbers(canvas);

		canvas.translate(getWidth() / 2, getHeight() / 3);
		int time = mTime.getTime();
		if (mAnimationPercent > 0.99999 || time == DigitMorpher.MIN_IMPLEMENTED) { // FIXME animation from 0 to 1 not implemented yet
			mDigitMorpher.draw(canvas, time);
		} else {
			mDigitMorpher.drawAnimation(canvas, time, mAnimationPercent);
		}
		canvas.restore();
	}

	private void debugDrawAllNumbers(Canvas canvas) {
		canvas.save();
		canvas.translate(0, getHeight() / 2);
		for (int i = 0; i < 10; i++) {
			mDigitMorpher.draw(canvas, i);
			canvas.translate(100, 0);
		}
		canvas.restore();
	}

	@Override
	public void onTimeChanged(Time time) {
		mAnimationPercent = 0;
		cancelAnimation();
		startAnimation();
	}
}
