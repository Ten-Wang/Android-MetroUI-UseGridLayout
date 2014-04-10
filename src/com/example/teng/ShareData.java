package com.example.teng;

import android.view.View;
import android.widget.GridLayout;

public class ShareData {
	//the singleton
	public static ShareData shareData;
	public static ShareData data() {
		if (shareData == null)
			shareData = new ShareData();
		return shareData;
	}
	public GridLayout mGridLayout;
	public View[] mViews;
	public int viewMax;
}
