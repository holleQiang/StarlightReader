package com.zhangqiang.sl.reader.view;

import com.zhangqiang.sl.framework.context.SLContext;
import com.zhangqiang.sl.framework.graphic.SLCanvas;
import com.zhangqiang.sl.framework.graphic.SLPaint;
import com.zhangqiang.sl.framework.graphic.SLPath;
import com.zhangqiang.sl.framework.graphic.SLRectF;
import com.zhangqiang.sl.framework.view.SLView;

public class BatteryView extends SLView {

    private int mBatteryBodyHeight = 40;
    private int mBatteryBodyWidth = 60;
    private int mBatteryBodyRadius = 5;
    private float mBatteryBodyBorderWidth = 4;
    private int mBatteryHeadHeight = 20;
    private int mBatteryHeadWidth = 10;
    private int mBatteryHeadRadius = 3;
    private float mBatteryLevel = 0f;
    private final SLPaint mPaint;
    private int mBatteryColor;
    private final SLPath mBatteryHeadPath;
    private SLRectF mBatteryHeadRectF = new SLRectF();
    private float[] mBatterHeadRadii = new float[8];

    public BatteryView(SLContext context) {
        super(context);
        mPaint = context.newPaint();
        mBatteryHeadPath = context.newPath();
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
    protected void onDraw(SLCanvas canvas) {
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
        mBatteryHeadPath.addRoundRect(mBatteryHeadRectF, mBatterHeadRadii, SLPath.Direction.CW);
        canvas.drawPath(mBatteryHeadPath,mPaint);

        float halfBorder = mBatteryBodyBorderWidth / 2;
        float batteryBodyLeft = batteryHeadRight + halfBorder;
        float batteryBodyTop = (float) (verticalSize - mBatteryBodyHeight) / 2 + getPaddingTop() + halfBorder;
        float batteryBodyRight = batteryBodyLeft + mBatteryBodyWidth - mBatteryBodyBorderWidth;
        float batteryBodyBottom = batteryBodyTop + mBatteryBodyHeight - mBatteryBodyBorderWidth;
        mPaint.setStyle(SLPaint.Style.STROKE);
        mPaint.setStrokeWidth(mBatteryBodyBorderWidth);
        canvas.drawRoundRect(batteryBodyLeft, batteryBodyTop, batteryBodyRight, batteryBodyBottom, mBatteryBodyRadius, mBatteryBodyRadius, mPaint);

        int batteryLevelWidth = (int) ((mBatteryBodyWidth - mBatteryBodyBorderWidth * 2) * mBatteryLevel);
        float batteryLevelLeft = batteryBodyRight - halfBorder - batteryLevelWidth;
        float batteryLevelTop = batteryBodyTop + halfBorder;
        float batteryLevelRight = batteryLevelLeft + batteryLevelWidth;
        float batteryLevelBottom = batteryBodyBottom - halfBorder;
        mPaint.setStyle(SLPaint.Style.FILL);
        canvas.drawRoundRect(batteryLevelLeft, batteryLevelTop, batteryLevelRight, batteryLevelBottom, 0, 0, mPaint);
    }

    @Override
    protected void onMeasure(int widthOptions, int heightOptions) {
        super.onMeasure(widthOptions, heightOptions);
        int maxWidth = mBatteryHeadWidth + mBatteryBodyWidth + getPaddingTop() + getPaddingRight();
        int maxHeight = Math.max(mBatteryHeadHeight, mBatteryBodyHeight) + getPaddingTop() + getPaddingBottom();
        setMeasuredResult(resolveSizeAndState(maxWidth, widthOptions), resolveSizeAndState(maxHeight, heightOptions));
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
