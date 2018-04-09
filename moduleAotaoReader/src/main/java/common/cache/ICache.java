package common.cache;

public interface ICache {

	/**
	 * 字符串缓存
	 * 
	 * @param key
	 * @param value
	 */
	void put(String key, String value);

	/**
	 * 获取字符串缓存
	 * 
	 * @param key
	 * @return
	 */
	String getAsString(String key);

}
