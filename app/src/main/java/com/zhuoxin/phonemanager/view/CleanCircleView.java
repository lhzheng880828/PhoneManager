package com.zhuoxin.phonemanager.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.zhuoxin.phonemanager.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by YoungHong on 2016/11/18.
 * 自定义Home界面的一键清理按钮
 *
 * @author YoungHong
 * @since 2016年11月18日
 */

public class CleanCircleView extends View {
    int width;
    int height;
    int currentAngle = 360;
    RectF oval;
    boolean isBack = true;
    boolean isRunning = false;

    public CleanCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * 设置饼状图扫描的角度
     *
     * @param targetAngle 需要画出的扇形角度
     * @param perAngle    每次递进的角度
     */
    public void setAngle(final int targetAngle, final int perAngle) {
        //如果动画没没运行，则执行该方法
        if (!isRunning) {
            isRunning = true;
            final Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (isBack) {
                        //返回动画，角度为0后设置isBack状态为false
                        if (currentAngle - perAngle > 0) {
                            currentAngle -= perAngle;
                        } else {
                            currentAngle = 0;
                            isBack = false;
                        }
                    } else {
                        //前进动画,角度达到targetAngle后，设置isBack为true，isRunning为false ,并终止tiemr,
                        if (currentAngle + perAngle < targetAngle) {
                            currentAngle += perAngle;
                        } else {
                            currentAngle = targetAngle;
                            isBack = true;
                            isRunning = false;
                            timer.cancel();
                        }

                    }
                    postInvalidate();

                }
            };
            //启动timerTask
            timer.schedule(timerTask, 40, 40);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        //以短边为基准
        if (width < height) {
            height = width;
        } else {
            width = height;
        }
        oval = new RectF(0, 0, width, height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        //设置画笔颜色为蓝色
        if (Build.VERSION.SDK_INT >= 23) {
            paint.setColor(getResources().getColor(R.color.bluePrimaryColor, null));
        } else {
            paint.setColor(Color.BLUE);
        }
        //画圆
        canvas.drawArc(oval, -90, currentAngle, true, paint);
    }


}
