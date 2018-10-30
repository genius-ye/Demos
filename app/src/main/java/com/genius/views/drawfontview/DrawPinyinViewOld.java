package com.genius.views.drawfontview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by wangly on 2018/7/23.
 */

public class DrawPinyinViewOld extends View {

    /*
       指令 参数           描述
       M   x y         起始点坐标x y （Move to）
       L   x y         从当前点的坐标画直线到指定点的 x y坐标 （Line to）
       H   x           从当前点的坐标画水平直线到指定的x轴坐标 （Horizontal line to）
       V   y           从当前点的座标画垂直直线到指定的y轴坐标 （Vertical line to）
       C   x1 y1 x2 y2 x y 从当前点的坐标画条贝塞风线到指定点的x, y坐标，其中 x1 y1及x2, y2为控制点 （Curve）
       S   x2 y2 x y       从当前点的坐标画条反射的贝塞曲线到指定点的x, y坐标，其中x2, y2为反射的控制点（Smooth curve）
       Q   x1 y1 x y       从当前点的坐标画条反射二次贝塞曲线到指定点的x, y坐标，其中x1 y1为控制点（Quadratic Bézier curve）
       T   x y             从当前点的坐标画条反射二次贝塞曲线到指定点的x, y坐标，以前一个坐标为反射控制点（Smooth Quadratic Bézier curve）
       A   rx ry x-axis-rotation large-arc-flag sweep-flag x y     从当前点的坐标画个椭圆形到指定点的x, y坐标，
       其中rx, ry为椭圆形的x轴及y轴的半径，x-axis-rotation是弧线与x轴的旋转角度，
       large-arc-flag则设定1最大角度的弧线或是0最小角度的弧线，sweep-flag设定方向为1顺时针方向或0逆时针方向（Arc）
       Z       关闭路径，将当前点坐标与第一个点的坐标连接起来（Closepath）著作权归作者所有。

        注释：以上所有命令均允许小写字母。大写表示绝对定位，小写表示相对定位。
    */
//    String pinyin_a = "[\"M195.24,124.18q-14.75-17.67-36.33-17.67a49.08,49.08,0,0,0-19.48,4.25,41.41,41.41,0,0,0-16.85,13.42q-7.12,9.09-9,17.92a84,84,0,0,0-1.9,17.73q0,14.74,4.73,25.24t11.67,16.56q12.6,11.13,30.86,11.32,14.46,0,22.36-5.12A63.09,63.09,0,0,0,196.12,194Z" +
//            "m-12.16,63q-8.16,10.76-22.9,10.55-14.94,0-23.34-11.08t-8.4-26.81q0-15.63,8.79-27a27.68,27.68,0,0,1,23-11.33q14.26,0,22.66,10.79t8.4,27.49Q191.24,176.44,183.08,187.17Z\",\"" +
//            "M195.24,124.18q-.06-6.93-.1-13.86h13.58q0,17.87,0,35.93t.05,35.84q0,9.58,4.3,13.33" +
//            "a19.32,19.32,0,0,0,10.06,4.64,64.81,64.81,0,0,1,1.22,6.4c.29,2.12.67,4.28,1.12,6.49A25.44,25.44,0,0,1,196.12,194\n" +
//            "Q195.52,159.26,195.24,124.18Z\"]";
//    String pinyin_a_iii = "[\"197.92 142 187.92 126 177.92 118 161.92 113 149.92 115 140.92 119 130.92 128 122.92 139 119.92 150 119.92 161 120.92 172 123.92 182 128.92 192 136.92 199 144.92 203 154.92 205 166.92 205 173.92 203 181.92 198 188.92 191 197.92 178 198.5 161.5 197.92 142\",\"" +
//            "201.92 109 201.92 174 201.92 185 203.92 193 206.92 199 213.92 205 226.92 207\"]";

    /**
     * 拼音数据
     */
    private String pinyinData;
    /**
     * 处理后的拼音字符串
     */
    private String dataString;
    /**
     * 拼音笔画
     */
    private String pinyinStroke;

    private Path mPath;
    private Context mContext;
    private Paint mPaint;
    /**
     * 提示画笔
     */
    private Paint noticePaint;
    /**
     * 每个笔画需要经过的点的集合
     */
    private ArrayList<ArrayList<FloatPoint>> mPointDatas = new ArrayList<>();
    /**
     * 每个笔画需要经过的点的路径
     */
    private ArrayList<Path> mPointPath = new ArrayList<>();
    /**
     * 每个笔画的长度
     */
    private ArrayList<Float> mLengths = new ArrayList<>();
    /**
     * 每个笔画的路径
     */
    private ArrayList<Path> mPaths = new ArrayList<>();
    /**
     * 下一笔画提示的path
     */
    private Path noticePath;
    /**
     * 是否显示提示笔画
     */
    private boolean isShowNotice = true;
    /**
     * 当前的笔画
     */
    private int currentIndex;
    /**
     * 手绘的路径
     */
    private Path drawPath;
    /**
     * 是否是合法操作
     */
    private boolean isLegalOp;
    /**
     * 手指触摸的区域
     */
    private RectF touchRect;
    /**
     * 要匹配的区域
     */
    private RectF matchRect;
    /**
     * 误差范围
     */
    private int deviations = 80;
    /**
     * 默认画笔的大小
     */
    private float LINEWIDTH = 5;
    /**
     * 画布大小
     */
    private int pixel = 300;
    /**
     * 画布缩放的倍数
     */
    private float scaleTimes_x = 1.5f;
    private float scaleTimes_y = 1.5f;
    /**
     * 手绘画笔
     */
    private Paint drawPaint;
    /**
     * 画轮廓的画笔
     */
    private Paint drawStrokePaint;
    /**
     * 是否在自动播放
     */
    private boolean isAutoPlay;
    /**
     * x轴的偏移量
     */
    private float deviation_x = 0;
    /**
     * y轴的偏移量
     */
    private float deviation_y = 0;
    /**
     * 画布的实际宽
     */
    private float canvas_w;
    /**
     * 画布的实际高
     */
    private float canvas_h;
    /**
     * 是否可以描红
     */
    private boolean isCanTouch;


