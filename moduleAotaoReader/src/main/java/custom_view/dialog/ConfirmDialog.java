package custom_view.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysy15350.moduleaotaoreader.R;

import common.CommFunAndroid;

/**
 * 确认对话框
 *
 * @author yangshiyou
 */
public class ConfirmDialog extends Dialog {

    private Context mContext;

    private View conentView;

    String mTitle, mContent, mOkText, mCancelText;

    ImageView img_icon;

    TextView tv_title, tv_content;
    Button btn_ok, btn_cancel;

    private View ll_close;

    public ConfirmDialog(Context context) {
        this(context, "系统提示", "是否确定？", "确定", "取消");
    }

    public ConfirmDialog(Context context, String content) {
        this(context, "系统提示", content, "确定", "取消");
    }

    int mImgId = 0;

    public ConfirmDialog(Context context, String title, String content, String okText, String cancelText, int imgId) {
        super(context);

        mContext = context;
        mTitle = title;
        mContent = content;
        mOkText = okText;
        mCancelText = cancelText;

        mImgId = imgId;

        initView();// 初始化按钮事件
    }


    public ConfirmDialog(Context context, String title, String content, String okText, String cancelText) {
        this(context, title, content, okText, cancelText, 0);
    }

    private void initView() {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        conentView = inflater.inflate(R.layout.mo_aotao_reader_dialog_confirm, null);
        tv_title = (TextView) conentView.findViewById(R.id.tv_title);
        img_icon = (ImageView)conentView.findViewById(R.id.img_icon);
        tv_content = (TextView)conentView.findViewById(R.id.tv_content);

        ll_close = conentView.findViewById(R.id.ll_close);
        btn_cancel = (Button)conentView.findViewById(R.id.btn_cancel);
        btn_ok = (Button)conentView.findViewById(R.id.btn_ok);

        if (!CommFunAndroid.isNullOrEmpty(mTitle)) {
            tv_title.setText(mTitle);
        }
        if (!CommFunAndroid.isNullOrEmpty(mContent)) {
            tv_content.setText(mContent);
        }
        if (mImgId != 0) {
            try {
                img_icon.setVisibility(View.VISIBLE);
                img_icon.setImageResource(mImgId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!CommFunAndroid.isNullOrEmpty(mOkText)) {
            btn_ok.setText(mOkText);
        }
        if (!CommFunAndroid.isNullOrEmpty(mCancelText)) {
            btn_cancel.setText(mCancelText);
        }

        ll_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mListener != null) {
                    mListener.onCancelClick();
                }
                dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mListener != null) {
                    mListener.onCancelClick();
                }
                dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mListener != null) {
                    mListener.onOkClick();
                }
                dismiss();
            }
        });

        // WindowManager.LayoutParams params = this.getWindow().getAttributes();
        // params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        // params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        // dialog.getWindow().setBackgroundDrawable(new
        // ColorDrawable(android.R.color.transparent));
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b0000000")));
        this.setCanceledOnTouchOutside(false);

        // 解决圆角黑边
        // getWindow().setBackgroundDrawable(new BitmapDrawable());
        // 或者
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(conentView);

    }

    /**
     * 点击监听
     */
    private DialogListener mListener;

    /**
     * 设置popupwindow中按钮监听
     *
     * @param listener
     */
    public void setDialogListener(DialogListener listener) {
        this.mListener = listener;
    }

    /**
     * 点击事件监听
     *
     * @author yangshiyou
     */
    public interface DialogListener {

        public void onCancelClick();

        public void onOkClick();

    }

}
