package cn.lemon.whiteboard.widget;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import cn.lemon.whiteboard.R;

/**
 * Created by user on 2016/10/25.
 */

public class InputDialog extends AppCompatDialog {

    private Context mContext;

    private TextView mTitle;
    private EditText mInputContent;
    private TextView mPassiveText;
    private TextView mPositiveText;

    public InputDialog(Context context) {
        this(context, cn.lemon.common.R.style.MaterialDialogStyle);
    }

    public InputDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        initView();
    }

    protected InputDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        setCancelable(cancelable);
        setOnCancelListener(cancelListener);
    }

    public void initView(){
        View dialogContent = LayoutInflater.from(mContext).inflate(R.layout.widget_dialog_input, null);

        mTitle = (TextView) dialogContent.findViewById(R.id.title);
        mInputContent = (EditText) dialogContent.findViewById(R.id.input_content);
        mPassiveText = (TextView) dialogContent.findViewById(R.id.passive_button);
        mPositiveText = (TextView) dialogContent.findViewById(R.id.passive_button);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogContent,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    /**
     * 确认按钮监听器
     */
    public void setPositiveClickListener(final OnClickListener listener) {
        mPositiveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(InputDialog.this, 0);
                }
            }
        });
    }

    /**
     * 取消安装监听器
     */
    public void setPassiveClickListener(final OnClickListener listener) {
        mPassiveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onClick(InputDialog.this, 1);
                }
            }
        });
    }

    public String getInputContent(){
        return mInputContent.getText().toString();
    }

    public String getTitle(){
        return mTitle.getText().toString();
    }

}
