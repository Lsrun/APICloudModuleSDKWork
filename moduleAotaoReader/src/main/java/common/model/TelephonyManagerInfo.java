package common.model;

public class TelephonyManagerInfo {

	/**
	 * mac地址
	 */
	private String macAddress;

	/**
	 * 手机号
	 */
	private String line1Number;

	private String phoneIp;

	/**
	 * 手机设备ID
	 */
	private String deviceid;

	/**
	 * SIM卡的序号
	 */
	private String simSerialNumber;

	/**
	 * 用户Id
	 */
	private String subscriberId;

	/**
	 * 运营商
	 */
	private String providersName;

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getLine1Number() {
		return line1Number;
	}

	public void setLine1Number(String line1Number) {
		this.line1Number = line1Number;
	}

	public String getPhoneIp() {
		return phoneIp;
	}

	public void setPhoneIp(String phoneIp) {
		this.phoneIp = phoneIp;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getSimSerialNumber() {
		return simSerialNumber;
	}

	public void setSimSerialNumber(String simSerialNumber) {
		this.simSerialNumber = simSerialNumber;
	}

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	public String getProvidersName() {
		return providersName;
	}

	public void setProvidersName(String providersName) {
		this.providersName = providersName;
	}

}