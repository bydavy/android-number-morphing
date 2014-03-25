package com.bydavy.digitalclock;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.bydavy.morpher.DigitalClockView;
import com.bydavy.morpher.font.DFont;

import java.text.SimpleDateFormat;

public class Main extends Activity implements SystemClockManager.SystemClockListener {

	private DigitalClockView mDigitalClockView;
	private SystemClockManager mSystemClockManager;
	private SimpleDateFormat mSimpleDateFormat;
	private DigitalClockView mDigitalClockViewPadding;
	private DigitalClockView mDigitalClockViewBig;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mDigitalClockView = (DigitalClockView) findViewById(R.id.digitalClock);

		mDigitalClockViewPadding = (DigitalClockView) findViewById(R.id.digitalClockPadding);
		mDigitalClockViewPadding.setFont(new DFont(120, 10));
		//mDigitalClockViewPadding.setFontSize(100);

		mDigitalClockViewBig = (DigitalClockView) findViewById(R.id.digitalClockBig);
		//mDigitalClockViewBig.setMorphingDuration(1200);

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
		String formattedTime = mSimpleDateFormat.format(time);

		if (mDigitalClockView != null) {
			mDigitalClockView.setTimeNoAnimation(formattedTime);
		}
		if (mDigitalClockViewPadding != null) {
			mDigitalClockViewPadding.setTime(formattedTime);
		}
		if (mDigitalClockViewBig != null) {
			mDigitalClockViewBig.setTime(formattedTime);
		}
	}
}