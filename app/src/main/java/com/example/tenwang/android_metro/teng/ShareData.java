package com.example.tenwang.android_metro.teng;

import android.widget.GridLayout;

public class ShareData {
	public static ShareData shareData;
	public static ShareData data() {
		if (shareData == null)
			shareData = new ShareData();
		return shareData;
	}
	public GridLayout mGridLayout;
	public int viewMax;
}
