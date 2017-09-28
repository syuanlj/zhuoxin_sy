package com.feicui.bean;

import android.graphics.drawable.Drawable;

public class SoftList {

	private String name;
	private Drawable icon;
	private String packageName;
	private String time;
	private boolean isChear;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public boolean isChear() {
		return isChear;
	}
	public void setChear(boolean isChear) {
		this.isChear = isChear;
	}
	@Override
	public String toString() {
		return "SoftList [name=" + name + ", icon=" + icon + ", packageName="
				+ packageName + ", time=" + time + ", isChear=" + isChear + "]";
	}
	
}
