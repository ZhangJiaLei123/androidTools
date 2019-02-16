package com.bigbai.mview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.Gravity;

/**
 * 带行号的 EditText
 */
public class MyEditText extends AppCompatEditText {
    private Paint line;
    private Paint line_row;

    @SuppressLint("ResourceAsColor")
    public MyEditText(Context context, AttributeSet As){
        super( context, As);
        setFocusable(true);
        line=new Paint();
        line.setColor(Color.GRAY);
        line.setStrokeWidth(2);

        line_row=new Paint();
        line_row.setColor(Color.GRAY);
        line_row.setStrokeWidth(4);

        setPadding(95,0,0,0);
        setGravity(Gravity.TOP);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onDraw(final Canvas canvas)
    {
        if(getText().toString().length()!=0){
            float y=0;
            Paint p=new Paint();
            p.setColor(R.color.colorWhite);
            p.setTextSize(40);
            for(int l=0;l<getLineCount();l++){
                y=((l+1)*getLineHeight())-(getLineHeight()/4);
                canvas.drawText(String.valueOf(l+1),0,y,p);
                canvas.save();
            }
        }
        int k=getLineHeight();
        int i=getLineCount();
        //竖线
        canvas.drawLine(90,0,90,getHeight()+(i*k),line);
        int y=(getLayout().getLineForOffset(getSelectionStart())+1)*k;
        //行标
        canvas.drawLine(0,y-1,90,y-1,line_row);
        canvas.save();
        canvas.restore();
        super.onDraw(canvas);
    }
}
