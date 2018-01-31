package com.example.vip_serve_demo;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class MyListView extends ListView {
	public MyListView(Context context) {
		super(context);
	
	}
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
		                MeasureSpec.AT_MOST);
		        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		    }
	public MyListView(Context context,AttributeSet paramAttributeSet)  
	 {  
	     super(context,paramAttributeSet);  
	 }  
}