package com.example.tenwang.android_metro.teng;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.tenwang.android_metro.R;

public class MainActivity extends AppCompatActivity {
	private int currentCount =0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
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
				if (((Integer) ShareData.data().mGridLayout.getChildAt(i).getTag()) == currentCount)
					ShareData.data().mGridLayout.getChildAt(i).setAlpha(100);
			}
			currentCount++;

		}
		return true;
	}
}
