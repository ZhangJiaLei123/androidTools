package com.bigbai.mview.btn;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.view.MotionEvent;

/**
 * Created by ccwxf on 2016/2/29.
 */
public class FinishStatus implements ButtonStatus {

    private static final int Status_Stretch = 0;
    private static final int Status_Finish = 1;
    private static final int Stroke_Over = 10;
    private String Text_Over = "完成";
    private static final int Text_Over_Size = 40;
    private static final int Delay_Stretch = 10;
    private static final int Pixel_Stretch = 8;

    private CommitButton button;
    private int width;
    private int height;
    private int centerX;
    private int centerY;
    private int status = Status_Stretch;
    private Handler handler = new Handler();

    public FinishStatus(CommitButton button, int width, int height, int centerX, int centerY) {
        this.button = button;
        this.width = width;
        this.height = height;
        this.centerX = centerX;
        this.centerY = centerY;
        Text_Over = button.textEnd;
        startStretch();
    }

    public void setTextOver(String Text_Over){
        this.Text_Over = Text_Over;
    }

    /**
     * 开始伸展背景
     */
    private void startStretch() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(width < button.getMaxWidth()){
                    width += Pixel_Stretch;
                    button.invalidate();
                    handler.postDelayed(this, Delay_Stretch);
                }else{
                    width = button.getMaxWidth();
                    if(status == Status_Stretch){
                        status = Status_Finish;
                    }
                    button.invalidate();
                }
            }
        }, Delay_Stretch);
    }

    @Override
    public Status getStatus() {
        return Status.Finish;
    }

    @Override
    public boolean onTouchEvent(MotionEvent mEvent) {
        return false;
    }

    @Override
    public void onDraw(Canvas mCanvas, Paint mPaint) {
        //绘制背景
        mPaint.setColor(button.getColorBase());
        mPaint.setStyle(Paint.Style.FILL);
        mCanvas.drawRoundRect(new RectF(centerX - width / 2, centerY - height / 2, centerX + width / 2, centerY + height / 2 ),
                button.getRound(), button.getRound(), mPaint);
        //绘制图片
        if(status == Status_Finish){
            mPaint.setColor(Color.WHITE);
            mPaint.setStrokeWidth(Stroke_Over);
            mPaint.setTextSize(Text_Over_Size);
            Paint.FontMetrics metrics = mPaint.getFontMetrics();
            int textWidth = (int) mPaint.measureText(Text_Over);
            int baseLine = (int) (height / 2 + (metrics.bottom - metrics.top) / 2 - metrics.bottom);
            mCanvas.drawText(Text_Over, (width - textWidth) / 2, baseLine, mPaint);
        }
    }
}
