package com.bydavy.digitalclock;

import android.app.Activity;
import android.os.Bundle;

public class Main extends Activity {

	private DigitalClockView mDigitalClockView;
	private Time mTime;
	private TimeUpdater mTimeUpdater;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mDigitalClockView = (DigitalClockView) findViewById(R.id.digitalClock);

		mTime = new Time();
		mTimeUpdater = new TimeUpdater(mTime);
		mDigitalClockView.setTime(mTime);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mTimeUpdater.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mTimeUpdater.stop();
	}
}
