package com.bigbai.mview.medittext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.util.AttributeSet;
import android.widget.EditText;

import org.w3c.dom.Text;

import static android.content.Context.MODE_PRIVATE;


/**
 * 重写EditText,自动记内容，下次自动填充
 * @author: Zhang
 * @date: 2019/7/1 - 15:13
 * @note Created by com.bigbai.mview.mTextView.
 */
@SuppressLint("AppCompatCustomView")
public class RememberTextView extends EditText {

    SharedPreferences SP = null;

    public RememberTextView(Context context) {
        super(context);
        initData(context);
    }

    public RememberTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    public RememberTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RememberTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initData(context);
    }


    /**
     * 对SharedPreferences进行初始化,
     * 并填充初始值
     * @param context
     */
    private void initData(Context context){
        if(SP != null){
            return;
        }
        SP = context.getSharedPreferences(context.getPackageName() + "_preferences", MODE_PRIVATE);
        String text = super.getText().toString();
        int id = getId();
        String textValue = SP.getString("_RememberTextView_" + id , text);
        super.setText(textValue);

    }


    /**
     * 重写setText，更新SharedPreferences值
     * @param text
     */
    public void setText(String text){
        super.setText(text);
        if(SP != null) {
            SP.edit().putString("_RememberTextView_" + getId(), text).commit();
        }
    }


    /**
     * 重写getText，更新SharedPreferences值
     * @return
     */
    @Override
    public Editable getText(){

        Editable editable =  super.getText();
        if(SP != null)
        {
            SP.edit().putString("_RememberTextView_" + getId(), editable.toString()).commit();
        }

        return super.getText();
    }

}
