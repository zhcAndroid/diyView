package com.zhc.diyviews.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class TaijiView extends View {

    private Paint mWhitePaint = new Paint();
    private Paint mBlackPaint = new Paint();
    private float degree= 0f;

    int width ,height;

    public TaijiView(Context context) {
        super(context);
        // 初始化画笔
        initPaint();
    }

    public TaijiView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 初始化画笔
        initPaint();
    }

    public TaijiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 初始化画笔
        initPaint();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {

        mBlackPaint.setStrokeWidth(0f);
        mBlackPaint.setColor(Color.BLACK);
        mBlackPaint.setAntiAlias(true);
        mBlackPaint.setStyle(Paint.Style.FILL);

        mWhitePaint.setStrokeWidth(0f);
        mWhitePaint.setColor(Color.WHITE);
        mWhitePaint.setAntiAlias(true);
        mWhitePaint.setStyle(Paint.Style.FILL);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //开始画太极
        float r = (float) (Math.min(width, height) / 2 * 0.8);
        //移动到画布的中心点
        canvas.translate(width /2,height /2);
        //对画布进行旋转 达到太极转动的效果
        canvas.rotate(degree);

        // 画太极左右黑白
        canvas.drawArc(new RectF(-r, -r, r, r), -90f, 180f, true, mWhitePaint);
        canvas.drawArc(new RectF(-r, -r, r, r), 90f, 180f, true, mBlackPaint);

        // 画太极上下小半圆
        canvas.drawCircle(0f, -r / 2, r / 2, mBlackPaint);
        canvas.drawCircle(0f, r / 2, r / 2, mWhitePaint);

        // 画太极上下的眼
        canvas.drawCircle(0f, -r / 2, r / 10, mWhitePaint);
        canvas.drawCircle(0f, r / 2, r / 10, mBlackPaint);

        degree+=5.0f;
        invalidate();

    }

}

