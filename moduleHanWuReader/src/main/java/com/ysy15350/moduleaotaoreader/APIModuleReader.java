package com.ysy15350.moduleaotaoreader;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.google.hwgson.Gson;
import com.uzmap.pkg.uzcore.UZWebView;
import com.uzmap.pkg.uzcore.uzmodule.UZModule;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;
import com.ysy15350.moduleaotaoreader.db.DbUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import common.CommFun;


/**
 * ApiCloud 主入口
 */
public class APIModuleReader extends UZModule {

    public APIModuleReader(UZWebView webView) {
        super(webView);

    }

    public APIModuleReader() {
        super(null);

    }


    /**
     * <strong>函数</strong><br><br>
     * 该函数映射至Javascript中moduleDemo对象的startActivity函数<br><br>
     * <strong>JS Example：</strong><br>
     * moduleDemo.startActivity(argument);
     *
     * @param moduleContext (Required)
     */
    public void jsmethod_startReadActivity(UZModuleContext moduleContext) {

        String url = moduleContext.optString("url");
        String uid = moduleContext.optString("uid");
        String aid = moduleContext.optString("aid");
        String cid = moduleContext.optString("cid");
        String bookPath = moduleContext.optString("bookPath");
        String isDebug = moduleContext.optString("isDebug");

        String type = moduleContext.optString("type");



        JSONObject ret = new JSONObject();

        try {
            if (CommFun.isNullOrEmpty(uid) || CommFun.isNullOrEmpty(aid)) {

                showDialog("系统提示", "参数错误");
                try {
                    ret.put("buttonIndex", 1);
                    ret.put("status", 0);
                    ret.put("msg", "参数错误");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                moduleContext.success(ret, true);

                return;
            }


            ret.put("buttonIndex", 1);
            ret.put("status", 1);
            ret.put("msg", "启动成功");

            moduleContext.success(ret, true);

            registerBoradcastReceiver();

            String path = "";

            if (!CommFun.isNullOrEmpty(bookPath)) {
                path = makeRealPath(bookPath);
            }

            if (CommFun.isNullOrEmpty(path)) {
                path = makeRealPath("fs://reader/");
            }

            Intent intent = new Intent(getContext(), ReadActivity.class);
            intent.putExtra("url", url);
            intent.putExtra("uid", uid);
            intent.putExtra("aid", aid);
            intent.putExtra("cid", cid);
            intent.putExtra("path", path);
            intent.putExtra("isDebug", isDebug);
            intent.putExtra("type", type);

            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取点击次数
     *
     * @param moduleContext 对象
     */
    public void jsmethod_getCount(UZModuleContext moduleContext) {

        String aid = moduleContext.optString("aid");
        String cid = moduleContext.optString("cid");
        int type = moduleContext.optInt("type");

        if (!aid.equals("") && !cid.equals("")) {

            if (type == 1) {
                // 获取次数数据
                int num = DbUtil.getInstence(moduleContext.getContext()).getCountByAid(aid, cid);

                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("aid", aid);
                    jsonObject.put("cid", cid);
                    jsonObject.put("count", num);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                moduleContext.success(jsonObject, true);

            } else if (type == 0) {
                DbUtil.getInstence(moduleContext.getContext()).clearCount(aid, cid);

                JSONObject jsonObject = new JSONObject();

                try {

                    jsonObject.put("msg", "清除成功");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                moduleContext.success(jsonObject, true);
            }

        } else {

            if (type == 1) {

                List<DbUtil.Info> list = DbUtil.getInstence(getContext()).getAllCount();
                Gson gson = new Gson();
                String json = gson.toJson(list);

                try {
                    JSONObject jsonObject = new JSONObject(json);
                    moduleContext.success(jsonObject, true);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (type == 0) {

                DbUtil.getInstence(moduleContext.getContext()).clearAllCount();

                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("msg", "清除所有成功");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                moduleContext.success(jsonObject, true);

            }

        }
    }


    public void jsmethod_funAbc(UZModuleContext moduleContext, JSONObject jsonObject) {
        moduleContext.success(jsonObject, true);

    }


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_NAME)) {
                String type = intent.getStringExtra("type");
                String savepath = intent.getStringExtra("savepath");
                String msg = intent.getStringExtra("msg");

                try {

                    JSONObject ret = new JSONObject();

                    ret.put("status", 1);
                    ret.put("type", type);
                    ret.put("savepath", savepath);
                    ret.put("msg", "success");
                    sendEventToHtml5("moduleAotaoReader", ret);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    };


    public static String ACTION_NAME = "moduleaotaoreaderACtion";

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_NAME);
        getContext().registerReceiver(mBroadcastReceiver, myIntentFilter);
    }


    private AlertDialog.Builder mAlert;

    private void showDialog(String title, String msg) {
        if (mAlert == null)
            mAlert = new AlertDialog.Builder(mContext);
        mAlert.setTitle(title);
        mAlert.setMessage(msg);
        mAlert.setCancelable(false);
        mAlert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mAlert = null;
            }
        });
        mAlert.show();
    }


    /**
     * <strong>函数</strong><br><br>
     * 该函数映射至Javascript中moduleDemo对象的showAlert函数<br><br>
     * <strong>JS Example：</strong><br>
     * moduleDemo.showAlert(argument);
     *
     * @param moduleContext (Required)
     */
    public void jsmethod_showAlert(final UZModuleContext moduleContext) {
        if (null != mAlert) {
            return;
        }
        String showMsg = moduleContext.optString("msg");
        mAlert = new AlertDialog.Builder(mContext);
        mAlert.setTitle("这是标题");
        mAlert.setMessage(showMsg);
        mAlert.setCancelable(false);
        mAlert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mAlert = null;
                JSONObject ret = new JSONObject();
                try {
                    ret.put("buttonIndex", 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                moduleContext.success(ret, true);
            }
        });
        mAlert.show();
    }
}
