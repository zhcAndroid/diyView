package com.zhc.diyviews.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 自定义横向滑动ViewGroup
 *思路： 就是把子viwe横向排列即可
 *      处理掉滑动时的事件冲突
 *      添加弹性 滑动超过屏幕的一半时自动滑到下一个页面
 *      其他优化。。。
 *
 */
public class HorizontalScrollView extends ViewGroup {

    //记录最后触摸时x y轴坐标
    private int mLastX;
    private int mLastY;

    /**
     * 使用scroller进行view进行滑动处理
     */
    private Scroller mScroller;

    //子view的宽度
    private int childViewWidth;
    //当前页面角标
    private int currIndex;


    public HorizontalScrollView(Context context) {
        this(context,null);
    }

    public HorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    //初始化操作。。获取自定义属性什么的在这里
    private void init(Context context, AttributeSet attrs) {

        mScroller = new Scroller(context);

    }


    /**
     * 计算所有ChildView的宽度和高度 然后根据ChildView的计算结果，设置自己的宽和高
     *
     * 自定义ViewGroup时一般不需要重写onMeaure如果你是继承特定的ViewGroup
     * 这里直接继承原生的viewGroup 最好处理一下当大小为wrap_content时 控件的大小
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获取宽和高的测量模式和大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMode);
        int heightSize = MeasureSpec.getSize(heightMode);

        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();
        if(childCount == 0){
            //没有子View
            setMeasuredDimension(0,0);
        }else if(widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST){
            //获取第一个子View
            View childAt = getChildAt(0);
            int measuredWidth = childAt.getMeasuredWidth();
            int measuredHeight = childAt.getMeasuredHeight();

            //设置宽=第一个子View的宽*子view的个数 高度和单个的高度保持一致
            setMeasuredDimension(measuredWidth * childCount,measuredHeight);


        }else if(widthMode == MeasureSpec.AT_MOST){
            //获取第一个子View
            View childAt = getChildAt(0);
            int measuredWidth = childAt.getMeasuredWidth();

            setMeasuredDimension(measuredWidth * childCount,heightSize);
        }else if(heightMode == MeasureSpec.AT_MOST){
            //获取第一个子View
            View childAt = getChildAt(0);
            int measuredHeight = childAt.getMeasuredHeight();
            setMeasuredDimension(widthSize,measuredHeight);

        }




    }

    /**
     * 这个是必须重写的
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childCount = getChildCount();

        Log.i("onLayout==childCount",childCount+"");

        //记录最后view的起点位置 x轴方向
        int left = 0;
        View childView = null;
        for(int i=0;i<childCount;i++){
            childView = getChildAt(i);
            if(childView.getVisibility() != View.GONE){
                int measuredWidth = childView.getMeasuredWidth();
                childViewWidth = measuredWidth;
                int measuredHeight = childView.getMeasuredHeight();
                childView.layout(left,0,left+measuredWidth,measuredHeight);
                left+=measuredWidth;
                Log.i("left",measuredWidth+"");
            }
        }

    }


    /**
     * 是否对事件进行拦截
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercepted = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isIntercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x -mLastX;
                int deltaY = y - mLastY;
                //当判断是横向滑动时 我们自己消费掉这个事件 不在向子view传递
                if(Math.abs(deltaX) - Math.abs(deltaY) > 0){
                    isIntercepted = true;
                }else{
                    isIntercepted = false;
                }
                Log.i("isIntercepted==",isIntercepted+"");
                break;
            case MotionEvent.ACTION_UP:
                isIntercepted = false;
                break;
        }
        mLastX = x;
        mLastY = y;

        return isIntercepted;
    }


    /**
     * 对滑动事件进行处理
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                int delateX = x - mLastX;

                //滑到最后一个元素 继续向右滑动时 禁止
                if(delateX < 0 && currIndex==getChildCount()-1){
                    return false;
                }
                //滑到第一个元素 继续向左边滑动时 禁止
                if(delateX > 0 && currIndex==0){
                    return false;
                }
                //根据手指移动的距离 移动view
                scrollBy(-delateX,0);



                break;
            case MotionEvent.ACTION_UP:

                //当抬起手指时 判断移动的距离 如果大于宽度的一半 则认为用户需要到下一个页面

                int distance = getScrollX() - childViewWidth*currIndex;

                if(Math.abs(distance) > childViewWidth / 2){

                    if(distance > 0){

                        currIndex++;
                    }else{
                        currIndex--;
                    }

                }
                smoothScrollTo(currIndex * childViewWidth,0 );
                break;


        }

        mLastX = x;
        mLastY = y;

        //这里注意 返回true
        return true;
    }


    /**
     * 根据滑动的距离差 弹性滑动到指定位置
     * @param dX
     * @param dy
     */
    public void smoothScrollTo(int dX,int dy){

        mScroller.startScroll(getScrollX(),getScrollY(),dX - getScrollX(),dy - getScrollY(),500);
        invalidate();

    }


    @Override
    public void computeScroll() {
        super.computeScroll();

        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }

    }
}
