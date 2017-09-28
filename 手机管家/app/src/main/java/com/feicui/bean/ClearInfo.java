package com.feicui.bean;

import java.io.File;

import android.graphics.drawable.Drawable;

public class ClearInfo {
	private String softChinesename;
	private String apkname;
	private String filepath;
	private boolean isChecked;
	private long fileSize;
	private File file;
	private Drawable icon;

	public ClearInfo(String softChinesename, String apkname, String filepath,
			boolean isChecked, long fileSize, Drawable icon, File file) {
		super();
		this.softChinesename = softChinesename;
		this.apkname = apkname;
		this.filepath = filepath;
		this.isChecked = isChecked;
		this.fileSize = fileSize;
		this.icon = icon;
		this.file = file;
	}

	public String getSoftChinesename() {
		return softChinesename;
	}

	public void setSoftChinesename(String softChinesename) {
		this.softChinesename = softChinesename;
	}

	public String getApkname() {
		return apkname;
	}

	public void setApkname(String apkname) {
		this.apkname = apkname;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "ClearInfo [softChinesename=" + softChinesename + ", apkname="
				+ apkname + ", filepath=" + filepath + ", isChecked="
				+ isChecked + ", fileSize=" + fileSize + ", file=" + file
				+ ", icon=" + icon + "]";
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
