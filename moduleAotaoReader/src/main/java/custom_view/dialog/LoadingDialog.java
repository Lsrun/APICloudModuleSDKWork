package custom_view.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ysy15350.moduleaotaoreader.R;


/**
 * 确认对话框
 *
 * @author yangshiyou
 */
public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        this(context, "数据加载中", true);
    }

    public LoadingDialog(Context context, boolean isCanceledOnTouchOutside) {
        this(context, "数据加载中", isCanceledOnTouchOutside);
    }

    /**
     * 数据加载等待提示
     *
     * @param context                  上下文
     * @param message                  提示内容
     * @param isCanceledOnTouchOutside 点击屏幕是否隐藏
     */
    public LoadingDialog(Context context, String message, boolean isCanceledOnTouchOutside) {
        super(context, R.style.setting_dialog);

        LayoutInflater mInflater = LayoutInflater.from(context);
        ViewGroup rootView = (ViewGroup) mInflater.inflate(R.layout.mo_aotao_reader_dialog_loading, null);

        TextView tv_title = (TextView) rootView.findViewById(R.id.message);
        if (!message.equals(""))
            tv_title.setText(message);

        // 设置内容
        setContentView(rootView);
        // 设置窗口属性
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        // 设置宽度、高度、密度、对齐方式
        float density = getDensity(context);
        params.width = (int) (160 * density);
        params.height = (int) (120 * density);
        params.gravity = Gravity.CENTER;

        this.setCanceledOnTouchOutside(isCanceledOnTouchOutside);

        window.setAttributes(params);
    }

    /**
     * 获取显示密度
     *
     * @param context
     * @return
     */
    public float getDensity(Context context) {
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        return dm.density;
    }
}
