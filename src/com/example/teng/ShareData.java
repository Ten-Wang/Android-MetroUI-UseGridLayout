package com.example.teng;

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
	public int viewMax;
}
