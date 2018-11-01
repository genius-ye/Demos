package com.genius.views.scrollnumberview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.genius.R;
import com.genius.utils.DisplayUtils;


/**
 * 上下滚动的数字
 * Created by geniusye
 */
public class AutoScrollNumView extends View {
    private static final int DEFAULT_COLOR = Color.BLACK;
    private static final int DEFAULT_TEXT_SIZE = 16;
    private int mColor = DEFAULT_COLOR;
    private int mTextSize = DEFAULT_TEXT_SIZE;
    private int mOffset = 0;
    private int mWid = 0;
    private int mHei = 0;
    private Paint mPaint;
    private int mRequiredWid = 0;
    private int mRequiredHei = 0;
    private float mBaseLine;
    private int mCurrentNum = 0;
    private boolean isPlaying = false;
    /**
     * 动画
     */
    private ValueAnimator anim;
    /**
     * 动画播放时间
     */
    private long duration = 1000;

    /**
     * 数字外框
     */
    private RectF mRectF;
    private Paint rectPaint;
    /**
     * 外框圆角大小
     */
    private int rectRadius = 14;
    /**
     * 外框边线的宽度
     */
    private int rectStrokeWidth = 3;

    public AutoScrollNumView(Context context) {
        super(context);

        init(context, null);
    }

    public AutoScrollNumView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AutoScrollNumView);
            if (ta != null) {
                mColor = ta.getColor(R.styleable.AutoScrollNumView_autoscrollnumviewTextColor, Color.BLACK);
                mTextSize = ta.getDimensionPixelSize(R.styleable.AutoScrollNumView_autoscrollnumviewTextSize, DEFAULT_TEXT_SIZE);

                ta.recycle();
            }
        }

        setTextSize(mTextSize);
        setTextColor(mColor);
//        initRectf();
    }

    /**
     * 初始化外框
     */
    private void initRectf()
    {
        mRectF = new RectF(0,0,mWid,mHei);
        rectPaint = new Paint();
        rectPaint.setColor(Color.parseColor("#eeeeee"));
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setAntiAlias(true);// 设置画笔的锯齿效果
        rectPaint.setStrokeWidth(rectStrokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < 10; i++) {
            canvas.drawText(String.valueOf(i), (mWid - mRequiredWid) / 2,
                    mHei * i + (mHei-mBaseLine*2/3) - mOffset, mPaint);
        }

        canvas.drawRoundRect(mRectF,rectRadius,rectRadius,rectPaint);
    }

    private void calRequireSize() {
        Paint.FontMetrics fm = mPaint.getFontMetrics();

        mRequiredWid = (int) mPaint.measureText("0");
        mRequiredHei = (int) (fm.bottom - fm.top);

        //  canvas.drawText()其中一个参数为baseLine,如果设置为试图的高度，则会截掉部分
        //  如小写字母p中的下吧吗部分，就属于baseLine以下的部分
        //  故需要计算baseLine的高度，公式参考如下
        //  http://www.sjsjw.com/kf_mobile/article/9_31376_30207.asp
        mBaseLine = (fm.bottom - fm.top) / 2 - fm.descent;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWid = w;
        mHei = h;
        initRectf();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false));
    }

    private int measure(int measureSpec, boolean isWidSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        int requireSize = isWidSpec ? mRequiredWid : mRequiredHei;

        if (!(mode == MeasureSpec.EXACTLY && size > requireSize))
            size = requireSize;

        return size;
    }

    public void startAnim(int startNum,int targetNum) {
        if (targetNum < 0) {
            targetNum = 0;
        } else if (targetNum > 9) {
            targetNum = 9;
        }
        mCurrentNum = targetNum;

        if(anim!=null)
        {
            anim.cancel();
        }

        anim = ValueAnimator.ofInt(startNum*mHei, targetNum * mHei);
        anim.setDuration(duration);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mOffset = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isPlaying = false;
            }
        });
        anim.start();
        isPlaying = true;
    }

    public void setTextColor(int color) {
        mColor = color;
        mPaint.setColor(mColor);

        invalidate();
    }

    public void setTextSize(int size) {
        if (size < 0) {
            return;
        }
        mTextSize = size;
        int pxSize = DisplayUtils.dip2px(getContext(), mTextSize);
        mPaint.setTextSize(pxSize);

        calRequireSize();
        requestLayout();
        mOffset = mCurrentNum * mHei;
        invalidate();
    }

    /**
     *
     * @param duration 动画播放时间（单位：毫秒）
     */
    public void setDuration(final long duration) {
        this.duration = duration;
    }

    public void setRectRadius(final int rectRadius) {
        this.rectRadius = rectRadius;
    }

    public void setRectStrokeWidth(final int rectStrokeWidth) {
        this.rectStrokeWidth = rectStrokeWidth;
    }
}