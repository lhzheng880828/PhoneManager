package com.zhuoxin.phonemanager.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.zhuoxin.phonemanager.R;

/**
 * Created by YoungHong on 2016/11/13.
 */

public class PiechartView extends View {
    int proportion = 35;
    int backgroundColor = Color.DKGRAY;
    int color = Color.BLUE;

    public PiechartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //从attr中获取自定义属性
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PiechartView, 0, 0);
        //初始化自定义属性
        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attrName = typedArray.getIndex(i);
            switch (attrName) {
                case R.styleable.PiechartView_piechartProportion:
                    proportion = typedArray.getInt(attrName, 0);
                    break;
                case R.styleable.PiechartView_piechartBackgroundColor:
                    backgroundColor = typedArray.getColor(attrName, Color.DKGRAY);
                    break;
                case R.styleable.PiechartView_piechartColor:
                    color = typedArray.getColor(attrName, Color.BLUE);
                    break;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (width < height) {
            height = width;
        } else {
            width = height;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(backgroundColor);
        RectF rectF = new RectF(0, 0, getWidth(), getHeight());
        canvas.drawArc(rectF, -90, 360, true, paint);
        paint.setColor(color);
        canvas.drawArc(rectF, -90, proportion, true, paint);
    }
}
