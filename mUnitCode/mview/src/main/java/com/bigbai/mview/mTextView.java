package com.bigbai.mview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 带下标的 TextView ,可用于listview内的组件监听
 */
@SuppressLint("AppCompatCustomView")
public class mTextView extends TextView {

    private int index = -1;

    public mTextView(Context context) {
        super(context);
    }

    public mTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public mTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public mTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
