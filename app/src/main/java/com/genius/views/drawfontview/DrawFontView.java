package com.genius.views.drawfontview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * M = moveto
 * L = lineto
 * H = horizontal lineto
 * V = vertical lineto
 * C = curveto
 * S = smooth curveto
 * Q = quadratic Belzier curve
 * T = smooth quadratic Belzier curveto
 * A = elliptical Arc
 * Z = closepath
 */
public class DrawFontView extends View {
    //M startPoint Q controlPoint endPoint

    /**
     * 汉字数据
     */
    private String chineseWordData;
    /**
     * 汉字笔画
     */
    private String chineseWordStroke;

//    String wu =
//            "M 334 759 Q 328 760 323 760 Q 310 763 307 757 Q 300 750 310 734 Q 341 679 361 585 Q 365 557 383 539 Q 402 517 407 534 Q 408 541 410 551 L 407 587 Q 403 602 382 723 L 334 759 Z," +
//                    "M 660 626 Q 687 705 721 731 Q 739 749 724 767 Q 655 824 599 802 Q 413 742 334 759 L 382 723 Q 607 774 626 756 Q 641 740 608 632 L 660 626 Z," +
//                    "M 410 551 Q 416 551 423 552 Q 501 571 672 591 Q 682 592 684 602 Q 684 609 660 626 L 608 632 Q 605 633 602 632 Q 494 602 407 587 L 410 551 Z," +
//                    "M 508 429 Q 581 442 658 455 Q 716 468 726 475 Q 735 482 731 492 Q 724 504 694 513 Q 664 520 578 492 Q 436 458 319 449 Q 282 445 308 426 Q 344 402 420 415 Q 433 418 449 419 L 508 429 Z\"," +
//                    "M 519 274 Q 633 286 800 283 Q 830 282 855 283 Q 877 282 883 292 Q 889 305 871 320 Q 814 363 772 355 Q 678 337 519 314 L 458 305 Q 320 290 162 272 Q 140 271 156 251 Q 172 235 191 229 Q 215 223 232 228 Q 334 255 451 267 L 519 274 Z," +
//                    "M 519 314 Q 534 384 539 393 Q 546 400 540 411 Q 534 418 508 429 L 449 419 Q 467 367 458 305 L 451 267 Q 415 144 310 76 Q 265 49 185 15 Q 158 2 190 1 Q 211 -2 252 11 Q 304 23 350 47 Q 393 66 426 105 Q 469 153 504 253 L 519 314 Z," +
//                    "M 504 253 Q 505 252 510 246 Q 666 18 719 -1 Q 797 -19 906 -7 Q 927 -6 929 -1 Q 932 6 916 14 Q 867 36 808 59 Q 679 116 541 254 Q 528 267 519 274 L 508.90848379915906 272.96116744991343 L 504 253 Z";

//    String iii =
//            "[[[316,749],[357,701],[395,539]]," +
//                    "[[349,755],[354,748],[393,738],[594,782],[641,780],[675,748],[642,654],[616,644]]," +
//                    "[[416,558],[426,572],[599,606],[651,608],[675,602]]," +
//                    "[[311,439],[350,431],[391,433],[672,487],[719,486]]," +
//                    "[[159,261],[214,251],[363,276],[782,320],[833,312],[870,299]]," +
//                    "[[457,414],[496,388],[496,370],[479,267],[437,166],[409,127],[369,87],[296,44],[222,15],[196,11]]," +
//                    "[[515,266],[527,242],[537,237],[591,172],[658,105],[727,48],[786,27],[923,1]]]";

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
    //Q control
    //L
    //Z
    private static final int LINEWIDTH = 5;
    private static final int POINTWIDTH = 10;

    private Context mContext;
    /**
     * 默认画布的大小
     */
    private int pixel = 1000;
    /**
     * 画布的实际宽
     */
    private float canvas_w;
    /**
     * 画布的实际高
     */
    private float canvas_h;
    /**
     * 汉字路径在x轴的偏移量
     */
    private float deviation_x;
    /**
     * 汉字路径在y轴的偏移量
     */
    private float deviation_y;
    Paint mPaint;
    Path mPath;

    ArrayList<FontData> fontDatas = new ArrayList<>();

    private int mScreenWidth;
    private int mScreenHeight;

