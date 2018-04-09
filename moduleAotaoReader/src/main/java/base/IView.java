package base;

/**
 * Created by yangshiyou on 2016/11/29.
 */

public interface IView {

    /**
     * 初始化数据
     */
    public void initData();

    /**
     * 初始化View
     */
    public void initView();


    /**
     * 读取缓存
     */
    public void readCahce();

    /**
     * 加载数据
     */
    public void loadData();

    /**
     * 绑定数据
     */
    public void bindData();

    /**
     * 消息提示
     *
     * @param msg 消息内容
     */
    public void showMsg(String msg);


    /**
     * 提示框
     *
     * @param msg
     */
    public void showWaitDialog(String msg);

    /**
     * 隐藏提示框
     */
    public void hideWaitDialog();


    /**
     * 设置文本内容
     *
     * @param id   控件id
     * @param text 文本内容
     */
    public void setViewText(int id, CharSequence text);

    /**
     * 获取文本内容
     *
     * @param id 控件id
     */
    public String getViewText(int id);

    /**
     * 设置字体颜色
     *
     * @param id
     * @param color
     */
    public void setTextColor(int id, int color);

    /**
     * 设置背景颜色
     *
     * @param id
     * @param color
     */
    public void setBackgroundColor(int id, int color);


    /**
     * 设置控件不可见
     *
     * @param id
     */
    public void setVisibility_GONE(int id);

    /**
     * 设置控件可见
     *
     * @param id
     */
    public void setVisibility_VISIBLE(int id);


}