    public DrawPinyinViewOld(Context context) {
        this(context, null);
    }

    public DrawPinyinViewOld(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawPinyinViewOld(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        drawPaint = new Paint();
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setColor(Color.BLUE);
        // 抗锯齿
        drawPaint.setAntiAlias(true);
        // 防抖动
        drawPaint.setDither(true);
        drawPaint.setStrokeWidth(100);

        mContext = context;
        mPath = new Path();
        mPaint = new Paint();
        // 抗锯齿
        mPaint.setAntiAlias(true);
        // 防抖动
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);

        noticePaint = new Paint();
        noticePaint.setStyle(Paint.Style.STROKE);
        noticePaint.setColor(Color.RED);
        // 抗锯齿
        noticePaint.setAntiAlias(true);
        // 防抖动
        noticePaint.setDither(true);
        noticePaint.setStrokeWidth(LINEWIDTH);

        drawStrokePaint = new Paint();
        // 抗锯齿
        drawStrokePaint.setAntiAlias(true);
        // 防抖动
        drawStrokePaint.setDither(true);
        drawStrokePaint.setStyle(Paint.Style.FILL);
        drawStrokePaint.setStrokeWidth(LINEWIDTH);

        noticePath = new Path();
        drawPath = new Path();
        initRect();

//        initData(pinyinData,pinyinStroke,false);
    }

    /**
     * 初始化拼音数据
     * @param pinyinData    :拼音原数据
     * @param pinyinStroke  :拼音笔画数据
     * @param isFill        :是否是填充
     */
    public void initData(String pinyinData,String pinyinStroke,boolean isFill)
    {
        setPinyinData(pinyinData);
        setPinyinStroke(pinyinStroke);
        if(isAutoPlay)
        {
            isAutoPlay = false;
        }
        mPointDatas.clear();
        mPointPath.clear();
        mLengths.clear();
        initDataString();
        initPinyin();
        initPinyin();
        solvePassPoint();
        if(isFill)
        {
            currentIndex = mPaths.size();
        }
        else
        {
            currentIndex = 0;
        }
        getNoticePath();
        invalidate();
    }

    /**
     * 初始化各个区域
     */
    private void initRect() {
        touchRect = new RectF();
        matchRect = new RectF();
    }

    /**
     * 清除描红
     */
    public void clear() {
        if (!isAutoPlay) {
            drawPath.reset();
            currentIndex = 0;
            postInvalidate();
            getNoticePath();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

//        canvas.scale((float) getMeasuredWidth() / pixel, (float) getMeasuredHeight() / pixel);
        canvas.drawColor(Color.YELLOW);
        drawPinyinStroke(canvas);
        if (currentIndex > 0) {
            drawStroke(canvas);
        }
        drawNoticePath(canvas);
        if (currentIndex < mPointDatas.size()) {
            //新建一个画布(画布是透明的)
            int layid = canvas.saveLayer(0, 0, getMeasuredWidth(), getMeasuredHeight(), null, Canvas.ALL_SAVE_FLAG);
            //设置画布背景颜色（红色）（如果不设置颜色，该图层为空，上层相交则也为空）
//          canvas.drawColor(Color.RED);
            //设置相交模式（取两个图层相交的部分，以目标图层颜色显示，也就是下层背景色（红色））
            drawPath(canvas);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            Bitmap bitmap = characterBitmap();
            canvas.drawBitmap(bitmap, 0, 0, mPaint);
            canvas.restoreToCount(layid);
        }

        //测试
        if (touchRect != null) {
            canvas.drawRect(touchRect, mPaint);
            mPaint.setColor(Color.RED);
            canvas.drawRect(matchRect, mPaint);
//            canvas.drawRect(rectF,mPaint);
        }

    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measuredWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
//        setMeasuredDimension(pixel/2,pixel/2);
    }

    private int measureHeight(final int heightMeasureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (specMode) {
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min((int) canvas_h, specSize);
                break;
        }

        return result;
    }

    private int measuredWidth(final int widthMeasureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (specMode) {
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min((int)canvas_w, specSize);
                break;
        }

        return result;
    }

    /**
     * 处理源数据的字符串
     */
    private void initDataString()
    {
        if(TextUtils.isEmpty(pinyinData))
        {
            return;
        }
        StringBuilder sb = new StringBuilder();
        char chars[] = pinyinData.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            //获取ascii码
            int ascii = (int) chars[i];
            //A-Z(65~90) a-z(97~122)
            if ((ascii > 64 && ascii < 91) || (ascii > 96 && ascii < 123)) {
                sb.append(" " + chars[i] + " ");
            }
            //-（45）
            else if (ascii == (int) '-') {
                //0-9（48~57）
                if ((int) chars[i - 1] > 47 && (int) chars[i - 1] < 58) {
                    sb.append("," + chars[i]);
                } else {
                    sb.append(chars[i]);
                }
            } else if (ascii == (int) '.') {
                //0-9（48~57）
                if ((int) chars[i - 1] < 48 || (int) chars[i - 1] > 57) {
                    sb.append("0" + chars[i]);
                } else {
                    //处理55.71.75这种情况
                    int index = i - 1;
                    while (true) {
                        if ((int) chars[index] > 47 && (int) chars[index] < 58) {
                            index--;
                            continue;
                        } else if (chars[index] == '.') {
                            sb.append(",0" + chars[i]);
                        } else {
                            sb.append(chars[i]);
                        }
                        break;
                    }

                }
            } else {
                sb.append(chars[i]);
            }
        }
        pinyinData = sb.toString().trim();
    }

