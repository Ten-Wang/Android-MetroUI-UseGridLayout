package com.example.teng;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	private int cur_count =0;


	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_setting) {
			for (int i = 0; i < ShareData.data().viewMax; i++) {
				// Log.i("Ten","((String)gl.getChildAt(i).getTag()"+((String)gl.getChildAt(i).getTag()));
				if (((Integer) ShareData.data().gl.getChildAt(i).getTag()) == cur_count)
					ShareData.data().gl.getChildAt(i).setAlpha(100);
			}
			cur_count++;

		}
		return true;
	}
}
