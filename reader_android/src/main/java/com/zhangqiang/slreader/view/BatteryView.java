package com.zhangqiang.slreader.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

public class BatteryView extends View {

    private int mBatteryBodyHeight = 40;
    private int mBatteryBodyWidth = 60;
    private int mBatteryBodyRadius = 5;
    private float mBatteryBodyBorderWidth = 4;
    private int mBatteryHeadHeight = 20;
    private int mBatteryHeadWidth = 10;
    private int mBatteryHeadRadius = 3;
    private float mBatteryLevel = 0f;
    private final Paint mPaint;
    private int mBatteryColor;
    private final Path mBatteryHeadPath;
    private final RectF mBatteryHeadRectF = new RectF();
    private final float[] mBatterHeadRadii = new float[8];
    private final RectF mTempRectF = new RectF();

    public BatteryView(Context context) {
        super(context);
        mPaint = new Paint();
        mBatteryHeadPath = new Path();
        updateBatteryHeadRadii();
    }

    private void updateBatteryHeadRadii() {
        mBatterHeadRadii[0] = mBatteryHeadRadius;
        mBatterHeadRadii[1] = mBatteryHeadRadius;
        mBatterHeadRadii[2] = 0;
        mBatterHeadRadii[3] = 0;
        mBatterHeadRadii[4] = 0;
        mBatterHeadRadii[5] = 0;
        mBatterHeadRadii[6] = mBatteryHeadRadius;
        mBatterHeadRadii[7] = mBatteryHeadRadius;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.reset();
        mPaint.setColor(mBatteryColor);
        int verticalSize = getHeight() - getPaddingTop() - getPaddingBottom();
        int batteryHeadTop = (verticalSize - mBatteryHeadHeight) / 2 + getPaddingTop();
        int batteryHeadLeft = getPaddingLeft();
        int batteryHeadRight = batteryHeadLeft + mBatteryHeadWidth;
        int batteryHeadBottom = batteryHeadTop + mBatteryHeadHeight;
        mBatteryHeadRectF.set(batteryHeadLeft, batteryHeadTop, batteryHeadRight, batteryHeadBottom);
        mBatteryHeadPath.reset();
        mBatteryHeadPath.addRoundRect(mBatteryHeadRectF, mBatterHeadRadii, Path.Direction.CW);
        canvas.drawPath(mBatteryHeadPath,mPaint);

        float halfBorder = mBatteryBodyBorderWidth / 2;
        float batteryBodyLeft = batteryHeadRight + halfBorder;
        float batteryBodyTop = (float) (verticalSize - mBatteryBodyHeight) / 2 + getPaddingTop() + halfBorder;
        float batteryBodyRight = batteryBodyLeft + mBatteryBodyWidth - mBatteryBodyBorderWidth;
        float batteryBodyBottom = batteryBodyTop + mBatteryBodyHeight - mBatteryBodyBorderWidth;
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBatteryBodyBorderWidth);
        mTempRectF.set(batteryBodyLeft, batteryBodyTop, batteryBodyRight, batteryBodyBottom);
        canvas.drawRoundRect(mTempRectF, mBatteryBodyRadius, mBatteryBodyRadius, mPaint);


        int batteryLevelWidth = (int) ((mBatteryBodyWidth - mBatteryBodyBorderWidth * 2) * mBatteryLevel);
        float batteryLevelLeft = batteryBodyRight - halfBorder - batteryLevelWidth;
        float batteryLevelTop = batteryBodyTop + halfBorder;
        float batteryLevelRight = batteryLevelLeft + batteryLevelWidth;
        float batteryLevelBottom = batteryBodyBottom - halfBorder;
        mPaint.setStyle(Paint.Style.FILL);
        mTempRectF.set(batteryLevelLeft, batteryLevelTop, batteryLevelRight, batteryLevelBottom);
        canvas.drawRoundRect(mTempRectF, 0, 0, mPaint);
    }

    @Override
    protected void onMeasure(int widthOptions, int heightOptions) {
        super.onMeasure(widthOptions, heightOptions);
        int maxWidth = mBatteryHeadWidth + mBatteryBodyWidth + getPaddingTop() + getPaddingRight();
        int maxHeight = Math.max(mBatteryHeadHeight, mBatteryBodyHeight) + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(resolveSize(maxWidth, widthOptions), resolveSize(maxHeight, heightOptions));
    }

    public void setBatteryBodyHeight(int batteryBodyHeight) {
        if (mBatteryBodyHeight != batteryBodyHeight) {
            this.mBatteryBodyHeight = batteryBodyHeight;
            requestLayout();
        }
    }

    public void setBatteryBodyWidth(int batteryBodyWidth) {
        if (mBatteryBodyWidth != batteryBodyWidth) {
            this.mBatteryBodyWidth = batteryBodyWidth;
            requestLayout();
        }
    }

    public void setBatteryBodyRadius(int batteryBodyRadius) {
        if (mBatteryBodyRadius != batteryBodyRadius) {
            this.mBatteryBodyRadius = batteryBodyRadius;
            invalidate();
        }
    }

    public void setBatteryBodyBorderWidth(float batteryBodyBorderWidth) {
        if (mBatteryBodyBorderWidth != batteryBodyBorderWidth) {
            this.mBatteryBodyBorderWidth = batteryBodyBorderWidth;
            invalidate();
        }
    }

    public void setBatteryHeadHeight(int batteryHeadHeight) {
        if (mBatteryHeadHeight != batteryHeadHeight) {
            this.mBatteryHeadHeight = batteryHeadHeight;
            requestLayout();
        }
    }

    public void setBatteryHeadWidth(int batteryHeadWidth) {
        if (mBatteryHeadWidth != batteryHeadWidth) {
            this.mBatteryHeadWidth = batteryHeadWidth;
            requestLayout();
        }
    }

    public void setBatteryHeadRadius(int batteryHeadRadius) {
        if (mBatteryHeadRadius != batteryHeadRadius) {
            this.mBatteryHeadRadius = batteryHeadRadius;
            updateBatteryHeadRadii();
            invalidate();
        }
    }

    public void setBatteryLevel(float batteryLevel) {
        if (mBatteryLevel != batteryLevel) {
            this.mBatteryLevel = batteryLevel;
            invalidate();
        }
    }

    public void setBatteryColor(int batteryColor) {
        if (mBatteryColor != batteryColor) {
            this.mBatteryColor = batteryColor;
            invalidate();
        }
    }
}
