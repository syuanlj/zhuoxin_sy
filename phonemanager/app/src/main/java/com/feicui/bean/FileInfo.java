package com.feicui.bean;

import java.io.File;

public class FileInfo {
	private String fileSize;
	private String lastTime;
	private String fileName;
	private String iconID;
	private boolean ischecked;
	private File file;
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getLastTime() {
		return lastTime;
	}
	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getIconID() {
		return iconID;
	}
	public void setIconID(String iconID) {
		this.iconID = iconID;
	}
	public boolean isIschecked() {
		return ischecked;
	}
	public void setIschecked(boolean ischecked) {
		this.ischecked = ischecked;
	}
	
	@Override
	public String toString() {
		return "FileInfo [fileSize=" + fileSize + ", lastTime=" + lastTime
				+ ", fileName=" + fileName + ", iconID=" + iconID
				+ ", ischecked=" + ischecked + ", file=" + file + "]";
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	
}
