package com.ysy15350.moduleaotaoreader;

import android.content.Context;

import java.io.IOException;

import base.mvp.BasePresenter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReadPresenter extends BasePresenter<ReadViewInterface> {

    public ReadPresenter(Context context) {
        super(context);

    }


    public void getBook() {
        String url = "http://106.14.4.243/riku/reader/chapter.php";
        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("aid", "1691")
                .add("cid", "20141")
                .add("uid", "17013")
                .add("sign", "4febca4995491650511821e2a933f884")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void getBooknqueue() {
        String url = "http://106.14.4.243/riku/reader/chapter.php?aid=1691&cid=20141&uid=17013&sign=4febca4995491650511821e2a933f884";

        RequestBody body = new FormBody.Builder()
                .add("aid", "1691")
                .add("cid", "20141")
                .add("uid", "17013")
                .add("sign", "4febca4995491650511821e2a933f884")
                .build();


        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                System.out.println(str);
                mView.chapterCallback(response);
                System.out.println("我是异步线程,线程Id为:" + Thread.currentThread().getId());
            }
        });
        for (int i = 0; i < 10; i++) {
            System.out.println("我是主线程,线程Id为:" + Thread.currentThread().getId());
            try {
                Thread.currentThread().sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

}