    /**
     * 初始化拼音数据
     */
    private void initPinyin() {

        if(TextUtils.isEmpty(pinyinData))
        {
            return;
        }
        Log.w("genius", pinyinData);
        //上一次操作的控制点
        FloatPoint cPoint = new FloatPoint();
        //上一次操作的结束点
        FloatPoint lPoint = new FloatPoint();
        //第一次操作的点
        FloatPoint startPoint = new FloatPoint();
        //上一次操作的命令
        String command = "";
        mPaths.clear();
        mPaths.add(new Path());
        String str[] = pinyinData.split(" ");
        mPath.reset();
        for (int i = 0; i < str.length; i++) {
            Log.w("genius", str[i]);
            if (str[i].equals("M")) {
                String p[] = str[i + 1].split(",");
                for (int j = 0; j < p.length; j++) {
                    lPoint.set(Float.valueOf(p[j].trim()) - deviation_x, Float.valueOf(p[j + 1].trim()) - deviation_y);
                    startPoint.set(lPoint.x, lPoint.y);
                    mPath.moveTo(lPoint.x, lPoint.y);
                    mPaths.get(mPaths.size() - 1).moveTo(lPoint.x, lPoint.y);
                    j++;
                    command = "M";
                }
            } else if (str[i].equals("m")) {
                String p[] = str[i + 1].split(",");
                for (int j = 0; j < p.length; j++) {
                    lPoint.set(Float.valueOf(p[j].trim()) + lPoint.x, Float.valueOf(p[j + 1].trim()) + lPoint.y);
                    mPath.moveTo(lPoint.x, lPoint.y);
                    mPaths.get(mPaths.size() - 1).moveTo(lPoint.x, lPoint.y);
                    j++;
                    command = "m";
                }
            } else if (str[i].equals("l")) {
                String p[] = str[i + 1].split(",");
                lPoint.set(Float.valueOf(p[0].trim()) + lPoint.x, Float.valueOf(p[1].trim()) + lPoint.y);
                mPath.lineTo(lPoint.x, lPoint.y);
                mPaths.get(mPaths.size() - 1).lineTo(lPoint.x, lPoint.y);
                command = "l";
            } else if (str[i].equals("L")) {
                String p[] = str[i + 1].split(",");
                lPoint.set(Float.valueOf(p[0].trim()) - deviation_x, Float.valueOf(p[1].trim()) - deviation_y);
                mPath.lineTo(lPoint.x, lPoint.y);
                mPaths.get(mPaths.size() - 1).lineTo(lPoint.x, lPoint.y);
                command = "L";
            } else if (str[i].equals("q")) {
                String p[] = str[i + 1].split(",");
                for (int j = 0; j < p.length; j++) {
                    cPoint.set(Float.valueOf(p[j].trim()) + lPoint.x, Float.valueOf(p[j + 1].trim()) + lPoint.y);
                    lPoint.set(Float.valueOf(p[j + 2].trim()) + lPoint.x, Float.valueOf(p[j + 3].trim()) + lPoint.y);
                    mPath.quadTo(cPoint.x, cPoint.y, lPoint.x, lPoint.y);
                    mPaths.get(mPaths.size() - 1).quadTo(cPoint.x, cPoint.y, lPoint.x, lPoint.y);
                    j = j + 3;
                    command = "q";
                }
            } else if (str[i].equals("Q")) {
                String p[] = str[i + 1].split(",");
                for (int j = 0; j < p.length; j++) {
                    cPoint.set(Float.valueOf(p[j].trim()) - deviation_x, Float.valueOf(p[j + 1].trim()) - deviation_y);
                    lPoint.set(Float.valueOf(p[j + 2].trim()) - deviation_x, Float.valueOf(p[j + 3].trim()) - deviation_y);
                    mPath.quadTo(cPoint.x, cPoint.y, lPoint.x, lPoint.y);
                    mPaths.get(mPaths.size() - 1).quadTo(cPoint.x, cPoint.y, lPoint.x, lPoint.y);
                    j = j + 3;
                    command = "Q";
                }
            } else if (str[i].equals("T")) {
                String p[] = str[i + 1].split(",");
                cPoint = getSymmetryPoint(cPoint, lPoint);
                lPoint.set(Float.valueOf(p[0].trim()) - deviation_x, Float.valueOf(p[1].trim()) - deviation_y);
                mPath.quadTo(cPoint.x, cPoint.y, lPoint.x, lPoint.y);
                mPaths.get(mPaths.size() - 1).quadTo(cPoint.x, cPoint.y, lPoint.x, lPoint.y);
                command = "T";
            } else if (str[i].equals("t")) {
                String p[] = str[i + 1].split(",");
                cPoint = getSymmetryPoint(cPoint, lPoint);
                lPoint.set(Float.valueOf(p[0].trim()) + lPoint.x, Float.valueOf(p[1].trim()) + lPoint.y);
                mPath.quadTo(cPoint.x, cPoint.y, lPoint.x, lPoint.y);
                mPaths.get(mPaths.size() - 1).quadTo(cPoint.x, cPoint.y, lPoint.x, lPoint.y);
                command = "t";
            } else if (str[i].equals("h")) {
                String p[] = str[i + 1].split(",");
                for (int j = 0; j < p.length; j++) {
                    lPoint.set(Float.valueOf(p[j].trim()) + lPoint.x, lPoint.y);
                    mPath.lineTo(lPoint.x, lPoint.y);
                    mPaths.get(mPaths.size() - 1).lineTo(lPoint.x, lPoint.y);
                    command = "h";
                }

            } else if (str[i].equals("H")) {
                String p[] = str[i + 1].split(",");
                for (int j = 0; j < p.length; j++) {
                    lPoint.set(Float.valueOf(p[j].trim()) - deviation_x, lPoint.y);
                    mPath.lineTo(lPoint.x, lPoint.y);
                    mPaths.get(mPaths.size() - 1).lineTo(lPoint.x, lPoint.y);
                    command = "H";
                }
            } else if (str[i].equals("v")) {
                String p[] = str[i + 1].split(",");
                for (int j = 0; j < p.length; j++) {
                    lPoint.set(lPoint.x, Float.valueOf(p[j].trim()) + lPoint.y);
                    mPath.lineTo(lPoint.x, lPoint.y);
                    mPaths.get(mPaths.size() - 1).lineTo(lPoint.x, lPoint.y);
                    command = "v";
                }
            } else if (str[i].equals("V")) {
                String p[] = str[i + 1].split(",");
                for (int j = 0; j < p.length; j++) {
                    lPoint.set(lPoint.x, Float.valueOf(p[j].trim()) - deviation_y);
                    mPath.lineTo(lPoint.x, lPoint.y);
                    mPaths.get(mPaths.size() - 1).lineTo(lPoint.x, lPoint.y);
                    command = "V";
                }
            } else if (str[i].equals("a")) {
                command = "a";
                String p[] = str[i + 1].split(",");
                lPoint = solve_a(p, lPoint, false);
            } else if (str[i].equals("A")) {
                command = "A";
                String p[] = str[i + 1].split(",");
                lPoint = solve_a(p, lPoint, true);
            } else if (str[i].equals("c")) {
                String p[] = str[i + 1].split(",");
                for (int j = 0; j < p.length; j++) {
                    float x1 = Float.valueOf(p[j].trim()) + lPoint.x;
                    float y1 = Float.valueOf(p[j + 1].trim()) + lPoint.y;
                    float x2 = Float.valueOf(p[j + 2].trim()) + lPoint.x;
                    float y2 = Float.valueOf(p[j + 3].trim()) + lPoint.y;
                    lPoint.set(Float.valueOf(p[j + 4].trim()) + lPoint.x, Float.valueOf(p[j + 5].trim()) + lPoint.y);
                    mPath.cubicTo(x1, y1, x2, y2, lPoint.x, lPoint.y);
                    mPaths.get(mPaths.size() - 1).cubicTo(x1, y1, x2, y2, lPoint.x, lPoint.y);
                    cPoint.set(x2, y2);
                    j = j + 5;
                    command = "c";
                }
            } else if (str[i].equals("C")) {
                String p[] = str[i + 1].split(",");
                for (int j = 0; j < p.length; j++) {
                    float x1 = Float.valueOf(p[j].trim()) - deviation_x;
                    float y1 = Float.valueOf(p[j + 1].trim()) - deviation_y;
                    float x2 = Float.valueOf(p[j + 2].trim()) - deviation_x;
                    float y2 = Float.valueOf(p[j + 3].trim()) - deviation_y;
                    lPoint.set(Float.valueOf(p[j + 4].trim()) - deviation_x, Float.valueOf(p[j + 5].trim()) - deviation_y);
                    mPath.cubicTo(x1, y1, x2, y2, lPoint.x, lPoint.y);
                    mPaths.get(mPaths.size() - 1).cubicTo(x1, y1, x2, y2, lPoint.x, lPoint.y);
                    cPoint.set(x2, y2);
                    j = j + 5;
                    command = "C";
                }
            } else if (str[i].equals("S")) {
                String p[] = str[i + 1].split(",");
                for (int j = 0; j < p.length; j++) {
                    float x1;
                    float y1;
                    float x2 = Float.valueOf(p[j].trim())-deviation_x;
                    float y2 = Float.valueOf(p[j + 1].trim())-deviation_y;
                    if (command.equals("C") || command.equals("c") || command.equals("s") || command.equals("S")) {
                        FloatPoint p1 = getSymmetryPoint(cPoint, lPoint);
                        x1 = p1.x;
                        y1 = p1.y;
                    } else {
                        x1 = x2;
                        y1 = y2;
                    }
                    lPoint.set(Float.valueOf(p[j + 2].trim()) - deviation_x, Float.valueOf(p[j + 3].trim()) - deviation_y);
                    mPath.cubicTo(x1, y1, x2, y2, lPoint.x, lPoint.y);
                    mPaths.get(mPaths.size() - 1).cubicTo(x1, y1, x2, y2, lPoint.x, lPoint.y);
                    cPoint.set(x2, y2);
                    j = j + 3;
                    command = "S";
                }
            } else if (str[i].equals("s")) {
                String p[] = str[i + 1].split(",");
                for (int j = 0; j < p.length; j++) {
                    float x1;
                    float y1;
                    float x2 = Float.valueOf(p[j].trim()) + lPoint.x;
                    float y2 = Float.valueOf(p[j + 1].trim()) + lPoint.y;
                    if (command.equals("C") || command.equals("c") || command.equals("s") || command.equals("S")) {
                        FloatPoint p1 = getSymmetryPoint(cPoint, lPoint);
                        x1 = p1.x;
                        y1 = p1.y;
                    } else {
                        x1 = x2;
                        y1 = y2;
                    }
                    lPoint.set(Float.valueOf(p[j + 2].trim()) + lPoint.x, Float.valueOf(p[j + 3].trim()) + lPoint.y);
                    mPath.cubicTo(x1, y1, x2, y2, lPoint.x, lPoint.y);
                    mPaths.get(mPaths.size() - 1).cubicTo(x1, y1, x2, y2, lPoint.x, lPoint.y);
                    cPoint.set(x2, y2);
                    j = j + 3;
                    command = "s";
                }
            } else if (str[i].equals("Z")) {
                command = "Z";
                mPath.close();
                lPoint = startPoint;
                mPaths.get(mPaths.size() - 1).close();
            } else if (str[i].equals("z")) {
                command = "z";
                mPath.close();
                lPoint = startPoint;
                mPaths.get(mPaths.size() - 1).close();
            } else if (str[i].equals("\",\"")) {
                mPaths.add(new Path());
            }
        }

        RectF rectF = new RectF();
        mPath.computeBounds(rectF,false);
//        Log.d("genius","坐标："+rectF.left+","+rectF.top+","+rectF.right+","+rectF.bottom);
        float offet = 10f;
        canvas_w = (rectF.right- rectF.left)+offet;
        canvas_h = (rectF.bottom - rectF.top)+offet;
        if(rectF.left!=offet/2&&rectF.top!=offet/2)
        {
            deviation_x = rectF.left-offet/2;
            deviation_y = rectF.top-offet/2;
        }
        if(getMeasuredWidth()!=0&&getMeasuredWidth() != 0)
        {
            scaleTimes_x = getMeasuredWidth()/canvas_w;
            scaleTimes_y = getMeasuredHeight()/canvas_h;
            setLayoutParams(new RelativeLayout.LayoutParams(getMeasuredWidth(),getMeasuredHeight()));
        }
        else {
            canvas_w = canvas_w*scaleTimes_x;
            canvas_h = canvas_h*scaleTimes_y;
            setLayoutParams(new RelativeLayout.LayoutParams((int)canvas_w,(int)canvas_h));
        }
    }

