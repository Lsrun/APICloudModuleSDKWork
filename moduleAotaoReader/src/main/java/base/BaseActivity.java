package base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

//import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import common.AppStatusManager;
import common.CommFunAndroid;
import common.message.MessageBox;
import common.model.RequestPermissionType;


public class BaseActivity extends FragmentActivity implements IView {

    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    protected static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;

    /**
     * 控件ViewGroup
     */
    protected View mContentView;

    protected ViewHolder mHolder;

    /**
     * 界面标题
     */
    protected String mTitle = "";

    /**
     * 是否需要登录
     */
    boolean mNeedLogin = false;


    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ACTIVITY_COUNT++;
        String str = this.toString();
        activityNames.add(str);

        StringBuilder sb = new StringBuilder();

        for (String activityName :
                activityNames) {
            sb.append(activityName + "\n");
        }


        if (1 == 1) {
            //showMsg("不检查");
        } else {
            checkAppStatus();  //如果是重新打开的应用，重新从入口进入，缺陷：不会回到选择照片的页面
        }

        mContentView = getWindow().getDecorView();

        mHolder = ViewHolder.get(this, mContentView);


    }

    public boolean isFirst = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirst) {
            init();
        }

    }


    protected void checkAppStatus() {
        if (AppStatusManager.getInstance().getAppStatus() == AppStatusManager.AppStatusConstant.APP_FORCE_KILLED) {

        }
    }


    /**
     * 初始化，1：initView；2：readCahce；3：loadData；4：bindData
     */
    private void init() {

        initView();

        initData();

        readCahce();

        loadData();

        bindData();

    }

    /**
     * 初始化，1：initView；2：readCahce；3：loadData；4：bindData
     *
     * @param context
     * @param contentView
     * @param title
     * @param isNeedLogin
     */
    public void init(Context context, View contentView, String title, boolean isNeedLogin) {

        mContentView = contentView;
        mTitle = title;
        mNeedLogin = isNeedLogin;


        initView();

        initData();

        readCahce();

        loadData();

        bindData();
    }


    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        // 填充状态栏
        //CommFunAndroid.fullScreenStatuBar(this);
    }

    @Override
    public void readCahce() {
    }

    @Override
    public void loadData() {
    }

    @Override
    public void bindData() {

    }

    protected boolean isLogin() {


        return false;
    }

    @Override
    public void showMsg(String msg) {
        if (CommFunAndroid.isNullOrEmpty(msg))
            return;
        MessageBox.show(msg);
    }

    @Override
    public void showWaitDialog(String msg) {
        if (CommFunAndroid.isNullOrEmpty(msg))
            return;
        // CommFunMessage.showWaitDialog(this, msg);
    }

    @Override
    public void hideWaitDialog() {

        // CommFunMessage.hideWaitDialog();
    }

    @Override
    public void setViewText(int id, CharSequence text) {
        if (mHolder != null)
            mHolder.setText(id, text);
    }

    @Override
    public String getViewText(int id) {
        if (mHolder != null)
            return mHolder.getViewText(id);
        return "";
    }

    @Override
    public void setTextColor(int id, int color) {
        if (mHolder != null)
            mHolder.setTextColor(id, color);
    }

    @Override
    public void setBackgroundColor(int id, int color) {
        if (mHolder != null)
            mHolder.setBackgroundColor(id, color);
    }

    @Override
    public void setVisibility_GONE(int id) {
        if (mHolder != null)
            mHolder.setVisibility_GONE(id);
    }

    @Override
    public void setVisibility_VISIBLE(int id) {
        if (mHolder != null)
            mHolder.setVisibility_VISIBLE(id);

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    /**
     * 申请读取文件权限
     */
    public boolean callReadExtrnalStoreagePermission(Activity activity) {


        //判断Android版本是否大于23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            Log.d(TAG, "callReadExtrnalStoreagePermission() called with: checkCallPhonePermission = [" + checkCallPhonePermission + "]");

            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                        activity
                        , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                        , RequestPermissionType.REQUEST_CODE_ASK_READ_EXTERNAL_STORAGE);
                return false;
            } else {
                return true;
            }
        }

        return true;
    }

    /**
     * 检查是否拥有权限
     *
     * @param thisActivity
     * @param permission
     * @param requestCode
     * @param errorText
     */
    protected void checkPermission(Activity thisActivity, String permission, int requestCode, String errorText) {
        //判断当前Activity是否已经获得了该权限
        if (ContextCompat.checkSelfPermission(thisActivity, permission) != PackageManager.PERMISSION_GRANTED) {
            //如果App的权限申请曾经被用户拒绝过，就需要在这里跟用户做出解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    permission)) {
                Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show();
                //进行权限请求
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{permission},
                        requestCode);
            } else {
                //进行权限请求
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{permission},
                        requestCode);
            }
        } else {

        }
    }


    /**
     * 注册权限申请回调
     *
     * @param requestCode  申请码
     * @param permissions  申请的权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        Log.d(TAG, "onRequestPermissionsResult() called with: requestCode = [" + requestCode + "], permissions = [" + permissions + "], grantResults = [" + grantResults + "]");
        switch (requestCode) {

            case RequestPermissionType.REQUEST_CODE_ASK_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showMsg("你允许读取文件");

                } else {
                    showMsg("你已拒绝读取文件请求");
                }
                break;
            case RequestPermissionType.REQUEST_CODE_ASK_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showMsg("你允许使用相机");

                } else {
                    //showMsg("拒绝");
                    // Permission Denied
                    //Toast.makeText(this, "CALL_PHONE Denied", Toast.LENGTH_SHORT).show();
                    showMsg("您已拒绝系统使用相机权限");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    public static int ACTIVITY_COUNT = 0;
    public static List<String> activityNames = new ArrayList<>();

    @Override
    protected void finalize() throws Throwable {
        ACTIVITY_COUNT--;
        String str = this.toString();
        if (activityNames.contains(str)) {
            activityNames.remove(str);
        }
        Log.d(TAG, "finalize() called" + this + "------------------ACTIVITY_COUNT=" + ACTIVITY_COUNT);
        super.finalize();
    }
}