package com.feicui.utils;

import android.content.Context;
import android.widget.Toast;

public class Toastutil {

	
	
	public static void shortToast(Context context,String text){
		Toast.makeText(context, text, 0).show();
	}
	public static void longToast(Context context,String text){
		Toast.makeText(context, text, 1).show();
	}
}