    /**
     * 手绘画笔
     */
    private Paint drawPaint;
    /**
     * 画轮廓的画笔
     */
    private Paint drawStrokePaint;
    /**
     * 汉字画笔
     */
    private Paint characterPaint;
    /**
     * 提示画笔
     */
    private Paint noticePaint;
    /**
     * 存储汉字每一笔画的所有点
     */
    private ArrayList<ArrayList<FontData>> mfontDatas = new ArrayList<>();
    /**
     * 每个笔画的路径
     */
    private ArrayList<Path> mPaths = new ArrayList<>();
    /**
     * 当前的笔画
     */
    private int currentIndex;
    /**
     * 每个笔画需要经过的点的集合
     */
    private ArrayList<ArrayList<Point>> mPointDatas = new ArrayList<>();
    /**
     * 每个笔画需要经过的点的路径
     */
    private ArrayList<Path> mPointPath = new ArrayList<>();
    /**
     * 每个笔画的长度
     */
    private ArrayList<Float> mLengths = new ArrayList<>();
    /**
     * 误差范围
     */
    private int deviations = 80;
    /**
     * 是否是合法操作
     */
    private boolean isLegalOp;
    /**
     * 手指触摸的区域
     */
    private Rect touchRect;
    /**
     * 要匹配的区域
     */
    private Rect matchRect;
    /**
     * 手绘的路径
     */
    private Path drawPath;
    /**
     * 是否在自动播放
     */
    private boolean isAutoPlay;
    /**
     * 下一笔画提示的path
     */
    private Path noticePath;
    /**
     * 是否显示提示笔画
     */
    private boolean isShowNotice = true;
    /**
     * 画布缩放的倍数
     */
    private float scaleTimes_x = 1.0f;
    private float scaleTimes_y = 1.0f;
    private float scaleTimes = 1.0f;
    /**
     * 是否可以描红
     */
    private boolean isCanTouch;

    public DrawFontView(Context context) {
        this(context, null);
    }

    public DrawFontView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawFontView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        setLayerType(View.LAYER_TYPE_SOFTWARE, null);//关掉硬件加速
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPath = new Path();
        // 抗锯齿
        mPaint.setAntiAlias(true);
        // 防抖动
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mContext = context;
        getScreenParams();

