package com.bydavy.digitalclock;

import android.app.Activity;
import android.os.Bundle;
import com.bydavy.morpher.DigitalClockView;
import com.bydavy.morpher.font.DFont;

import java.text.SimpleDateFormat;

public class MultipleClocks extends Activity implements SystemClockManager.SystemClockListener {

	private DigitalClockView mDigitalClockView;
	private SystemClockManager mSystemClockManager;
	private SimpleDateFormat mSimpleDateFormat;
	private DigitalClockView mDigitalClockViewPadding;
	private DigitalClockView mDigitalClockViewBig;
	private SimpleDateFormat mShortDateFormat;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiple);

		mDigitalClockView = (DigitalClockView) findViewById(R.id.digitalClock);
		mDigitalClockView.setFont(new DFont(120, 5));

		mDigitalClockViewPadding = (DigitalClockView) findViewById(R.id.digitalClockPadding);
		mDigitalClockViewPadding.setFont(new DFont(100, 10));

		mDigitalClockViewBig = (DigitalClockView) findViewById(R.id.digitalClockBig);
		mDigitalClockViewBig.setFont(new DFont(120, 12));

		mShortDateFormat = new SimpleDateFormat("hh:mm");
		mSimpleDateFormat = new SimpleDateFormat("hh:mm:ss");
		mSystemClockManager = new SystemClockManager(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSystemClockManager.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSystemClockManager.stop();
	}

	@Override
	public void onTimeChanged(long time) {
		String shortFormattedTime = mShortDateFormat.format(time);
		String formattedTime = mSimpleDateFormat.format(time);

		mDigitalClockView.setTimeNoAnimation(formattedTime);
		mDigitalClockViewPadding.setTime(formattedTime);
		mDigitalClockViewBig.setTime(formattedTime);
	}
}
