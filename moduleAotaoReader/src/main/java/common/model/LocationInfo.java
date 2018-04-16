package common.model;

public class LocationInfo {

	/**
	 * 错误码：0
	 */
	private int errorCode;

	/**
	 * 定位类型：2
	 */
	private int locationType;

	/**
	 * 经度:106.556413
	 */
	private double longitude;

	/**
	 * 纬度:29.5225597
	 */
	private double latitude;

	/**
	 * 精度（米）：29.0
	 */
	private double accuracy;

	/**
	 * 提供者：lbs
	 */
	private String provider;

	/**
	 * 国家:中国
	 */
	private String country;

	/**
	 * 省:重庆市
	 */
	private String province;

	/**
	 * 市:重庆市
	 */
	private String city;

	/**
	 * 城市编码:023
	 */
	private String cityCode;

	/**
	 * 区:南岸区
	 */
	private String district;

	/**
	 * 区域码:500108
	 */
	private String adCode;

	/**
	 * 地址:重庆市南岸区……
	 */
	private String address;

	/**
	 * 兴趣点:盛大酒店
	 */
	private String poiName;

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public int getLocationType() {
		return locationType;
	}

	public void setLocationType(int locationType) {
		this.locationType = locationType;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAdCode() {
		return adCode;
	}

	public void setAdCode(String adCode) {
		this.adCode = adCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPoiName() {
		return poiName;
	}

	public void setPoiName(String poiName) {
		this.poiName = poiName;
	}

	// if (location.getErrorCode() == 0) {
	// sb.append("定位成功" + "\n");
	// sb.append("定位类型: " + location.getLocationType() + "\n");
	// sb.append("经 度 : " + location.getLongitude() + "\n");
	// sb.append("纬 度 : " + location.getLatitude() + "\n");
	// sb.append("精 度 : " + location.getAccuracy() + "米" + "\n");
	// sb.append("提供者 : " + location.getProvider() + "\n");
	//
	// jd = location.getLongitude();
	// wd = location.getLatitude();
	//
	// if (ACache.aCache != null) {
	// ACache.aCache.put("jd", jd + "");
	// ACache.aCache.put("wd", wd + "");
	// }
	//
	// if
	// (location.getProvider().equalsIgnoreCase(android.location.LocationManager.GPS_PROVIDER))
	// {
	// // 以下信息只有提供者是GPS时才会有
	// sb.append("速 度 : " + location.getSpeed() + "米/秒" + "\n");
	// sb.append("角 度 : " + location.getBearing() + "\n");
	// // 获取当前提供定位服务的卫星个数
	// sb.append("星 数 : " + location.getExtras().getInt("satellites", 0) +
	// "\n");
	// } else {
	// // 提供者是GPS时是没有以下信息的
	// sb.append("国 家 : " + location.getCountry() + "\n");
	// sb.append("省 : " + location.getProvince() + "\n");
	// sb.append("市 : " + location.getCity() + "\n");
	// sb.append("城市编码 : " + location.getCityCode() + "\n");
	// sb.append("区 : " + location.getDistrict() + "\n");
	// sb.append("区域 码 : " + location.getAdCode() + "\n");
	// sb.append("地 址 : " + location.getAddress() + "\n");
	// sb.append("兴趣点 : " + location.getPoiName() + "\n");
	// }
	// } else {
	// // 定位失败
	// sb.append("定位失败" + "\n");
	// sb.append("错误码:" + location.getErrorCode() + "\n");
	// sb.append("错误信息:" + location.getErrorInfo() + "\n");
	// sb.append("错误描述:" + location.getLocationDetail() + "\n");
	// }

}