        initPaints();
        initRect();
        drawPath = new Path();
        noticePath = new Path();

//        initData(chineseWordData, chineseWordStroke,true);
    }

    /**
     * 初始化汉字数据
     *
     * @param chineseWordData   :汉字原数据
     * @param chineseWordStroke :汉字笔画数据
     * @param isFill            :是否是填充
     */
    public void initData(String chineseWordData, String chineseWordStroke, final boolean isFill, final boolean isPlay) {
        setChineseWordData(chineseWordData);
        setChineseWordStroke(chineseWordStroke);
        //解决获取的getMeasuredWidth()=0的情况
        post(new Runnable() {
            @Override
            public void run() {
                if(isAutoPlay)
                {
                    isAutoPlay = false;
                    drawPath.reset();
                }
                mfontDatas.clear();
                fontDatas.clear();
                mPaths.clear();
                mPointDatas.clear();
                mPointPath.clear();
                initPointData();
                initPoints();
                solvePassPoint();
                if (isFill) {
                    currentIndex = mPointPath.size();
                }
                else {
                    currentIndex = 0;
                    if(isPlay)
                    {
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                autoPlay();
                            }
                        },1000);
                    }
                }
                getNoticePath();
                invalidate();
            }
        });
    }

    /**
     * 清除描红
     */
    public void clear() {
        if (!isAutoPlay) {
            currentIndex = 0;
            postInvalidate();
            getNoticePath();
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
                        int step = 10;
                        for (int j = 0; j < pathMeasure.getLength() / step; j++) {
                            drawPath.reset();
                            pathMeasure.getSegment(0, j * step, drawPath, true);
//                            drawPath = scalePath(drawPath);
                            PathMeasure pathMeasure1 = new PathMeasure(drawPath, false);
                            Log.d("genius", "drawPath长度：" + pathMeasure1.getLength());
                            postInvalidate();
                            try {
                                Thread.sleep(40);
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

//    /**
//     * 自动描红播放
//     */
//    public void autoPlay() {
//        if (!isAutoPlay) {
//            clear();
//            isAutoPlay = true;
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
////                while (index<fontDatas.size()){
////                    index++;
//                    for (int i = 0; i < mPointDatas.size(); i++) {
//                        drawPath.reset();
//                        for (int j = 0; j < mPointDatas.get(i).size(); j++) {
//                            if (j == 0) {
//                                drawPath.moveTo(mPointDatas.get(i).get(j).x - 20-deviation_x, pixel - mPointDatas.get(i).get(j).y - 20-deviation_y);
//                            } else {
//                                int startx = mPointDatas.get(i).get(j - 1).x-deviation_x;
//                                int starty = pixel - mPointDatas.get(i).get(j - 1).y-deviation_y;
//                                int endx = mPointDatas.get(i).get(j).x-deviation_x;
//                                int endy = pixel - mPointDatas.get(i).get(j).y-deviation_y;
//                                int step = 20;
//                                float stepx = (float) (endx - startx) / step;
//                                float stepy = (float) (endy - starty) / (float) step;
//
//                                for (int k = 0; k < step; k++) {
//                                    drawPath.lineTo(startx + stepx * k, starty + stepy * k);
//                                    postInvalidate();
//                                    try {
//                                        Thread.sleep(10);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//                            }
//                        }
//                        ++currentIndex;
//                        getNoticePath();
//                        postInvalidate();
//                    }
//                    isAutoPlay = false;
////                }
//                }
//            }).start();
//        }
//    }

    /**
     * 获取下一笔画的提示path
     */
    private void getNoticePath() {
        noticePath.reset();
        if (isShowNotice) {
            if (currentIndex < mPointDatas.size()) {
                for (int i = 0; i < mPointDatas.get(currentIndex).size(); i++) {
                    Point point = mPointDatas.get(currentIndex).get(i);
                    if (i == 0) {
                        noticePath.moveTo(point.x - deviation_x, pixel - point.y - deviation_y);
                    } else {
                        noticePath.quadTo(point.x - deviation_x, pixel - point.y - deviation_y, point.x - deviation_x, pixel - point.y - deviation_y);
                    }
                }
                PathMeasure measure = new PathMeasure(noticePath, false);
                Path path1 = new Path();
                measure.getSegment(measure.getLength() - 15, measure.getLength(), path1, true);
                Matrix matrix = new Matrix();
                Point p = mPointDatas.get(currentIndex).get(mPointDatas.get(currentIndex).size() - 1);
                matrix.setRotate(30, p.x - deviation_x, pixel - p.y - deviation_y);
                noticePath.addPath(path1, matrix);
                matrix.setRotate(-30, p.x - deviation_x, pixel - p.y - deviation_y);
                noticePath.addPath(path1, matrix);
            }
        }
    }

    /**
     * 初始化手绘画笔
     */
    private void initPaints() {

        drawPaint = new Paint();
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setColor(Color.BLUE);
        // 抗锯齿
        drawPaint.setAntiAlias(true);
        // 防抖动
        drawPaint.setDither(true);
        drawPaint.setStrokeWidth(100);

        //初始化文字画笔
        characterPaint = new Paint();
        characterPaint.setStyle(Paint.Style.FILL);
        characterPaint.setColor(Color.GREEN);
        // 抗锯齿
        characterPaint.setAntiAlias(true);
        // 防抖动
        characterPaint.setDither(true);
        characterPaint.setStrokeWidth(5);

        noticePaint = new Paint();
        noticePaint.setStyle(Paint.Style.STROKE);
        noticePaint.setColor(Color.RED);
        // 抗锯齿
        noticePaint.setAntiAlias(true);
        // 防抖动
        noticePaint.setDither(true);
        noticePaint.setStrokeWidth(5);

        drawStrokePaint = new Paint();
        // 抗锯齿
        drawStrokePaint.setAntiAlias(true);
        // 防抖动
        drawStrokePaint.setDither(true);
        drawStrokePaint.setStyle(Paint.Style.FILL);
        drawStrokePaint.setStrokeWidth(LINEWIDTH);
    }

    /**
     * 初始化各个区域
     */
    private void initRect() {
        touchRect = new Rect();
        matchRect = new Rect();
    }

    class FontData {
        String type;
        FloatPoint start;
        FloatPoint control;
        FloatPoint end;
    }

    private void getScreenParams() {
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        mScreenWidth = display.getWidth();
        mScreenHeight = display.getHeight();
    }

    /**
     * 处理每个笔画需要经过的点的集合
     */
    private void solvePassPoint() {

        if (TextUtils.isEmpty(chineseWordStroke)) {
            return;
        }
        String line[] = chineseWordStroke.split("]],");
        for (int i = 0; i < line.length; i++) {
            ArrayList<Point> tmp = new ArrayList<>();
            String arr[] = line[i].split("],");
            for (int j = 0; j < arr.length; j++) {
                String str = arr[j].replace("[", "").replace("]", "");
                String result[] = str.split(",");
                Point point = new Point(Integer.parseInt(result[0]), Integer.parseInt(result[1]));
//                Log.d("数组", str);
                tmp.add(point);
            }
            mPointDatas.add(tmp);
        }

        solvePassPath();
    }

    /**
     * 处理每个笔画经过的点所在的路径
     */
    private void solvePassPath() {
        mLengths.clear();
        for (int i = 0; i < mPointDatas.size(); i++) {
            Path path = new Path();
            for (int j = 0; j < mPointDatas.get(i).size(); j++) {
                Point point = mPointDatas.get(i).get(j);
                if (j == 0) {
                    path.moveTo(point.x - deviation_x - 10, pixel - point.y - deviation_y - 10);
                } else {
                    path.quadTo(point.x - deviation_x, pixel - point.y - deviation_y, point.x - deviation_x, pixel - point.y - deviation_y);
                }
            }
//            path = scalePath(path);
            PathMeasure pathMeasure = new PathMeasure(path, false);
            Log.d("genius", "Path长度：" + pathMeasure.getLength());
            mLengths.add(pathMeasure.getLength());
            mPointPath.add(path);
        }
    }

    /**
     * 初始化汉字的数据
     */
    private void initPoints() {
//        String x = "M 334 759 Q 328 760 323 760 Q 310 763 307 757 Q 300 750 310 734 Q 341 679 361 585 Q 365 557 383 539 Q 402 517 407 534 Q 408 541 410 551 L 407 587 Q 403 602 382 723 L 334 759 Z";
        if (TextUtils.isEmpty(chineseWordData)) {
            return;
        }
        chineseWordData = chineseWordData.replace("[\"", "");
        chineseWordData = chineseWordData.replace("\"]", "");
        String line[] = chineseWordData.split("\",\"");
        for (int y = 0; y < line.length; y++) {
            ArrayList<FontData> tmp = new ArrayList<>();
            String l = line[y];
            String p[] = l.split(" ");
            for (int i = 0; i < p.length; i++) {
                if ("M".equals(p[i].trim())) {
                    FontData data = new FontData();
                    data.type = "M";
                    data.start = new FloatPoint();
                    data.start.x = Float.valueOf(p[i + 1].trim()).intValue() - deviation_x;
                    data.start.y = pixel - Float.valueOf(p[i + 2].trim()).intValue() - deviation_y;
                    fontDatas.add(data);
                    tmp.add(data);
                    i = i + 2;
                } else if ("Q".equals(p[i].trim())) {
                    FontData data = new FontData();
                    data.type = "Q";
                    data.control = new FloatPoint();
                    data.control.x = Float.valueOf(p[i + 1].trim()).intValue() - deviation_x;
                    data.control.y = pixel - Float.valueOf(p[i + 2].trim()).intValue() - deviation_y;
                    data.end = new FloatPoint();
                    data.end.x = Float.valueOf(p[i + 3].trim()).intValue() - deviation_x;
                    data.end.y = pixel - Float.valueOf(p[i + 4].trim()).intValue() - deviation_y;
                    fontDatas.add(data);
                    tmp.add(data);
                    i = i + 4;
                } else if ("L".equals(p[i].trim())) {
                    FontData data = new FontData();
                    data.type = "L";
                    data.end = new FloatPoint();
                    data.end.x = Float.valueOf(p[i + 1].trim()).intValue() - deviation_x;
                    data.end.y = pixel - Float.valueOf(p[i + 2].trim()).intValue() - deviation_y;
                    fontDatas.add(data);
                    tmp.add(data);
                    i = i + 2;
                } else if ("Z".equals(p[i].trim())) {
                    FontData data = new FontData();
                    data.type = "Z";
                }

            }
            mfontDatas.add(tmp);
        }

        //遍历存储每一个笔画所在的path
        mPath.reset();
        for (int i = 0; i < mfontDatas.size(); i++) {
            Path path = new Path();
            for (int j = 0; j < mfontDatas.get(i).size(); j++) {
                FontData fontData = mfontDatas.get(i).get(j);
                if ("M".equals(fontData.type)) {
                    path.moveTo(fontData.start.x, fontData.start.y);
                    mPath.moveTo(fontData.start.x, fontData.start.y);
                } else if ("Q".equals(fontData.type)) {
                    path.quadTo(fontData.control.x, fontData.control.y,
                            fontData.end.x, fontData.end.y);
                    mPath.quadTo(fontData.control.x, fontData.control.y,
                            fontData.end.x, fontData.end.y);
                } else if ("L".equals(fontData.type)) {
                    path.lineTo(fontData.end.x, fontData.end.y);
                    mPath.lineTo(fontData.end.x, fontData.end.y);
                } else if ("Z".equals(fontData.type)) {
                    path.close();
                    mPath.close();
                }
            }
            mPaths.add(path);
        }
    }

    /**
     * 初始化原始数据，得出原始数据所在的rect,拿到画布的宽和高以及x轴和y轴的偏移量
     */
    private void initPointData() {
        if (TextUtils.isEmpty(chineseWordData)) {
            return;
        }
        Path tmpPath = new Path();
        chineseWordData = chineseWordData.replace("[\"", "");
        chineseWordData = chineseWordData.replace("\"]", "");
        String line[] = chineseWordData.split("\",\"");
        for (int y = 0; y < line.length; y++) {
            String l = line[y];
            String p[] = l.split(" ");
            for (int i = 0; i < p.length; i++) {
                if ("M".equals(p[i].trim())) {
                    tmpPath.moveTo(Float.valueOf(p[i + 1].trim()).intValue(), pixel - Float.valueOf(p[i + 2].trim()).intValue());
                    i = i + 2;
                } else if ("Q".equals(p[i].trim())) {
                    tmpPath.quadTo(Float.valueOf(p[i + 1].trim()).intValue(), pixel - Float.valueOf(p[i + 2].trim()).intValue(), Float.valueOf(p[i + 3].trim()).intValue(), pixel - Float.valueOf(p[i + 4].trim()).intValue());
                    i = i + 4;
                } else if ("L".equals(p[i].trim())) {
                    tmpPath.lineTo(Float.valueOf(p[i + 1].trim()).intValue(), pixel - Float.valueOf(p[i + 2].trim()).intValue());
                    i = i + 2;
                } else if ("Z".equals(p[i].trim())) {
                    tmpPath.close();
                }

            }
        }

        RectF rectF = new RectF();
        tmpPath.computeBounds(rectF, false);
//        Log.d("genius","坐标："+rectF.left+","+rectF.top+","+rectF.right+","+rectF.bottom);
        float offet = 10;
        canvas_w = (rectF.right - rectF.left) + offet;
        canvas_h = (rectF.bottom - rectF.top) + offet;
        if (rectF.left != 0 || rectF.top != 0) {
            deviation_x = rectF.left - offet / 2;
            deviation_y = rectF.top - offet / 2;
        }
        if (getMeasuredWidth() != 0 && getMeasuredWidth() != 0) {
            scaleTimes_x = getMeasuredWidth() / canvas_w;
            scaleTimes_y = getMeasuredHeight() / canvas_h;
            scaleTimes = Math.min(scaleTimes_x,scaleTimes_y);

            deviation_y = deviation_y-pixel/2+canvas_h;

            setLayoutParams(new RelativeLayout.LayoutParams(getMeasuredWidth(), getMeasuredHeight()));
        } else {
            setLayoutParams(new RelativeLayout.LayoutParams((int) canvas_w, (int) canvas_h));
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //缩放画布
        canvas.scale(scaleTimes, scaleTimes);

        //原始画布(黄色)
        canvas.drawColor(Color.YELLOW);
        drawBezier(canvas);
        if (currentIndex > 0) {
            drawStroke(canvas);
        }
        canvas.drawPath(noticePath, noticePaint);
        if (currentIndex < mfontDatas.size()) {
            //新建一个画布(画布是透明的)
            int layid = canvas.saveLayer(0, 0, canvas_w, canvas_h, null, Canvas.ALL_SAVE_FLAG);
            //设置画布背景颜色（红色）（如果不设置颜色，该图层为空，上层相交则也为空）
//          canvas.drawColor(Color.RED);
            //设置相交模式（取两个图层相交的部分，以目标图层颜色显示，也就是下层背景色（红色））
            canvas.drawPath(drawPath, drawPaint);
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
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measuredWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
        Log.d("genius", "onMeasure:" + getMeasuredWidth() + " x " + getMeasuredHeight());
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
                result = Math.min((int) canvas_w, specSize);
                break;
        }
        return result;
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
     * 手绘路径的bitmap
     *
     * @return
     */
    private Bitmap pathBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(1024, 1024, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        // 抗锯齿
        paint.setAntiAlias(true);
        // 防抖动
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(20);
        Path path = new Path();
        path.addRect(400, 400, 600, 600, Path.Direction.CW);
        canvas.drawPath(path, paint);
        return bitmap;
    }

    private void drawBezier(Canvas canvas) {
        drawStrokePaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mPath, drawStrokePaint);
    }

    /**
     * 绘制已绘过的区域
     *
     * @param canvas
     */
    private void drawStroke(Canvas canvas) {
        drawStrokePaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < currentIndex; i++) {
            canvas.drawPath(mPaths.get(i), drawStrokePaint);
        }
    }

    /**
     * 创建文字图层(其中的一个笔画)
     *
     * @return
     */
    private Bitmap characterBitmap() {
        Bitmap bitmap = Bitmap.createBitmap((int) canvas_w, (int) canvas_h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
//        canvas.drawColor(Color.WHITE);
        canvas.drawPath(mPaths.get(currentIndex), characterPaint);
        return bitmap;
    }

    /**
     * 计算两点之间的距离
     *
     * @param p1
     * @param p2
     */
    private double pointDistance(Point p1, Point p2) {
        int s1 = Math.abs(p1.x - p2.x);
        int s2 = Math.abs(p1.y - p2.y);
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

        float eventx = event.getX() / scaleTimes;
        float eventy = event.getY() / scaleTimes;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawDistance = 0;
                startX = eventx;
                startY = eventy;
                drawPath.reset();

                if (currentIndex < mPointDatas.size()) {
                    //判断起始点是否在给定的区域内
                    touchRect.set((int) startX - deviations, (int) startY - deviations, (int) startX + deviations, (int) startY + deviations);
                    Point startPoint = mPointDatas.get(currentIndex).get(0);
                    //获取笔画起始点的rect
                    matchRect = new Rect(startPoint.x - deviations, (int) canvas_h - startPoint.y - deviations, startPoint.x + deviations, (int)canvas_h - startPoint.y + deviations);

                    if (matchRect.intersect(touchRect)) {
                        Toast.makeText(mContext, "在合理范围内", Toast.LENGTH_LONG).show();
                        isLegalOp = true;
                        drawPath.moveTo(startPoint.x, canvas_h - startPoint.y);
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
                    Point p1 = new Point((int) startX, (int) startY);
                    Point p2 = new Point((int) currentX, (int) currentY);
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
                        Point endPoint = mPointDatas.get(currentIndex).get(mPointDatas.get(currentIndex).size() - 1);
                        touchRect.set((int) endX - deviations, (int) endY - deviations, (int) endX + deviations, (int) endY + deviations);
                        //获取笔画终点的rect
                        matchRect = new Rect(endPoint.x - deviations, (int)canvas_h - endPoint.y - deviations, endPoint.x + deviations, (int)canvas_h - endPoint.y + deviations);
                        //判断绘制的长度是否超过笔画的长度或者小于笔画的一半
                        if (drawDistance > mLengths.get(currentIndex) + deviations || drawDistance < (mLengths.get(currentIndex) - deviations)/2) {
                            isLegalOp = false;
                            drawPath.reset();
                        } else {
                            //判断结束点是否在给定的区域内
                            if (matchRect.intersect(touchRect)) {
                                isLegalOp = true;
                                if (currentIndex < mfontDatas.size()) {
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

    public String getChineseWordData() {
        return chineseWordData;
    }

    public void setChineseWordData(final String chineseWordData) {
        this.chineseWordData = chineseWordData;
    }

    public String getChineseWordStroke() {
        return chineseWordStroke;
    }

    public void setChineseWordStroke(final String chineseWordStroke) {
        this.chineseWordStroke = chineseWordStroke;
    }

    public boolean isCanTouch() {
        return isCanTouch;
    }

    public void setCanTouch(final boolean canTouch) {
        isCanTouch = canTouch;
    }
}
