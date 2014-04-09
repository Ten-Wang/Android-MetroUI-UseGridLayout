package com.example.teng;

import android.view.View;
import android.widget.GridLayout;

public class ShareData {
	//唯一
	public static ShareData shareData;
	public static ShareData data() {
		if (shareData == null)
			shareData = new ShareData();
		return shareData;
	}
	public GridLayout gl;
	public View[] mViews;
	public int viewMax;
}
