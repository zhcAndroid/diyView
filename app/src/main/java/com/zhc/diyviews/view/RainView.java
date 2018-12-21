package com.zhc.diyviews.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class RainView extends View{

    private String[] mTextArray = new String[]{"a","b","c","d","e",
            "f","g","h","i","j","k","l","m","n","o","p","q","r","s","t",
            "u","v","w","s","x","y","z"};

    private Paint mPaint;

    private float delateY = 10;
    float currY = 50;

    public RainView(Context context) {
        super(context);
        init();
    }

    public RainView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RainView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLUE);
        mPaint.setTextSize(48);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        currY = 50;
        for(int i= 0;i<100;i++){
            for(int j = 0;j < 30;j ++){
                int num = (int) (Math.random()*(mTextArray.length - 1));
                canvas.drawText(mTextArray[num],100*j,currY,mPaint);
            }

        }
        currY = 50+currY;
        Log.i("onDraw ",currY+"");
        invalidate();
    }
}
