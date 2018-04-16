package com.ysy15350.moduleaotaoreader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ysy15350.moduleaotaoreader.util.CommonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import common.string.MD5Util;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 购买activity
 */

public class BuyActivity extends Activity {
    ImageView imageView;
    TextView tvTitle;
    TextView tvQuRen;
    TextView tvBookName;
    TextView tvZhang;
    TextView tvOld;
    TextView tvNew;
    TextView tvUser;
    TextView tvPrice;
    CheckBox checkBox;
    CheckBox checkBoxRed;

    int aid;
    int cid;
    int uid;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mo_aotao_reader_buy_layout);
        imageView = (ImageView) findViewById(R.id.image_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvQuRen = (TextView) findViewById(R.id.tv_qr);
        tvBookName = (TextView) findViewById(R.id.buy_book_name);
        tvZhang = (TextView) findViewById(R.id.tv_buy_zhang);
        tvOld = (TextView) findViewById(R.id.tv_old_price);
        tvNew = (TextView) findViewById(R.id.tv_new_price);
        tvUser = (TextView) findViewById(R.id.tv_buy_user);
        tvPrice = (TextView) findViewById(R.id.tv_yu);
        checkBox = (CheckBox) findViewById(R.id.check_box);
        checkBoxRed = (CheckBox) findViewById(R.id.check_red);
        tvQuRen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getQuRen();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        aid = intent.getIntExtra("aid", 0);
        cid = intent.getIntExtra("cid", 0);
        uid = intent.getIntExtra("uid", 0);
        url = intent.getStringExtra("url");

        ReadActivity.log("跳转的uid=====" + uid);

        getDataFromUrl();

    }

    String autoBuy;
    String isRed;

    private void getQuRen() {
        if (tvBookName.getText().toString().equals("")) {
            return;
        } else {
            if (checkBox.isChecked()) {
                autoBuy = "1";
                Config.saveDing(this, uid, aid, true);
            } else {
                Config.saveDing(this, uid, aid, false);
                autoBuy = "0";
            }

            if (checkBoxRed.isChecked()) {
                isRed = "1";
            } else {
                isRed = "0";
            }

            queRenBuy();
        }
    }


    /**
     * 确认购买
     */
    private void queRenBuy() {
        String getUrl = url + "/buychapter.php?aid=" + aid + "&cid=" + cid + "&uid=" + uid + "&autobuy="
                + autoBuy + "&buymode=" + isRed + "&act=buy&sign=" + getSign() + "&ajaxapp=1&ajax_request=1";

        ReadActivity.log( "wwwwww====" + getUrl);

        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(getUrl).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();

                ReadActivity.log( "wwww" + str);

                Message message = handler.obtainMessage();
                message.what = 456;
                message.obj = str;
                handler.sendMessage(message);
            }
        });
    }


    /**
     * 获取sign
     *
     * @return sign字符串
     */
    private String getSign() {
        String sign = CommonUtil.getMd5Sign_sendreview(aid, cid, uid);
        return sign;

    }


    private void getDataFromUrl() {

        String getUrl = url + "/vipchapter.php?aid=" + aid + "&cid=" + cid + "&uid=" + uid;

        ReadActivity.log( "wwwwww====" + getUrl);

        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(getUrl).get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();

                ReadActivity.log( "wwww" + str);

                Message message = handler.obtainMessage();
                message.what = 123;
                message.obj = str;
                handler.sendMessage(message);
            }
        });

    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String str = (String) msg.obj;
            switch (msg.what) {
                case 123:
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        int code = jsonObject.optInt("status");
                        if (code == 1) {
                            tvBookName.setText(jsonObject.optString("articlename"));
                            tvZhang.setText(jsonObject.optString("chaptername"));
                            String danwei = getResources().getString(R.string.page_sanyechongbi);
                            tvOld.setText(jsonObject.optString("yjsaleprice") + danwei);
                            tvNew.setText(jsonObject.optString("saleprice") + danwei);
                            tvUser.setText(jsonObject.optString("username"));
                            tvPrice.setText(jsonObject.optString("useremoney"));
                            tvTitle.setText(jsonObject.optString("articlename"));
                            cid = jsonObject.optInt("cid");
                            aid = jsonObject.optInt("aid");
                            uid = jsonObject.optInt("uid");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case 456:
                    try {
                        JSONObject jsonObject = new JSONObject(str);
                        int code = jsonObject.optInt("status");
                        String string = jsonObject.optString("msg");
                        Toast.makeText(BuyActivity.this, string, Toast.LENGTH_SHORT).show();
                        if (code == 1) {

                            Intent data = new Intent();
                            data.putExtra("aid", aid);
                            data.putExtra("cid", cid);
                            setResult(RESULT_OK, data);

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    BuyActivity.this.finish();
                                }
                            }, 1500);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}
