package com.feicui.bean;

import java.io.Serializable;

public class TelType implements Serializable {

	private String name;
	private int idx;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	@Override
	public String toString() {
		return "TelType [name=" + name + ", idx=" + idx + "]";
	}
	
	
}