    /**
     * 处理path路径中的a标签
     *
     * @param p：a标签中的数据
     * @param lPoint：传路径上一个结束点
     * @param isAbPoint        :是否是绝对坐标（A:true,a:false）
     * @return 返回该标签处理完后的最后一个结束点
     */
    private FloatPoint solve_a(String p[], FloatPoint lPoint, boolean isAbPoint) {
        for (int j = 0; j < p.length; j++) {
            FloatPoint p2 = new FloatPoint();
            if (isAbPoint) {
                p2.set(Float.valueOf(p[j + 5].trim()) - deviation_x, Float.valueOf(p[j + 6].trim()) - deviation_y);
            } else {
                p2.set(Float.valueOf(p[j + 5].trim()) + lPoint.x, Float.valueOf(p[j + 6].trim()) + lPoint.y);
            }
            //1表示大角度弧线，0表示小角度弧线
            float largearcflag = Float.valueOf(p[j + 3].trim());
            //1为顺时针方向，0为逆时针方向
            float sweepflag = Float.valueOf(p[j + 4].trim());
            //半径
            float r = Float.valueOf(p[j].trim());
            ArrayList<FloatPoint> floatPoints = getCircleCenterPoint(lPoint, p2, Float.valueOf(p[j].trim()));
//                    Log.d("genius", "计算的圆心坐标：" + floatPoints.get(0).toString() + "-----" + floatPoints.get(1).toString());
            FloatPoint circleCenter = new FloatPoint();
            double startDegree = 0;
            double circleDegree = 0;
            if (floatPoints.size() > 1) {

                //圆心1在x轴上的交点
                FloatPoint c1 = new FloatPoint(floatPoints.get(0).x + r, floatPoints.get(0).y);
                //圆心1起始点夹角
                double angle11 = getPointDegree(lPoint, floatPoints.get(0), r);
                //圆心1终点夹角
                double angle12 = getPointDegree(p2, floatPoints.get(0), r);

                //圆心2在x轴上的交点
                FloatPoint c2 = new FloatPoint(floatPoints.get(1).x + r, floatPoints.get(1).y);
                //圆心2起始点夹角
                double angle21 = getPointDegree(lPoint, floatPoints.get(1), r);
                //圆心2终点夹角
                double angle22 = getPointDegree(p2, floatPoints.get(1), r);

                //小弧顺时针
                if (largearcflag == 0 && sweepflag == 1) {
                    double a1 = (360.0 - angle11) + angle12;
                    double a2 = angle12 - angle11;
                    if (a1 < 180 || (a2 > 0 && a2 < 180)) {
                        circleCenter = floatPoints.get(0);
                        startDegree = angle11;
                    } else {
                        circleCenter = floatPoints.get(1);
                        startDegree = angle21;
                    }
                    circleDegree = getTwoPointDegree(lPoint, p2, circleCenter);
                }
                //小弧逆时针
                else if (largearcflag == 0 && sweepflag == 0) {
                    double a1 = (360.0 - angle12) + angle11;
                    double a2 = angle11 - angle12;
                    if (a1 < 180.0 || (a2 > 0.0 && a2 < 180.0)) {
                        circleCenter = floatPoints.get(0);
                        startDegree = angle11;
                    } else {
                        circleCenter = floatPoints.get(1);
                        startDegree = angle21;
                    }
                    circleDegree = 0 - getTwoPointDegree(lPoint, p2, circleCenter);
                }
                //大弧顺时针
                else if (largearcflag == 1 && sweepflag == 1) {
                    double a1 = (360.0 - angle12) + angle11;
                    double a2 = angle11 - angle12;
                    if (a1 < 180.0 || (a2 > 0.0 && a2 < 180.0)) {
                        circleCenter = floatPoints.get(0);
                        startDegree = angle11;
                    } else {
                        circleCenter = floatPoints.get(1);
                        startDegree = angle21;
                    }
                    circleDegree = 360 - getTwoPointDegree(lPoint, p2, circleCenter);
                }
                //大弧逆时针
                if (largearcflag == 1 && sweepflag == 0) {
                    double a1 = (360.0 - angle11) + angle12;
                    double a2 = angle12 - angle11;
                    if (a1 < 180 || (a2 > 0 && a2 < 180)) {
                        circleCenter = floatPoints.get(0);
                        startDegree = angle11;
                    } else {
                        circleCenter = floatPoints.get(1);
                        startDegree = angle21;
                    }
                    circleDegree = 0 - (360 - getTwoPointDegree(lPoint, p2, circleCenter));
                }

            } else if (floatPoints.size() == 1) {
                circleCenter = floatPoints.get(0);
                //圆心1在x轴上的交点
                FloatPoint c1 = new FloatPoint(floatPoints.get(0).x + r, floatPoints.get(0).y);
                //圆心1起始点夹角
                startDegree = getPointDegree(lPoint, floatPoints.get(0), r);
                //圆心1终点夹角
                double angel12 = getPointDegree(p2, floatPoints.get(0), r);

                if (largearcflag == 1) {
                    circleDegree = 360 - getTwoPointDegree(lPoint, p2, circleCenter);
                } else {
                    circleDegree = getTwoPointDegree(lPoint, p2, circleCenter);
                }

                if (sweepflag == 0) {
                    circleDegree = -circleDegree;
                }
            } else {
                Log.e("genius", "该圆心不存在");
            }
//                    Log.d("genius", "实际的圆心坐标：" + circleCenter.toString());
//                    Log.d("genius", "实际的圆心角：" + circleDegree);

            if (floatPoints.size() > 0) {
                mPath.arcTo(new RectF(circleCenter.x - r, circleCenter.y - r, circleCenter.x + r, circleCenter.y + r), (float) startDegree, (float) circleDegree, false);
                mPaths.get(mPaths.size() - 1).arcTo(new RectF(circleCenter.x - r, circleCenter.y - r, circleCenter.x + r, circleCenter.y + r), (float) startDegree, (float) circleDegree, false);
            }
            lPoint = p2;
            j = j + 6;
        }
        return lPoint;
    }

