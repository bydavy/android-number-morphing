package com.bydavy.digitalclock;

import android.os.Handler;

/**
 * Faked while developing. I'm focusing on DigitMorpher for now
 */
public class TimeUpdater {

	private final Time mTime;
	private final Handler mHandler;

	private final Runnable mNextTime = new Runnable() {
		@Override
		public void run() {
			int time = mTime.getTime();
			time = ++time % 10;
			mTime.setTime(time);
			scheduleNextTime();
		}
	};

	private boolean mStarted;

	public TimeUpdater(Time time) {
		mTime = time;
		mHandler = new Handler();
	}

	private void scheduleNextTime() {
		mHandler.postDelayed(mNextTime, 1000);
	}

	public void start() {
		if (!mStarted) {
			scheduleNextTime();
			mStarted = true;
		}
	}

	public void stop() {
		if (mStarted) {
			mHandler.removeCallbacks(mNextTime);
			mStarted = false;
		}
	}
}
