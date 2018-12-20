package com.zhc.diyviews.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.zhc.diyviews.R;

/**
 * 自定义矩形
 * 主要学习基本的view自定义流程
 */
public class RectView extends View {

    private Paint mPaint;
    //at_most时 设置的默认宽高
    private int mDefaultWidth = 400;
    private int mDefaultHeight = 400;


    public RectView(Context context) {
        this(context, null);
    }

    public RectView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {

        //获取自定义的属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RectView);
        int color = typedArray.getColor(R.styleable.RectView_rect_back_color, Color.RED);
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(color);
        mPaint.setStrokeWidth(10f);

    }


    /**
     * 自定义view 直接继承view时 如果不手动设置wrap_content时大小
     * ，那么view的大小将和math_parent一样大
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获取宽高的测量模式
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        //根据宽高的测量模式 获取测量的宽和高
        int width = MeasureSpec.getSize(widthSpecMode);
        int height = MeasureSpec.getSize(heightSpecMode);

        //根据测量模式进行大小处理 这里主要处理的时at_most测量模式
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mDefaultWidth, mDefaultHeight);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mDefaultWidth, height);

        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(width, mDefaultHeight);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //计算padding 使其在布局中生效
        int mLeft = getPaddingLeft();
        int mRight = getPaddingRight();
        int mTop = getPaddingTop();
        int mBottom = getPaddingBottom();

        //获取控件的可以显示的宽和高
        int width = getWidth() - mLeft - mRight;
        int height = getHeight() - mTop - mBottom;

        //设置区域大小
        Rect rect = new Rect(0 + mLeft, 0 + mTop, width + mRight, height + mBottom);
        //画矩形
        canvas.drawRect(rect, mPaint);

    }

    /**
     * 自定义View一般不需要重写这个函数
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