    /**
     * 处理每个笔画需要经过的点的集合
     */
    private void solvePassPoint() {
        if(TextUtils.isEmpty(pinyinStroke))
        {
            return;
        }

        String line[] = pinyinStroke.split("\",\"");
        for (int i = 0; i < line.length; i++) {
            ArrayList<FloatPoint> tmp = new ArrayList<>();
            line[i] = line[i].replace("\"","");
            line[i] = line[i].replace("[", "");
            line[i] = line[i].replace("]", "");
            line[i] = line[i].replace(",", " ");
            String arr[] = line[i].split(" ");
            for (int j = 0; j < arr.length; j++) {
                FloatPoint floatPoint = new FloatPoint(Float.valueOf(arr[j]) - deviation_x, Float.valueOf(arr[j + 1]) - deviation_y);
                tmp.add(floatPoint);
                j++;
            }
            mPointDatas.add(tmp);
        }

        solvePassPath();
    }

    /**
     * 处理每个笔画经过的点所在的路径
     */
    private void solvePassPath() {
        for (int i = 0; i < mPointDatas.size(); i++) {
            Path path = new Path();
            for (int j = 0; j < mPointDatas.get(i).size(); j++) {
                if (j == 0) {
                    path.moveTo(mPointDatas.get(i).get(j).x, mPointDatas.get(i).get(j).y);
                } else {
                    path.lineTo(mPointDatas.get(i).get(j).x, mPointDatas.get(i).get(j).y);
                }
            }
//            path = scalePath(path);
            PathMeasure pathMeasure = new PathMeasure(scalePath(path), false);
            Log.d("genius", "Path长度：" + pathMeasure.getLength());
            mLengths.add(pathMeasure.getLength());
            mPointPath.add(path);
        }
    }

