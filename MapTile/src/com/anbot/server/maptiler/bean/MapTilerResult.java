package com.anbot.server.maptiler.bean;

/**
 * @author suwan
 *
 */
public class MapTilerResult {
	/**
	 * 图片像素宽
	 */
	private int pixelWidth;
	/**
	 * 图片像素高
	 */
	private int pixelHeight;
	/**
	 * 切图最大层
	 */
	private int maxLayer;
	/**
	 * 切图最小层
	 */
	private int minLayer;
	
	public MapTilerResult(int pixelWidth, int pixelHeight, int maxLayer, int minLayer) {
		super();
		this.pixelWidth = pixelWidth;
		this.pixelHeight = pixelHeight;
		this.maxLayer = maxLayer;
		this.minLayer = minLayer;
	}

	public int getPixelWidth() {
		return pixelWidth;
	}

	public void setPixelWidth(int pixelWidth) {
		this.pixelWidth = pixelWidth;
	}

	public int getPixelHeight() {
		return pixelHeight;
	}

	public void setPixelHeight(int pixelHeight) {
		this.pixelHeight = pixelHeight;
	}

	public int getMaxLayer() {
		return maxLayer;
	}

	public void setMaxLayer(int maxLayer) {
		this.maxLayer = maxLayer;
	}

	public int getMinLayer() {
		return minLayer;
	}

	public void setMinLayer(int minLayer) {
		this.minLayer = minLayer;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "MapTilerResult [pixelWidth="+pixelWidth+",pixelHeight="+pixelHeight+",maxLayer="+maxLayer+",minLayer="+minLayer+"]";
	}
	
	
}
