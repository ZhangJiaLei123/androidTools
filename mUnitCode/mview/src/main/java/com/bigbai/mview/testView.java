package com.bigbai.mview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;


@SuppressLint("AppCompatCustomView")
public class testView extends TextView {


    public testView(Context context) {
        super(context);
    }

    public testView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public testView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    int mLastx = 0;

    int mLasty = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (mLasty != -1) {
                    RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams)this.getLayoutParams();
                    linearParams.leftMargin +=(x-mLastx);
                    linearParams.topMargin +=(y-mLasty);
                    this.setLayoutParams(linearParams);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        mLastx = x;
        mLasty = y;
        return true;
    }

    public int getmX(){
        return mLastx;
    }

    public int getmY(){
        return mLasty;
    }

    public void moveAddX(int x){
     //   if (mLastx != -1)
       // {
           // Log.i("移动x");
            RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams)this.getLayoutParams();
            linearParams.leftMargin +=(x);
            this.setLayoutParams(linearParams);
            mLastx = linearParams.leftMargin;
       // }

    }

    public void moveAddY(int y){
     //   if (mLasty != -1)
       // {
            RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams)this.getLayoutParams();
            linearParams.topMargin +=(y);
            this.setLayoutParams(linearParams);
            mLasty = linearParams.topMargin;
       // }
    }

}
