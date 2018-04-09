package com.ysy15350.moduleaotaoreader.fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.ysy15350.moduleaotaoreader.R;
import com.ysy15350.moduleaotaoreader.ReadActivity;
import com.ysy15350.moduleaotaoreader.model.BookIntroduce;
import com.ysy15350.moduleaotaoreader.model.ResponseBook;
import com.ysy15350.moduleaotaoreader.util.CommonUtil;

import java.io.IOException;
import java.util.Locale;

import base.BaseFragment;
import base.data.BaseData;
import common.CommFun;
import common.file.FileUtils;
import common.message.MessageBox;
import common.string.JsonConvertor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 书本介绍
 */
public class MainTab4Fragment extends BaseFragment {

    @Override
    public View initFragmentView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.mo_aotao_reader_activity_main_tab4, container, false);
        return view;
    }

    private int mUid = 0;
    private int mAid = 0;

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() != null) {

            mUid = ReadActivity.mUid;
            mAid = ReadActivity.mAid;

//            String fileName = String.format(Locale.US, "bookinfo_%d.txt", mAid);
//            String fullPath = ReadActivity.savePath + "/" + fileName;
//            String chapterlistJson = FileUtils.readFile(fullPath);
//            if (ReadActivity.isDebug) {
//                MessageBox.show("文章读取结果：" + chapterlistJson);
//            }
//            if (CommFun.isNullOrEmpty(chapterlistJson)) {
//                chapterlistJson = BaseData.getCache("bookIntroduceJson" + mAid);
//            }
//            if (CommFun.isNullOrEmpty(chapterlistJson)) {
//
//            } else {
//                getBookIntroduce(chapterlistJson);
//            }


            info(mAid);

        }

    }

    ImageLoader imageLoader;
    private DisplayImageOptions options;

    ImageView img_cover;
    TextView tvFriend;

    @Override
    public void initView() {
        super.initView();
        if (getActivity() != null) {

            // 初始化imageLoader 否则会报错
            imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
            options = new DisplayImageOptions.Builder()
                    .showStubImage(R.mipmap.mo_aotao_reader_icon_loading) // 设置图片下载期间显示的图片
                    .showImageForEmptyUri(R.mipmap.mo_aotao_reader_icon_not_found) // 设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(R.mipmap.mo_aotao_reader_icon_not_found) // 设置图片加载或解码过程中发生错误显示的图片
                    .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                    .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                    .build();

            img_cover = (ImageView) mContentView.findViewById(R.id.img_cover);

            View ll_isfriend = mHolder.getView(R.id.ll_isfriend);

            tvFriend = mHolder.getView(R.id.tv_isfriend);

            if (ll_isfriend != null) {
                ll_isfriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mUid == 0 || mUid == -1) {

                            MessageBox.show("请登录");

                        } else {
                            addfriends();
                        }

                    }
                });
            }

        }
    }


    /**
     * 获取书籍介绍
     *
     * @param jsonStr json数据
     */
    private void getBookIntroduce(String jsonStr) {
        try {
            if (!CommFun.isNullOrEmpty(jsonStr)) {
                BookIntroduce bookIntroduce = JsonConvertor.fromJson(jsonStr, BookIntroduce.class);
                bindBookIntroduce(bookIntroduce);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    BookIntroduce mBookIntroduce;

    /**
     * 绑定图书介绍
     *
     * @param bookIntroduce
     */
    private void bindBookIntroduce(BookIntroduce bookIntroduce) {

        try {
            mBookIntroduce = bookIntroduce;
            if (bookIntroduce == null) {
                if (mHolder != null) {
                    mHolder.setText(R.id.tv_articlename, "-")
                            .setText(R.id.tv_notice, "-")
                            .setText(R.id.tv_intro, "-")
                            .setText(R.id.tv_sort, "-")
                            .setText(R.id.tv_type, "-");
                }

            } else {

                String cover = bookIntroduce.getCover();

                showWebImage(cover, img_cover);

                int isfriend = bookIntroduce.getIsfriend(); //0:未关注 1:已关注
                int fullflag = bookIntroduce.getFullflag();
                int firstflag = bookIntroduce.getFirstflag();
                int issignvip = bookIntroduce.getIssignvip();

                if (mHolder != null) {
                    mHolder.setText(R.id.tv_articlename, bookIntroduce.getArticlename())//作品名
                            .setText(R.id.tv_notice, bookIntroduce.getNotice())//公告
                            .setText(R.id.tv_intro, bookIntroduce.getIntro())//简介
                            .setText(R.id.tv_sort, bookIntroduce.getSort())//一级分类
                            .setText(R.id.tv_type, bookIntroduce.getType())//二级分类
                            .setText(R.id.tv_goodnum, bookIntroduce.getGoodnum())//收藏数
                            .setText(R.id.tv_allvote, bookIntroduce.getAllvote())//推荐数
                            .setText(R.id.tv_allvisit, bookIntroduce.getAllvisit())//访问数
                            .setText(R.id.tv_giftnum, bookIntroduce.getGiftnum())//礼物数
                            .setText(R.id.tv_allvipvote, bookIntroduce.getAllvipvote())//月票数
                            .setText(R.id.tv_reviewsnum, bookIntroduce.getReviewsnum()); //评论数

                    if (mUid == 0 || mUid == -1) {

                        mHolder.setTextColor(R.id.tv_isfriend, R.color.text_content);
                        mHolder.setText(R.id.tv_isfriend, "关注");

                    } else {

                        switch (isfriend) {
                            case 0:
                                //未关注
                                mHolder.setTextColor(R.id.tv_isfriend, R.color.text_content);
                                mHolder.setText(R.id.tv_isfriend, "关注");
                                break;
                            case 1:
                                //已关注
                                mHolder.setTextColor(R.id.tv_isfriend, R.color.blue);
                                mHolder.setText(R.id.tv_isfriend, "已关注");

                                break;
                        }
                    }

                    switch (fullflag) {
                        case 0:
                            mHolder.setVisibility_VISIBLE(R.id.tv_fullflag).setText(R.id.tv_fullflag, "连载");
                            break;
                        case 1:
                            mHolder.setVisibility_VISIBLE(R.id.tv_fullflag).setText(R.id.tv_fullflag, "完结");
                            break;
                        default:
                            mHolder.setVisibility_GONE(R.id.tv_fullflag);
                            break;
                    }

                    switch (firstflag) {
                        case 0:
                            mHolder.setVisibility_VISIBLE(R.id.tv_firstflag).setText(R.id.tv_firstflag, "他站首发");
                            break;
                        case 1:
                            mHolder.setVisibility_VISIBLE(R.id.tv_firstflag).setText(R.id.tv_firstflag, "本站首发");
                            break;
                        default:
                            mHolder.setVisibility_GONE(R.id.tv_firstflag);
                            break;
                    }

                    switch (issignvip) {
                        case 0:
                            mHolder.setVisibility_VISIBLE(R.id.tv_issignvip).setText(R.id.tv_issignvip, "一般作品");
                            break;
                        case 1:
                            mHolder.setVisibility_VISIBLE(R.id.tv_issignvip).setText(R.id.tv_issignvip, "vip作品");
                            break;
                        case 10:
                            mHolder.setVisibility_VISIBLE(R.id.tv_issignvip).setText(R.id.tv_issignvip, "签约作品");
                            break;
                        default:
                            mHolder.setVisibility_GONE(R.id.tv_issignvip);
                            break;
                    }

                    if (CommFun.isNullOrEmpty(bookIntroduce.getNature1())) {
                        mHolder.setVisibility_GONE(R.id.tv_nature1);
                    } else {
                        mHolder.setVisibility_VISIBLE(R.id.tv_nature1).setText(R.id.tv_nature1, bookIntroduce.getNature1());
                    }

                    if (CommFun.isNullOrEmpty(bookIntroduce.getNature2())) {
                        mHolder.setVisibility_GONE(R.id.tv_nature2);
                    } else {
                        mHolder.setVisibility_VISIBLE(R.id.tv_nature2).setText(R.id.tv_nature2, bookIntroduce.getNature2());
                    }

                    if (CommFun.isNullOrEmpty(bookIntroduce.getNature3())) {
                        mHolder.setVisibility_GONE(R.id.tv_nature3);
                    } else {
                        mHolder.setVisibility_VISIBLE(R.id.tv_nature3).setText(R.id.tv_nature3, bookIntroduce.getNature3());
                    }

                    if (CommFun.isNullOrEmpty(bookIntroduce.getNature4())) {
                        mHolder.setVisibility_GONE(R.id.tv_nature4);
                    } else {
                        mHolder.setVisibility_VISIBLE(R.id.tv_nature4).setText(R.id.tv_nature4, bookIntroduce.getNature4());
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    OkHttpClient mOkHttpClient = null;


    /**
     * 获取作品简介，传 aid， uid
     *
     */
    private void info(int aid) {

        mOkHttpClient = new OkHttpClient();

        String url = String.format(Locale.CHINA, "%s/info.php?aid=%d&uid=%d", ReadActivity.SERVICE_URL, aid, mUid);

        ReadActivity.log( "作品简介--url--" + url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();

                ReadActivity.log( "作品简介----" + str);

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (!CommFun.isNullOrEmpty(str)) {
                                String fileName = String.format(Locale.CHINA, "bookinfo_%d.txt", mAid);
                                String fullPath = ReadActivity.savePath + "/" + fileName;

                                boolean isSuccess = FileUtils.saveFile(str, ReadActivity.savePath, fileName);

                                if (ReadActivity.isDebug) {
                                    MessageBox.show("文章写入结果：" + isSuccess);
                                }

                                BaseData.setCache("bookIntroduceJson" + mAid, str);

                                getBookIntroduce(str);
                            }


                        }
                    });
                }
            }

        });

    }


    /**
     * 关注与取消关注
     */
    private void addfriends() {

        try {
            if (mBookIntroduce == null) {
                MessageBox.show("未检测到有效书本信息");
            }

            String authorid = mBookIntroduce.getAuthorid();
            int isfriend = mBookIntroduce.getIsfriend();  // 0:未关注 1:已关注

            String act = "delete";
            //add:关注；delete:取消关注
            if (isfriend == 0) {
                // 关注
                act = "add";
            }

            String sign = CommonUtil.getMd5Sign_addfriends(authorid, String.valueOf(mUid)); /// md5(秘钥#作者ID#用户ID)

            if (mOkHttpClient == null) {
                mOkHttpClient = new OkHttpClient();
            }

            String url = String.format(Locale.CHINA, "%s/addfriends.php?authorid=%s&uid=%d&act=%s&ajaxapp=1&ajax_request=1&sign=%s",
                    ReadActivity.SERVICE_URL, authorid, mUid, act, sign);

            ReadActivity.log( "---关注----" + url);

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Call call = mOkHttpClient.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String str = response.body().string();

                    ReadActivity.log( str);

                    try {
                        if (!CommFun.isNullOrEmpty(str)) {
                            ResponseBook responseBook = JsonConvertor.fromJson(str, ResponseBook.class);
                            if (responseBook != null) {
                                final int status = responseBook.getStatus();
                                final String msg = responseBook.getMsg();
                                if (getActivity() != null) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            if (status == 0) {

                                                mHolder.setTextColor(R.id.tv_isfriend, R.color.blue);
                                                mHolder.setText(R.id.tv_isfriend, "已关注");

                                            } else if (status == 1) {

                                                int is = mBookIntroduce.getIsfriend();
                                                if (is == 1) {
                                                    mHolder.setTextColor(R.id.tv_isfriend, R.color.text_content);
                                                    mHolder.setText(R.id.tv_isfriend, "关注");
                                                } else if (is == 0) {
                                                    mHolder.setTextColor(R.id.tv_isfriend, R.color.blue);
                                                    mHolder.setText(R.id.tv_isfriend, "已关注");
                                                }

                                                info(mAid);

                                            }

                                            MessageBox.show(msg);


                                        }
                                    });
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 显示图片
     *
     * @param url
     * @param imageView
     */
    private void showWebImage(final String url, final ImageView imageView) {
        // 调用ImageLoader类的displayImage()方法

        try {
            if (imageLoader != null)
                imageLoader.displayImage(url, imageView, options);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}