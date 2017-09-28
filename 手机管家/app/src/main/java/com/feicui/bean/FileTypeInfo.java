package com.feicui.bean;


public class FileTypeInfo   {

	private long fileSize;
	private String Type;
	private boolean isLoding;
	private long fileTypeAllSize;
	
	
	
	public FileTypeInfo(long fileSize, String type, boolean isLoding) {
		super();
		this.fileSize = fileSize;
		Type = type;
		this.isLoding = isLoding;
		
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public boolean isLoding() {
		return isLoding;
	}
	public void setLoding(boolean isLoding) {
		this.isLoding = isLoding;
	}
	public long getFileTypeAllSize() {
		return fileTypeAllSize;
	}
	public void setFileTypeAllSize(long fileTypeAllSize) {
		this.fileTypeAllSize = fileTypeAllSize;
	}
	@Override
	public String toString() {
		return "FileTypeInfo [fileSize=" + fileSize + ", Type=" + Type
				+ ", isLoding=" + isLoding + ", fileTypeAllSize="
				+ fileTypeAllSize + "]";
	}

	


}
