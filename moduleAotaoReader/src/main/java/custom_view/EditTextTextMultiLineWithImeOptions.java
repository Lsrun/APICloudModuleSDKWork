package custom_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

/**
 * Created by yangshiyou on 2017/11/24.
 */

//解决文本多行输入，无法设置回车按钮文本问题
public class EditTextTextMultiLineWithImeOptions extends EditText {
    public EditTextTextMultiLineWithImeOptions(Context context) {
        super(context);
    }

    public EditTextTextMultiLineWithImeOptions(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextTextMultiLineWithImeOptions(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EditTextTextMultiLineWithImeOptions(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection connection = super.onCreateInputConnection(outAttrs);
        int imeActions = outAttrs.imeOptions & EditorInfo.IME_MASK_ACTION;
        if ((imeActions & EditorInfo.IME_ACTION_DONE) != 0) {
            // clear the existing action
            outAttrs.imeOptions ^= imeActions;
            // set the DONE action
            outAttrs.imeOptions |= EditorInfo.IME_ACTION_DONE;
        }
        if ((outAttrs.imeOptions & EditorInfo.IME_FLAG_NO_ENTER_ACTION) != 0) {
            outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;//移除EditorInfo.IME_FLAG_NO_ENTER_ACTION标志位
        }
        if ((outAttrs.imeOptions & EditorInfo.IME_ACTION_SEND) != 0) {
            outAttrs.imeOptions &= ~EditorInfo.IME_ACTION_GO;//移除EditorInfo.IME_FLAG_NO_ENTER_ACTION标志位
        }
        return connection;
    }
}
