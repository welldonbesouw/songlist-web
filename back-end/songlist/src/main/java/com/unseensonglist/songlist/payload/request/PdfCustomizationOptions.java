package com.unseensonglist.songlist.payload.request;

public class PdfCustomizationOptions {

	private String titleOne = "Susunan Lagu";
	private String titleTwo = "";
	private String titleThree = "";	
	private float titleOneSize = 16;
	private float titleTwoSize = 16;
	private float titleThreeSize = 16;
	private float margin = 50;
	private float fontSize = 12;
	private float lineSpacing = 16;

	public String getTitleOne() {
		return titleOne;
	}

	public void setTitleOne(String titleOne) {
		this.titleOne = titleOne;
	}

	public String getTitleTwo() {
		return titleTwo;
	}

	public void setTitleTwo(String titleTwo) {
		this.titleTwo = titleTwo;
	}

	public String getTitleThree() {
		return titleThree;
	}

	public void setTitleThree(String titleThree) {
		this.titleThree = titleThree;
	}

	public float getMargin() {
		return margin;
	}

	public void setMargin(float margin) {
		this.margin = margin;
	}

	public float getTitleOneSize() {
		return titleOneSize;
	}

	public void setTitleOneSize(float titleOneSize) {
		this.titleOneSize = titleOneSize;
	}
	

	public float getTitleTwoSize() {
		return titleTwoSize;
	}

	public void setTitleTwoSize(float titleTwoSize) {
		this.titleTwoSize = titleTwoSize;
	}

	public float getTitleThreeSize() {
		return titleThreeSize;
	}

	public void setTitleThreeSize(float titleThreeSize) {
		this.titleThreeSize = titleThreeSize;
	}

	public float getFontSize() {
		return fontSize;
	}

	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}

	public float getLineSpacing() {
		return lineSpacing;
	}

	public void setLineSpacing(float lineSpacing) {
		this.lineSpacing = lineSpacing;
	}

}
