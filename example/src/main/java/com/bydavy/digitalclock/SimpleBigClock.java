package com.bydavy.digitalclock;

import android.app.Activity;
import android.os.Bundle;
import com.bydavy.morpher.DigitalClockView;
import com.bydavy.morpher.font.DFont;

import java.text.SimpleDateFormat;

public class SimpleBigClock extends Activity implements SystemClockManager.SystemClockListener {

	private DigitalClockView mDigitalClockView;
	private SystemClockManager mSystemClockManager;
	private SimpleDateFormat mSimpleDateFormat;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_big_clock);

		mDigitalClockView = (DigitalClockView) findViewById(R.id.digitalClock);
		mDigitalClockView.setFont(new DFont(130, 10));

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

		mDigitalClockView.setTime(formattedTime);
	}
}
