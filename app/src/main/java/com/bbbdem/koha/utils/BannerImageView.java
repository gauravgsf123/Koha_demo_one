package com.bbbdem.koha.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import com.bbbdem.koha.R;

public class BannerImageView extends androidx.appcompat.widget.AppCompatImageView {

    private Paint mRibbonPaint;
    private Paint mTextPaint;
    private Paint mBoxPaint;
    private float mScale;
    private String mBannerText;

    public BannerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPainters(attrs);
    }

    public BannerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPainters(attrs);
    }

    private void initPainters(AttributeSet attrs) {
        TypedArray attributes = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.BannerImageView, 0, 0);
        mBannerText = attributes.getString(R.styleable.BannerImageView_label);
        mBoxPaint = new Paint();
        int white = ContextCompat.getColor(getContext(), R.color.green);
        mBoxPaint.setColor(white);
        mBoxPaint.setAlpha(156);
        mRibbonPaint = new Paint();
        mRibbonPaint.setColor(ContextCompat.getColor(getContext(), R.color.button_color));
        mRibbonPaint.setAntiAlias(true);
        mRibbonPaint.setStyle(Paint.Style.STROKE);
        mRibbonPaint.setStrokeCap(Paint.Cap.BUTT);
        mScale = getResources().getDisplayMetrics().density;
        mRibbonPaint.setStrokeWidth(dp(16));
        mTextPaint = new Paint();
        mTextPaint.setColor(white);
        mTextPaint.setTextSize(dp(12));
    }

    /**
     * Converts dp to px
     *
     * @param dp
     * @return
     */
    private float dp(float dp) {
        return dp * mScale + 0.5f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(mBannerText)) {
            canvas.drawRect(0, 0, getRight(), getBottom(), mBoxPaint);
            canvas.drawLine(-dp(16), dp(64), dp(64), -dp(16), mRibbonPaint);
            canvas.save();
            canvas.rotate(-45, 0, 0);
            canvas.drawText(mBannerText, -dp(24), dp(38), mTextPaint);
            canvas.restore();
        }
    }

}
