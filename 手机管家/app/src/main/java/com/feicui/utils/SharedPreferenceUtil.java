package com.feicui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceUtil {

	
	/**
	 * 
	 * @param context
	 * @return  显示引导界面的share对象
	 */
	public static SharedPreferences getShowLeadSP(Context context){
		SharedPreferences sp= context.getSharedPreferences("isShowLead",0);
	return sp;
	}
	
	public static Editor getShowLeadED(Context context){
		Editor ed=getShowLeadSP(context).edit();
		return ed;
	}
}
