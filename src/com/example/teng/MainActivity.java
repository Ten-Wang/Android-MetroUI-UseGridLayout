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

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		Log.i("Ten", "此裝置銀幕大小為 " + metrics.widthPixels + " X "
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
//		View v = new View(this);
//		GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//		params.width = metrics.widthPixels / 2;
//		params.height = metrics.heightPixels / 3;
//		params.rowSpec = GridLayout.spec(Integer.MIN_VALUE, 2);
//		params.columnSpec = GridLayout.spec(Integer.MIN_VALUE, 2);
//		v.setLayoutParams(params);
//		v.setBackgroundColor(Color.MAGENTA);
//
//		// //if number % 4 == 3 測試2X2
//		text[11] = v;
//		View temp = text[12];
//		text[12] = text[11];
//		text[11] = temp;
//		gl.removeViewAt(12);
//		gl.removeViewAt(11);
//
//		gl.addView(text[11], 11);
//		gl.addView(text[12], 12);

		for (int i = 0; i < 24 ; i++){
			Bundle tag =new Bundle();
			tag.putInt("index", i);
			text[i].setTag(tag);
			if (i == 3) {
				text[i].setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						ClipData data = ClipData.newPlainText("", "");
						DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
								v);
						v.startDrag(data, shadowBuilder, v, 0);
						v.setVisibility(View.INVISIBLE);
						runOnUiThread(new Runnable() {
							public void run() {
								SwapView(1, 6);
							}
						});
						return false;
					}
				});
			} else {
				text[i].setOnTouchListener(new MyTouchListener());
				
				text[i].setOnDragListener(new View.OnDragListener() {

					@Override
					public boolean onDrag(View v, DragEvent event) {
						// TODO Auto-generated method stub
						switch (event.getAction()) {
						case DragEvent.ACTION_DRAG_ENDED:
//							changeid = ((Bundle)v.getTag()).getInt("index");
//							if(changeid != curid )
//								runOnUiThread(new Runnable() {
//									public void run() {
//										Log.i("Ten","change a:"+changeid+" change d:"+curid);
//										SwapView(changeid, curid);
//									}
//								});
							Log.i("chauster", "ACTION_DRAG_ENDED");
							break;
						case DragEvent.ACTION_DRAG_LOCATION:
							
							Log.i("chauster","tag:"+((Bundle)v.getTag()).getInt("index"));
							//Log.i("chauster", "eventX = " + event.getX());
							//Log.i("chauster", "eventY = " + event.getY());
							break;
						case DragEvent.ACTION_DROP:
							changeid = ((Bundle)v.getTag()).getInt("index");
							if(changeid != curid )
								runOnUiThread(new Runnable() {
									public void run() {
										Log.i("Ten","change a:"+changeid+" change d:"+curid);
										SwapView(changeid, curid);
									}
								});
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

	public void SwapView(int a, int b) {
		gl.removeView(text[b]);
		gl.removeView(text[a]);
		View temp = text[b];
		text[b] = text[a];
		text[a] = temp;
		gl.addView(text[a], a);
		gl.addView(text[b], b);
//		curid = changeid;
		
	}

	private final class MyTouchListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent motionEvent) {

			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
						view);
				view.startDrag(data, shadowBuilder, view, 0);
				curid = ((Bundle)view.getTag()).getInt("index");
				//view.setVisibility(View.INVISIBLE);
				return true;
			}
			else {
				//view.setVisibility(View.VISIBLE);
			}
			return true;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	// gl.setOnTouchListener(new OnTouchListener() {
	//
	// @Override
	// public boolean onTouch(View v, MotionEvent ev) {
	// if (mVelocityTracker == null) {
	// mVelocityTracker = VelocityTracker.obtain();
	// }
	// mVelocityTracker.addMovement(ev);
	//
	// final int action = ev.getAction();
	//
	// switch (action & MotionEvent.ACTION_MASK) {
	// case MotionEvent.ACTION_DOWN: {
	//
	// break;
	// }
	// case MotionEvent.ACTION_MOVE: {
	// int x = (int)ev.getX();
	// int y = (int)ev.getY();
	// Log.i("Ten","x:"+x+" y:"+y);
	// int l = x - v.getWidth()/2;
	// int t = y - v.getHeight()/2;
	// int r = x + v.getWidth()/2;
	// int b = y + v.getHeight()/2;
	// Log.i("Ten","l:"+l+" t:"+t+" r:"+r+" b:"+b);
	// v.layout(l,t,r,b);
	// break;
	// }
	// case MotionEvent.ACTION_UP: {
	// int x = (int)ev.getX();
	// int y = (int)ev.getY();
	// int l = x - v.getWidth()/2;
	// int t = y - v.getHeight()/2;
	// final int r = x + v.getWidth()/2;
	// final int b = y + v.getHeight()/2;
	// Log.i("Ten","l:"+l+" t:"+t+" r:"+r+" b:"+b);
	// v.layout(l,t,r,b);
	// break;
	// }
	// case MotionEvent.ACTION_CANCEL:
	// break;
	// }
	// return false;
	// }
	// });
}
