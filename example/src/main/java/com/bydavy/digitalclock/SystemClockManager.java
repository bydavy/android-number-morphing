package com.bydavy.digitalclock;

import android.os.Handler;


public class SystemClockManager {

	public interface SystemClockListener {
		void onTimeChanged(long time);
	}

	private static final long ONE_SEC_IN_MS = 1000;

	private final SystemClockListener mListener;
	private final Handler mHandler;
	private final Runnable mNextTime;

	private boolean mStarted;

	public SystemClockManager(SystemClockListener listener) {
		mListener = listener;
		mHandler = new Handler();

		mNextTime = new Runnable() {
			@Override
			public void run() {
				mListener.onTimeChanged(System.currentTimeMillis());
				scheduleNextTime();
			}
		};
	}

	private void scheduleNextTime() {
		mHandler.postDelayed(mNextTime, ONE_SEC_IN_MS);
	}

	public void start() {
		if (!mStarted) {
			mNextTime.run();
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