    /**
     * 自动描红播放
     */
    public void autoPlay() {
        if (!isAutoPlay) {
            clear();
            isAutoPlay = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < mPointPath.size(); i++) {
                        Path path = mPointPath.get(i);
                        PathMeasure pathMeasure = new PathMeasure(path, false);
                        int step = 6;
                        for (int j = 0; j < pathMeasure.getLength() / step; j++) {
                            drawPath.reset();
                            pathMeasure.getSegment(0, j * step, drawPath, true);
                            drawPath = scalePath(drawPath);
                            PathMeasure pathMeasure1 = new PathMeasure(drawPath, false);
                            Log.d("genius", "drawPath长度：" + pathMeasure1.getLength());
                            postInvalidate();
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //停止动画
                            if(!isAutoPlay)
                            {
                                return;
                            }
                        }
                        ++currentIndex;
                        getNoticePath();
                        postInvalidate();
                    }
                    isAutoPlay = false;

                }
            }).start();
        }
    }

    /**
     * 创建拼音图层(其中的一个笔画)
     *
     * @return
     */
    private Bitmap characterBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
//        canvas.drawColor(Color.WHITE);
        //初始化文字画笔
        Paint characterPaint = new Paint();
        characterPaint.setStyle(Paint.Style.FILL);
        characterPaint.setColor(Color.GREEN);
        // 抗锯齿
        characterPaint.setAntiAlias(true);
        // 防抖动
        characterPaint.setDither(true);
        characterPaint.setStrokeWidth(LINEWIDTH);

        canvas.drawPath(scalePath(mPaths.get(currentIndex)), characterPaint);
        return bitmap;
    }

    /**
     * 获取下一笔画的提示path
     */
    private void getNoticePath() {
        noticePath.reset();
        if (isShowNotice) {
            if (currentIndex < mPointDatas.size()) {
                for (int i = 0; i < mPointDatas.get(currentIndex).size(); i++) {
                    FloatPoint floatPoint = mPointDatas.get(currentIndex).get(i);
                    if (i == 0) {
                        noticePath.moveTo(floatPoint.x, floatPoint.y);
                    } else {
                        noticePath.quadTo(floatPoint.x, floatPoint.y, floatPoint.x, floatPoint.y);
                    }
                }
                PathMeasure measure = new PathMeasure(noticePath, false);
                Path path1 = new Path();
                measure.getSegment(measure.getLength() - 15, measure.getLength(), path1, true);
                Matrix matrix = new Matrix();
                FloatPoint p = mPointDatas.get(currentIndex).get(mPointDatas.get(currentIndex).size() - 1);
                matrix.setRotate(30, p.x, p.y);
                noticePath.addPath(path1, matrix);
                matrix.setRotate(-30, p.x, p.y);
                noticePath.addPath(path1, matrix);
            }
        }
    }

    /**
     * path缩放处理
     *
     * @param oldPath
     * @return
     */
    private Path scalePath(Path oldPath) {
        Path path = new Path();
        Matrix matrix = new Matrix();
        matrix.setScale(scaleTimes_x, scaleTimes_y);
        path.addPath(oldPath, matrix);
        return path;
    }

    /**
     * 绘制手绘path
     *
     * @param canvas
     */
    private void drawPath(Canvas canvas) {
        canvas.drawPath(drawPath, drawPaint);
    }

    /**
     * 绘制提示线
     *
     * @param canvas
     */
    private void drawNoticePath(Canvas canvas) {
        canvas.drawPath(scalePath(noticePath), noticePaint);
    }

    /**
     * 绘制已绘过的区域
     *
     * @param canvas
     */
    private void drawStroke(Canvas canvas) {
        drawStrokePaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < currentIndex; i++) {
            canvas.drawPath(scalePath(mPaths.get(i)), drawStrokePaint);
        }
    }

    /**
     * 画拼音轮廓
     *
     * @param canvas
     */
    private void drawPinyinStroke(Canvas canvas) {
        drawStrokePaint.setStyle(Paint.Style.STROKE);
//        mPaint.setColor(Color.YELLOW);
        canvas.drawPath(scalePath(mPath), drawStrokePaint);
    }

    /**
     * 已知一点的坐标和圆心的坐标求该点与x轴正方向之间的夹角
     *
     * @param p1
     * @param radius       ：圆的半径
     * @param circleCenter
     */
    private double getPointDegree(FloatPoint p1, FloatPoint circleCenter, float radius) {
        float a = p1.x - circleCenter.x;
        float b = p1.y - circleCenter.y;
        float c = radius;
        float d = 0;

        double rads = Math.acos((((a * c) + (b * d)) / ((Math.sqrt(a * a + b * b)) * (Math.sqrt(c * c + d * d)))));
        double angle = Math.toDegrees(rads);
        if (p1.y < circleCenter.y) {
            angle = 360 - angle;
        }
        return angle;
    }

    /**
     * 已知两点的坐标和圆心的坐标求圆心角（小于180度的角）
     *
     * @param p1
     * @param p2:
     * @param circleCenter
     */
    private double getTwoPointDegree(FloatPoint p1, FloatPoint p2, FloatPoint circleCenter) {
        float a = p1.x - circleCenter.x;
        float b = p1.y - circleCenter.y;
        float c = p2.x - circleCenter.x;
        float d = p2.y - circleCenter.y;

        double rads = Math.acos((((a * c) + (b * d)) / ((Math.sqrt(a * a + b * b)) * (Math.sqrt(c * c + d * d)))));
        double angle = Math.toDegrees(rads);
//        if (p1.y>circleCenter.y) {
//            angle = 360 - angle;
//        }
        return angle;
    }

    /**
     * 已知两点的坐标和半径求圆心坐标
     *
     * @param p1
     * @param p2
     * @param r
     */
    private ArrayList<FloatPoint> getCircleCenterPoint(FloatPoint p1, FloatPoint p2, Float r) {
        ArrayList<FloatPoint> floatPoints = new ArrayList<>();

        float a = (p1.x * p1.x - p2.x * p2.x + p1.y * p1.y - p2.y * p2.y) / 2 / (p1.x - p2.x);
        float b = (p1.y - p2.y) / (p1.x - p2.x);
        float A = b * b + 1;
        float B = 2 * p1.x * b - 2 * a * b - 2 * p1.y;
        float C = p1.x * p1.x + a * a - 2 * p1.x * a + p1.y * p1.y - r * r;
        float D = B * B - 4 * A * C;
        if (D < 0) {
            return floatPoints;
        } else if (D > 0) {
            float y1 = (float) ((-B + Math.sqrt(D)) / 2 / A);
            float x1 = a - b * y1;
            float y2 = (float) ((-B - Math.sqrt(D)) / 2 / A);
            float x2 = a - b * y2;
            FloatPoint floatPoint1 = new FloatPoint();
            floatPoint1.set(x1, y1);
            floatPoints.add(floatPoint1);
            FloatPoint floatPoint2 = new FloatPoint();
            floatPoint2.set(x2, y2);
            floatPoints.add(floatPoint2);
            return floatPoints;
        } else {
            float y1 = (float) ((-B + Math.sqrt(D)) / 2 / A);
            float x1 = a - b * y1;
            FloatPoint floatPoint1 = new FloatPoint();
            floatPoint1.set(x1, y1);
            floatPoints.add(floatPoint1);
            return floatPoints;
        }
    }

    /**
     * 求一个点关于另一个点的对称点
     *
     * @param p1
     * @param centerPoint
     * @return
     */
    private FloatPoint getSymmetryPoint(FloatPoint p1, FloatPoint centerPoint) {
        FloatPoint floatPoint = new FloatPoint();
        floatPoint.setX(2 * centerPoint.x - p1.x);
        floatPoint.setY(2 * centerPoint.y - p1.y);
        return floatPoint;
    }

    /**
     * 计算两点之间的距离
     *
     * @param p1
     * @param p2
     */
    private double pointDistance(FloatPoint p1, FloatPoint p2) {
        float s1 = Math.abs(p1.x - p2.x);
        float s2 = Math.abs(p1.y - p2.y);
        double distance = Math.sqrt(s1 * s1 + s2 * s2);
        return distance;
    }

    float currentX, currentY;
    float startX, startY, endX, endY;
    double drawDistance;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isAutoPlay || !isCanTouch) {
            return false;
        }

        float eventx = event.getX();
        float eventy = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawDistance = 0;
                startX = eventx;
                startY = eventy;
                drawPath.reset();

                if (currentIndex < mPointDatas.size()) {
                    //判断起始点是否在给定的区域内
                    touchRect.set((int) startX - deviations, (int) startY - deviations, (int) startX + deviations, (int) startY + deviations);
                    FloatPoint startPoint = mPointDatas.get(currentIndex).get(0);
                    FloatPoint startPonitNew = new FloatPoint(startPoint.x * scaleTimes_x, startPoint.y * scaleTimes_y);
                    //获取笔画起始点的rect
                    matchRect = new RectF(startPonitNew.x - deviations, startPonitNew.y - deviations, startPonitNew.x + deviations, startPonitNew.y + deviations);

                    if (matchRect.intersect(touchRect)) {
                        Toast.makeText(mContext, "在合理范围内", Toast.LENGTH_LONG).show();
                        isLegalOp = true;
                        drawPath.moveTo(startPonitNew.x, startPonitNew.y);
                    } else {
                        Toast.makeText(mContext, "***不在合理范围内***", Toast.LENGTH_LONG).show();
                        isLegalOp = false;
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                currentX = eventx;
                currentY = eventy;
                if (Math.abs(currentX - startX) > 10 || Math.abs(currentY - startY) > 10) {
                    FloatPoint p1 = new FloatPoint(startX, startY);
                    FloatPoint p2 = new FloatPoint(currentX, currentY);
                    drawDistance += pointDistance(p1, p2);
                    startX = currentX;
                    startY = currentY;
                }
                Log.d("genius", "滑动的距离：" + drawDistance);
                if (isLegalOp) {
                    drawPath.quadTo(currentX, currentY, eventx, eventy); // 画线
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                endX = eventx;
                endY = eventy;

                if (isLegalOp) {
                    if (currentIndex < mPointDatas.size()) {
                        //笔画的结束点
                        FloatPoint endPoint = mPointDatas.get(currentIndex).get(mPointDatas.get(currentIndex).size() - 1);
                        FloatPoint endPointNew = new FloatPoint(endPoint.x * scaleTimes_x, endPoint.y * scaleTimes_y);
                        touchRect.set(endX - deviations, endY - deviations, (int) endX + deviations, (int) endY + deviations);
                        //获取笔画终点的rect
                        matchRect = new RectF(endPointNew.x - deviations, endPointNew.y - deviations, endPointNew.x + deviations, endPointNew.y + deviations);
                        //判断绘制的长度是否超过笔画的长度或者小于笔画的一半
                        if (drawDistance > mLengths.get(currentIndex) + deviations || drawDistance < (mLengths.get(currentIndex) - deviations)/2) {
                            isLegalOp = false;
                            drawPath.reset();
                        } else {
                            //判断结束点是否在给定的区域内
                            if (matchRect.intersect(touchRect)) {
                                isLegalOp = true;
                                if (currentIndex < mPointDatas.size()) {
                                    currentIndex++;
                                    getNoticePath();
                                }
                            } else {
                                isLegalOp = false;
                                drawPath.reset();
                            }
                        }
                    }
                }
                drawPath.reset();
                invalidate();
                break;
            default:
                return false;
        }
        return true;
    }

    public String getPinyinData() {
        return pinyinData;
    }

    public void setPinyinData(final String pinyinData) {
        this.pinyinData = pinyinData;
    }

    public String getPinyinStroke() {
        return pinyinStroke;
    }

    public void setPinyinStroke(final String pinyinStroke) {
        this.pinyinStroke = pinyinStroke;
    }

    public boolean isCanTouch() {
        return isCanTouch;
    }

    public void setCanTouch(final boolean canTouch) {
        isCanTouch = canTouch;
    }
}
