package com.bydavy.digitalclock;


import java.util.ArrayList;
import java.util.List;

/**
 * Faked while developing. I'm focusing on DigitMorpher for now
 */
public class Time {

	public interface TimeListener {
		void onTimeChanged(Time time);
	}

	private int mTime;
	private List<TimeListener> mListeners;
	private List<TimeListener> mListenersTmp;

	public Time() {
		mListeners = new ArrayList<TimeListener>();
		mListenersTmp = new ArrayList<TimeListener>();
	}

	public void addListener(TimeListener listener) {
		mListeners.add(listener);
	}

	public void removeListener(TimeListener listener) {
		mListeners.remove(listener);
	}

	public void setTime(int time) {
		if (mTime != time) {
			mTime = time;
			fireTimeChanged();
		}
	}

	private void fireTimeChanged() {
		// Avoid GC
		mListenersTmp.clear();
		mListenersTmp.addAll(mListeners);

		for (TimeListener l : mListenersTmp) {
			l.onTimeChanged(this);
		}
	}

	public int getTime() {
		return mTime;
	}
}
