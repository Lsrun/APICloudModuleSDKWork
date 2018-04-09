package com.ysy15350.moduleaotaoreader.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Method;
import java.util.Locale;

import common.CommFun;
import common.string.MD5Util;

/**
 * Created by Administrator on 2016/1/17.
 */
public class CommonUtil {

    /**
     * 获取屏幕原始尺寸高度，包括虚拟功能键高度
     *
     * @param context
     * @return
     */
    public static int getDpi(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    /**
     * 获取 虚拟按键的高度
     *
     * @param context
     * @return
     */
    public static int getBottomStatusHeight(Context context) {
        int totalHeight = getDpi(context);

        int contentHeight = getScreenHeight(context);

        return totalHeight - contentHeight;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 标题栏高度
     *
     * @return
     */
    public static int getTitleHeight(Activity activity) {
        return activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    public static int getAPIVersion() {
        int APIVersion;
        try {
            APIVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            APIVersion = 0;
        }
        return APIVersion;
    }

    /**
     * @param context
     * @param px
     * @return
     */
    public static float convertPixelsToDp(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    /**
     * @param context
     * @param dp
     * @return
     */
    public static float convertDpToPixel(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转sp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    public static String subString(String text, int num) {

        String content = "";
        if (text != null) {
            if (text.length() > num) {
                content = text.substring(0, num - 1) + "...";
            } else {
                content = text;
            }
        }

        return content;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */

    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "找不到版本号";
        }
    }

    /**
     * @return 版本号
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 私钥
     */
    public static String PRIVATE_KEY = "cUa3dixDR7nHTcX3gZ5SBHfga04SvW0u";


    /**
     * 获取签名
     *
     * @param aid
     * @param cid
     * @return
     */
    public static String getMd5Sign(int aid, int cid) {
        try {
            if (!CommFun.isNullOrEmpty(PRIVATE_KEY) && aid != 0 && cid != 0) {
                String str = String.format(Locale.CHINA,"%s#%d#%d", PRIVATE_KEY, aid, cid);
                return MD5Util.GetMD5Code(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 关注、取消关注
     *
     * @param authorid
     * @param uid
     * @return
     */
    public static String getMd5Sign_addfriends(String authorid, String uid) {

        try {
            if (!CommFun.isNullOrEmpty(PRIVATE_KEY) && !CommFun.isNullOrEmpty(authorid) && !CommFun.isNullOrEmpty(uid)) {
                String str = String.format("%s#%s#%s", PRIVATE_KEY, authorid, uid);//md5(秘钥#作者ID#用户ID)
                return MD5Util.GetMD5Code(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 想法写入
     *
     * @param aid
     * @param cid
     * @param did 段落id
     * @return
     */
    public static String getMd5Sign_sendreview(int aid, int cid, int did) {

        try {
            if (!CommFun.isNullOrEmpty(PRIVATE_KEY) && aid != 0 && cid != 0 && did != 0) {
                String str = String.format(Locale.CHINA,"%s#%d#%d#%d", PRIVATE_KEY, aid, cid, did);    // md5(秘钥#作品ID#章节ID#段落ID)
                return MD5Util.GetMD5Code(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 自动订阅
     *
     * @param aid
     * @param uid
     * @return
     */
    public static String getMd5Sign_autobuy(int aid, int uid) {

        try {
            if (!CommFun.isNullOrEmpty(PRIVATE_KEY) && aid != 0) {
                String str = String.format(Locale.CHINA,"%s#%d#%d", PRIVATE_KEY, aid, uid);// md5(秘钥#作品ID#用户ID)
                return MD5Util.GetMD5Code(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 打赏
     *
     * @param aid
     * @param type 打赏类型
     * @param uid
     * @return
     */
    public static String getMd5Sign_paygift(int aid, int type, int uid) {

        try {
            if (!CommFun.isNullOrEmpty(PRIVATE_KEY) && aid != 0) {
                String str = String.format("%s#%d#%d#%d", PRIVATE_KEY, aid, type, uid);// md5(秘钥#作品ID#打赏类型ID#用户ID)
                return MD5Util.GetMD5Code(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


}
