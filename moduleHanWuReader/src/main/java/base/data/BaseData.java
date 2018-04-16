package base.data;

import android.content.Context;

import com.ysy15350.moduleaotaoreader.ReadActivity;

import java.io.ObjectStreamException;

import base.model.UserInfo;
import common.cache.ACache;

public class BaseData {


    private static ACache aCache;


    /**
     * 是否登录
     */
    private boolean isLogin = false;

    String token;

    /**
     * 是否有网络
     */
    public static boolean isNetwork;

    private final static String TAG = "BaseData";

    private BaseData() {
    }

    public static BaseData getInstance() {
        return BaseDataHolder.sInstance;
    }

    public static BaseData getInstance(Context context) {
        init(context);
        return BaseDataHolder.sInstance;
    }


    private static class BaseDataHolder {
        private static final BaseData sInstance = new BaseData();
    }

    // 杜绝单例对象在反序列化时重新生成对象
    private Object readResolve() throws ObjectStreamException {
        return BaseDataHolder.sInstance;
    }

    private static void init(Context context) {
        if (aCache == null && context != null) {
            aCache = ACache.get(context);
        }
    }

    /**
     * 获取token
     *
     * @return
     */
    public String getToken() {

        return "";

    }


    /**
     * 设置token
     *
     * @param token
     */
    public static void setToken(String token) {

    }


    /**
     * 获取当前用户登录信息
     *
     * @return
     */
    public UserInfo getUserInfo() {

        return null;
    }

    /**
     * 缓存已登录用户信息
     *
     * @param userInfo
     */
    public void setUserInfo(UserInfo userInfo) {

        try {
            //UserHelper.getInstance().delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置UID
     *
     * @param uid
     */
    public void setUid(int uid) {

    }

    /**
     * 获取Uid
     *
     * @return
     */
    public int getUid() {

        return 0;
    }


    /**
     * 设置缓存
     *
     * @param key
     * @param value
     */
    public static void setCache(String key, String value) {
        if (aCache != null && value != null) {
            aCache.put(key, value);
        }
    }

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     */
    public static void setCache(String key, String value, int time) {
        if (aCache != null && value != null) {
            aCache.put(key, value, time);
        }
    }

    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    public static String getCache(String key) {
        if (aCache != null) {
            return aCache.getAsString(key);
        }
        return "";
    }

}