package com.example.teng;

import android.os.Bundle;
import android.R.color;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	GridLayout gl;
	View[] text;
	int item;
	int curid = 0;
	int changeid = 0;
	View curView;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		Log.i("Ten", "This deview pixels X:" + metrics.widthPixels + " Y:"
				+ metrics.heightPixels);
		gl = new GridLayout(MainActivity.this);
		gl.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		gl.setOrientation(0);
		gl.setColumnCount(4);
		gl.setRowCount(6);
		Log.i("Ten",
				"Bottom:" + gl.getPaddingBottom() + " Left:"
						+ gl.getPaddingLeft() + " Right:"
						+ gl.getPaddingRight() + " Top:" + gl.getPaddingTop());

		text = new View[24];

		for (int i = 0; i < 24; i++) {
			text[i] = new View(MainActivity.this);
			text[i].setLayoutParams(new LayoutParams(metrics.widthPixels / 4,
					metrics.heightPixels / 6));

			if (i % 5 == 0)
				text[i].setBackgroundColor(Color.BLUE);
			else if (i % 7 == 0)
				text[i].setBackgroundColor(Color.RED);
			else if (i % 3 == 0)
				text[i].setBackgroundColor(Color.GRAY);
			else if (i % 11 == 0)
				text[i].setBackgroundColor(Color.CYAN);
			else
				text[i].setBackgroundColor(Color.GREEN);

			gl.addView(text[i]);
		}

		setContentView(gl);

		Log.i("Ten",
				"After=>Bottom:" + gl.getPaddingBottom() + " Left:"
						+ gl.getPaddingLeft() + " Right:"
						+ gl.getPaddingRight() + " Top:" + gl.getPaddingTop());
		// View v = new View(this);
		// GridLayout.LayoutParams params = new GridLayout.LayoutParams();
		// params.width = metrics.widthPixels / 2;
		// params.height = metrics.heightPixels / 3;
		// params.rowSpec = GridLayout.spec(Integer.MIN_VALUE, 2);
		// params.columnSpec = GridLayout.spec(Integer.MIN_VALUE, 2);
		// v.setLayoutParams(params);
		// v.setBackgroundColor(Color.MAGENTA);
		//
		// // //if number % 4 == 3 test 2X2
		// text[11] = v;
		// View temp = text[12];
		// text[12] = text[11];
		// text[11] = temp;
		// gl.removeViewAt(12);
		// gl.removeViewAt(11);
		//
		// gl.addView(text[11], 11);
		// gl.addView(text[12], 12);

		for (int i = 0; i < 24; i++) {

			text[i].setTag(i);
			if (i < 6)
				text[i].setOnClickListener(listener);
			else {
				text[i].setOnTouchListener(new MyTouchListener());
				text[i].setOnDragListener(new View.OnDragListener() {
					
					@Override
					public boolean onDrag(View v, DragEvent event) {
						Log.i("Drag", "DragEvent:"+event.getAction());
						switch (event.getAction()) {
						case DragEvent.ACTION_DRAG_STARTED:
							curView.setAlpha(0);
							break;
						case DragEvent.ACTION_DRAG_LOCATION:

							// Log.i("Ten", "eventX = " + event.getX());
							// Log.i("Ten", "eventY = " + event.getY());
							break;
						case DragEvent.ACTION_DROP:
							Log.i("Ten", "ACTION_DROP:");
							changeid = (Integer) v.getTag();
							if (changeid != curid)
								runOnUiThread(new Runnable() {
									public void run() {
										Log.i("Ten", "change a:" + changeid
												+ " change d:" + curid);
										SwapView(changeid, curid);
										curView.setVisibility(View.VISIBLE);
									}
								});
							break;
						case DragEvent.ACTION_DRAG_ENDED:
							Log.i("Ten",""+v.getTag());
							if(curView == v)
								curView.setAlpha(100);
							break;
						case DragEvent.ACTION_DRAG_ENTERED:
							break;
						case DragEvent.ACTION_DRAG_EXITED:
							Log.i("Ten", "ACTION_DRAG_EXITED:");
							break;
						default:
							break;
						}
						return true;
					}
				});
			}
		}

	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			if (v.getHeight() == metrics.widthPixels / 2) {
				GridLayout.LayoutParams params = new GridLayout.LayoutParams();
				params.width = metrics.widthPixels / 4;
				params.height = metrics.heightPixels / 6;
				params.rowSpec = GridLayout.spec(Integer.MIN_VALUE, 1);
				params.columnSpec = GridLayout.spec(Integer.MIN_VALUE, 1);
				Integer a = (Integer) v.getTag();
				gl.getChildAt(a).setLayoutParams(params);
				gl.getChildAt(a).setOnClickListener(listener);
			} else {
				GridLayout.LayoutParams params = new GridLayout.LayoutParams();

				params.width = metrics.widthPixels / 2;
				params.height = metrics.heightPixels / 3;
				params.rowSpec = GridLayout.spec(Integer.MIN_VALUE, 2);
				params.columnSpec = GridLayout.spec(Integer.MIN_VALUE, 2);
				v.setBackgroundColor(Color.YELLOW);
				Integer a = (Integer) v.getTag();
				gl.getChildAt(a).setLayoutParams(params);
				gl.getChildAt(a).setOnClickListener(listener);
			}

		}
	};

	public void SwapView(int a, int b) {
		Integer tInteger = (Integer) text[b].getTag();
		text[b].setTag(text[a].getTag());
		text[a].setTag(tInteger);
		if (a < b) {
			gl.removeView(text[b]);
			gl.removeView(text[a]);
			View temp = text[b];
			text[b] = text[a];
			text[a] = temp;
			gl.addView(text[a], a);
			gl.addView(text[b], b);
		} else {
			gl.removeView(text[a]);
			gl.removeView(text[b]);

			View temp = text[b];
			text[b] = text[a];
			text[a] = temp;
			gl.addView(text[b], b);
			gl.addView(text[a], a);
		}
	}

	private final class MyTouchListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent motionEvent) {

			Log.i("Touch","motionEvent.getAction()"+motionEvent.getAction());
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
						view);
				curid = (Integer) view.getTag();
				curView = view;
				view.startDrag(data, shadowBuilder, null, 0);

				return true;
			} else {
				return true;
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
