package com.example.teng;

import android.app.Fragment;
import android.content.ClipData;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class TFragment extends Fragment {

	int curid = 0;
	int changeid = 0;
	View curView;
	private int smallSize = 1;
	private int bigSize = 2;
	private int columnCount = 4;
	private int rowCount = 6;
	int viewMax = ShareData.data().viewMax = 24;
	GridLayout gl;
	View[] mViews;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);

		DisplayMetrics metrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		gl = ShareData.data().gl= new GridLayout(getActivity());
		mViews = ShareData.data().mViews = new View[viewMax];
		
		Log.i("Ten", "This deview pixels X:" + metrics.widthPixels + " Y:"
				+ metrics.heightPixels);
		gl.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		gl.setOrientation(0);
		gl.setColumnCount(columnCount);
		gl.setRowCount(rowCount);

		for (int i = 0; i < viewMax; i++) {
			View view = inflater.inflate(R.layout.tview, null);
			TextView t = (TextView) view.findViewById(R.id.textView1);
			t.setText("" + i);
			mViews[i] = view;// new View(MainActivity.this);
			mViews[i].setLayoutParams(new LayoutParams(metrics.widthPixels
					/ columnCount, metrics.heightPixels / rowCount));
			if (i % 5 == 0)
				mViews[i].setBackgroundColor(Color.BLUE);
			else if (i % 7 == 0)
				mViews[i].setBackgroundColor(Color.RED);
			else if (i % 3 == 0)
				mViews[i].setBackgroundColor(Color.GRAY);
			else if (i % 11 == 0 || i % 13 == 0 || i % 19 == 0)
				mViews[i].setBackgroundColor(Color.BLACK);	
			else if (i % 3 == 1)
				mViews[i].setBackgroundColor(Color.CYAN);	
			else
				mViews[i].setBackgroundColor(Color.GREEN);		
			
			mViews[i].setAlpha(0);
			gl.addView(mViews[i]);
		}
		ScrollView scrollView = new ScrollView(getActivity());
		scrollView.addView(gl);
		Log.i("Ten",
				"scrollView: scrollView.getHeight():" + scrollView.getHeight()
						+ "scrollView.getWidth():" + scrollView.getWidth());

		for (int i = 0; i < viewMax; i++) {

			mViews[i].setTag(i);

			mViews[i].setOnTouchListener(new MyTouchListener());
			mViews[i].setOnDragListener(new View.OnDragListener() {

				@Override
				public boolean onDrag(View v, DragEvent event) {
					Log.i("Drag", "DragEvent:" + event.getAction());
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
						if (v.getAlpha() == 0)
							break;
						changeid = (Integer) v.getTag();
						if (changeid != curid)
							getActivity().runOnUiThread(new Runnable() {
								public void run() {
									Log.i("Ten", "change a:" + changeid
											+ " change d:" + curid);
									SwapView(changeid, curid);

								}
							});
						break;
					case DragEvent.ACTION_DRAG_ENDED:
						Log.i("Ten", "" + v.getTag());
						if (curView == v)
							curView.setAlpha(100);
						curView.findViewById(R.id.button1).setVisibility(
								View.GONE);
						curView.findViewById(R.id.button2).setVisibility(
								View.GONE);
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
		return scrollView;
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			DisplayMetrics metrics = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
			if (curView.getHeight() == metrics.heightPixels / 3
					&& curView.getWidth() == metrics.widthPixels / 2) {
				GridLayout.LayoutParams params = new GridLayout.LayoutParams();
				params.width = metrics.widthPixels / columnCount;
				params.height = metrics.heightPixels / rowCount;
				params.rowSpec = GridLayout.spec(Integer.MIN_VALUE, smallSize);
				params.columnSpec = GridLayout.spec(Integer.MIN_VALUE,
						smallSize);
				Integer a = (Integer) curView.getTag();
				gl.getChildAt(a).setLayoutParams(params);
			} else {
				Log.i("Ten", "v.getHeight():" + v.getHeight()
						+ "===metrics.widthPixels / 2:" + metrics.heightPixels
						/ 2);
				GridLayout.LayoutParams params = new GridLayout.LayoutParams();

				params.width = metrics.widthPixels / (columnCount / 2);
				params.height = metrics.heightPixels / (rowCount / 2);
				params.rowSpec = GridLayout.spec(Integer.MIN_VALUE, bigSize);
				params.columnSpec = GridLayout.spec(Integer.MIN_VALUE, bigSize);
				Integer a = (Integer) curView.getTag();
				gl.getChildAt(a).setLayoutParams(params);
			}

		}
	};

	public void SwapView(int a, int b) {
		Integer tInteger = (Integer) mViews[b].getTag();
		mViews[b].setTag(mViews[a].getTag());
		mViews[a].setTag(tInteger);
		if (a < b) {
			gl.removeView(mViews[b]);
			gl.removeView(mViews[a]);
			View temp = mViews[b];
			mViews[b] = mViews[a];
			mViews[a] = temp;
			gl.addView(mViews[a], a);
			gl.addView(mViews[b], b);
		} else {
			gl.removeView(mViews[a]);
			gl.removeView(mViews[b]);

			View temp = mViews[b];
			mViews[b] = mViews[a];
			mViews[a] = temp;
			gl.addView(mViews[b], b);
			gl.addView(mViews[a], a);
		}
	}

	Handler handler = new Handler();
	Runnable mLongPressRunnable = new Runnable() {
		@Override
		public void run() {
			if (!isMoved) {
				Button b1 = (Button) curView.findViewById(R.id.button1);
				Button b2 = (Button) curView.findViewById(R.id.button2);
				b1.setVisibility(View.VISIBLE);
				b1.setOnClickListener(listener);
				b2.setVisibility(View.VISIBLE);
				b2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						curView.setAlpha(0);
						Toast.makeText(getActivity(), "Del",
								Toast.LENGTH_SHORT).show();
					}
				});
				isDraged = true;
			}
		}
	};

	// move threshold
	private static final int TOUCH_SLOP = 30;
	private boolean isDraged = false;
	private boolean isMoved;
	private int mLastMotionX, mLastMotionY;

	private final class MyTouchListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent motionEvent) {
			int x = (int) motionEvent.getX();
			int y = (int) motionEvent.getY();
			Log.i("Touch", "motionEvent.getAction()" + motionEvent.getAction());
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				if (isDraged && view == curView) {
					ClipData data = ClipData.newPlainText("", "");
					DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
							curView);
					curView.startDrag(data, shadowBuilder, null, 0);
				} else {
					if (curView != null) {
						curView.findViewById(R.id.button1).setVisibility(
								View.GONE);
						curView.findViewById(R.id.button2).setVisibility(
								View.GONE);

					}
					mLastMotionX = x;
					mLastMotionY = y;
					isMoved = false;
					curid = (Integer) view.getTag();
					curView = view;
					handler.postDelayed(mLongPressRunnable,
							ViewConfiguration.getLongPressTimeout());
				}
				isDraged = false;
				return true;
			} else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {

				if (isMoved)
					return true;
				if (Math.abs(mLastMotionX - x) > TOUCH_SLOP
						|| Math.abs(mLastMotionY - y) > TOUCH_SLOP) {
					// if touch move over slop threshold ,isMoved mark true;
					isMoved = true;
				}
				return true;
			} else {
				isMoved = true;
			}
			return true;
		}
	}
}
