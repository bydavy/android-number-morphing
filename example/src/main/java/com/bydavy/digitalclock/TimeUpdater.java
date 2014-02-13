package com.bydavy.digitalclock;

import android.os.Handler;

import java.util.Random;

/**
 * Faked while developing. I'm focusing on DigitMorpher for now
 */
public class TimeUpdater {

	private static final boolean RANDOM = false;

	private final Time mTime;
	private final Handler mHandler;

	private final Runnable mNextTime = new Runnable() {
		@Override
		public void run() {
			int time;
			if (RANDOM) {
				Random r = new Random();
				time = r.nextInt(9);
			}else {
				time = mTime.getTime();
				time = ++time % 10;
			}
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
