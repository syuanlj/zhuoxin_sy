package com.feicui.bean;

import android.graphics.drawable.Drawable;

public class RunningAppInfo {
	private String packageName;// ��װ������
	private String lableName;// �����г�������
	private Drawable icon;// Ӧ��ͼ��
	private long size;// Ӧ�ô�С
	private boolean isClear;// �Ƿ���Ҫ���
	private boolean isSystem;// �Ƿ���ϵͳӦ��

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public boolean isClear() {
		return isClear;
	}

	public void setClear(boolean isClear) {
		this.isClear = isClear;
	}

	public boolean isSystem() {
		return isSystem;
	}

	public void setSystem(boolean isSystem) {
		this.isSystem = isSystem;
	}

	public String getLableName() {
		return lableName;
	}

	public void setLableName(String lableName) {
		this.lableName = lableName;
	}

	@Override
	public String toString() {
		return "RunningAppInfo [packageName=" + packageName + ", lableName="
				+ lableName + ", icon=" + icon + ", size=" + size
				+ ", isClear=" + isClear + ", isSystem=" + isSystem + "]";
	}

}
