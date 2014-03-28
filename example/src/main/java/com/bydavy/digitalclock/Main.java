package com.bydavy.digitalclock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Main extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		findViewById(R.id.bigBlockButton).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Main.this, SimpleBigClock.class);
				startActivity(intent);
			}
		});

		findViewById(R.id.slowBigClockButton).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Main.this, SimpleBigClock.class);
				intent.putExtra(SimpleBigClock.EXTRA_MORPHING_DURATION, 3000);
				startActivity(intent);
			}
		});

		findViewById(R.id.multipleClocksButton).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Main.this, MultipleClocks.class);
				startActivity(intent);
			}
		});
	}

}
