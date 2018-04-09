package common.model;

public class ScreenInfo {

	/**
	 * 屏幕密度
	 */
	public float density = 1;

	/**
	 * 屏幕宽度
	 */
	public int width = 480;

	/**
	 * 屏幕高度
	 */
	public int height = 800;

	public float getDensity() {
		return density;
	}

	public void setDensity(float density) {
		this.density = density;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}