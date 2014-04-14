package com.example.teng;

import android.support.v4.app.Fragment;
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
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class TFragment extends Fragment {
	enum shape {
		SmallSquare, BigSquare, Rectangle
	}
	int currentId = 0;
	int changeId = 0;
	View currentView;
	
	private int smallSize = 1;
	private int bigSize = 2;
	private int columnCount = 4;
	private int rowCount = 6;
	
	int viewMax = ShareData.data().viewMax = 24;
	
	GridLayout mGridLayout;
	View[] mViews;
	DisplayMetrics metrics;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);

		metrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
		Log.i("Ten", "This deview pixels X:" + metrics.widthPixels + " Y:"
				+ metrics.heightPixels);
		mGridLayout = ShareData.data().mGridLayout= new GridLayout(getActivity());
		mViews = ShareData.data().mViews = new View[viewMax];
		GridLayoutInit(inflater);
		ScrollView scrollView = new ScrollView(getActivity());
		scrollView.addView(mGridLayout);
		return scrollView;
	}
	
	
	
	private void GridLayoutInit(LayoutInflater inflater) {
		mGridLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		mGridLayout.setOrientation(0);
		mGridLayout.setColumnCount(columnCount);
		mGridLayout.setRowCount(rowCount);

		for (int i = 0; i < viewMax; i++) {
			View view = inflater.inflate(R.layout.metro_view, null);
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
			
			mGridLayout.addView(mViews[i]);
						
			TextView tv = (TextView) view.findViewById(R.id.textView1);
			tv.setText("" + i);
			mViews[i].setTag(i);
			mViews[i].setOnTouchListener(new MyTouchListener());
			mViews[i].setOnDragListener(new MyDramGridLayoutistener());
		}
	}

	private final class MyDramGridLayoutistener implements OnDragListener {

		@Override
		public boolean onDrag(View v, DragEvent event) {
			Log.i("Drag", "DragEvent:" + event.getAction());
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				currentView.setAlpha(0);
				break;
			case DragEvent.ACTION_DRAG_LOCATION:
				// Log.i("Ten", "eventX = " + event.getX());
				// Log.i("Ten", "eventY = " + event.getY());
				break;
			case DragEvent.ACTION_DROP:
				Log.i("Ten", "ACTION_DROP:");
				if (v.getAlpha() == 0)
					break;
				changeId = (Integer) v.getTag();
				if (changeId != currentId)
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							Log.i("Ten", "change a:" + changeId
									+ " change d:" + currentId);
							SwapView(changeId, currentId);

						}
					});
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				Log.i("Ten", "" + v.getTag());
				if (currentView == v)
					currentView.setAlpha(100);
				currentView.findViewById(R.id.LeftTopbtn).setVisibility(
						View.GONE);
				currentView.findViewById(R.id.RightBottombtn).setVisibility(
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
	}
	/////////////////////////////////Click Touch Event Control////////////////////////////
	private GridLayout.LayoutParams setlayout(shape s) {
		GridLayout.LayoutParams params = new GridLayout.LayoutParams();
		if (s == shape.BigSquare) {
			params.width = metrics.widthPixels / 2;
			params.height = metrics.heightPixels / 3;
			params.rowSpec = GridLayout.spec(Integer.MIN_VALUE, bigSize);
			params.columnSpec = GridLayout.spec(Integer.MIN_VALUE, bigSize);
			
		} else if (s == shape.Rectangle) {
			params.width = metrics.widthPixels / 2;
			params.height = metrics.heightPixels / 6;
			params.rowSpec = GridLayout.spec(Integer.MIN_VALUE, smallSize);
			params.columnSpec = GridLayout.spec(Integer.MIN_VALUE, bigSize);
		} else {
			params.width = metrics.widthPixels / 4;
			params.height = metrics.heightPixels / 6;
			params.rowSpec = GridLayout.spec(Integer.MIN_VALUE, smallSize);
			params.columnSpec = GridLayout.spec(Integer.MIN_VALUE, smallSize);
		}
		return params;
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Integer a = (Integer) currentView.getTag();
			if (currentView.getHeight() == metrics.heightPixels / 3
					&& currentView.getWidth() == metrics.widthPixels / 2) {
				mGridLayout.getChildAt(a).setLayoutParams(
						setlayout(shape.SmallSquare));
			} else if (currentView.getHeight() == metrics.heightPixels / 6
					&& currentView.getWidth() == metrics.widthPixels / 2) {
				mGridLayout.getChildAt(a).setLayoutParams(
						setlayout(shape.BigSquare));
			} else {
				mGridLayout.getChildAt(a).setLayoutParams(
						setlayout(shape.Rectangle));
			}

		}
	};

	public void SwapView(int a, int b) {
		Integer tInteger = (Integer) mViews[b].getTag();
		mViews[b].setTag(mViews[a].getTag());
		mViews[a].setTag(tInteger);
		if (a < b) {
			mGridLayout.removeView(mViews[b]);
			mGridLayout.removeView(mViews[a]);
			View temp = mViews[b];
			mViews[b] = mViews[a];
			mViews[a] = temp;
			mGridLayout.addView(mViews[a], a);
			mGridLayout.addView(mViews[b], b);
		} else {
			mGridLayout.removeView(mViews[a]);
			mGridLayout.removeView(mViews[b]);

			View temp = mViews[b];
			mViews[b] = mViews[a];
			mViews[a] = temp;
			mGridLayout.addView(mViews[b], b);
			mGridLayout.addView(mViews[a], a);
		}
	}


	Handler _handler = new Handler();
	Runnable mLongPressRunnable = new Runnable() {
		@Override
		public void run() {
			if (!isMoved) {
				Button btn1 = (Button) currentView.findViewById(R.id.LeftTopbtn);
				Button btn2 = (Button) currentView.findViewById(R.id.RightBottombtn);
				btn1.setVisibility(View.VISIBLE);
				btn1.setOnClickListener(listener);
				btn2.setVisibility(View.VISIBLE);
				btn2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						currentView.setAlpha(0);
						for(int i = currentId ; i < viewMax -1 ;i++ )
							SwapView(i, i+1);

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
			if(view.getAlpha() == 0)
				return false;
			int x = (int) motionEvent.getX();
			int y = (int) motionEvent.getY();
			Log.i("Touch", "motionEvent.getAction()" + motionEvent.getAction());
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				if (isDraged && view == currentView) {
					ClipData data = ClipData.newPlainText("", "");
					DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
							currentView);
					currentView.startDrag(data, shadowBuilder, null, 0);
				} else {
					if (currentView != null) {
						currentView.findViewById(R.id.LeftTopbtn).setVisibility(
								View.GONE);
						currentView.findViewById(R.id.RightBottombtn).setVisibility(
								View.GONE);

					}
					mLastMotionX = x;
					mLastMotionY = y;
					isMoved = false;
					currentId = (Integer) view.getTag();
					currentView = view;
					_handler.postDelayed(mLongPressRunnable,
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
					_handler.removeCallbacks(mLongPressRunnable);
				}
				return true;
			} else {
				isMoved = true;
				_handler.removeCallbacks(mLongPressRunnable);
			}
			return true;
		}
	}
}
