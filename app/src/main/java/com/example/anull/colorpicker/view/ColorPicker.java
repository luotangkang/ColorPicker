package com.example.anull.colorpicker.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.anull.colorpicker.R;

/**
 * 旋转取色圆环
 * Created by null on 2016/8/31.
 */

public class ColorPicker extends AppCompatImageView implements View.OnTouchListener {

    private Bitmap bp;//色轮图片
    private int bw, bh;//色轮图片的尺寸
    private float x, y, radio;
    private OnColorSelectListener onColorSelectListener;
    private float oldD;
    private float newD;
    private Matrix matrix;
    private Bitmap bitmap;
    private int bp_width;

    public ColorPicker(Context context) {
        this(context, null);
    }

    public ColorPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        matrix = new Matrix();
        int i = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        int j = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        measure(i, j);
        setOnTouchListener(this);
        setClickable(true);
        bp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.color_circle);
        bp_width = bp.getWidth();
        bw = bp.getWidth();
        bh = bp.getHeight();
        setImageBitmap(bp);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float xx = event.getX();
        float yy = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!inCircle(xx, yy)) {
                    return false;
                }
                oldD = (float) Math.atan2(xx - getWidth() / 2, yy - getHeight() / 2);
                invalidate();
                if (onColorSelectListener != null && bp != null) {
                    onColorSelectListener.onStartSelect(bp.getPixel(getWidth() / 2, getHeight() / 4));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!inCircle(xx, yy)) {
                    return false;
                }
                newD = (float) Math.atan2(xx - getWidth() / 2, yy - getHeight() / 2);
                matrix.postRotate((float) Math.toDegrees(oldD - newD), getWidth() / 2, getHeight() / 2);
                bitmap = Bitmap.createBitmap(bp, 0, 0, bw, bh, matrix, true);
                setImageBitmap(bitmap);
                oldD = newD;

                invalidate();
                if (onColorSelectListener != null && bitmap != null) {
                    onColorSelectListener.onColorSelect(bitmap.getPixel(getWidth() / 2, getHeight() / 4));
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!inCircle(xx, yy)) {
                    return false;
                }

                invalidate();
                if (onColorSelectListener != null && bitmap != null) {
                    onColorSelectListener.onStopSelect(bitmap.getPixel(getWidth() / 2, getHeight() / 4));
                }
                break;
        }
        x = xx;
        y = yy;
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        x = getWidth() / 2;
        y = getHeight() / 2;
        radio = x > y ? y : x;

        invalidate();
    }

    private boolean inCircle(float x, float y) {
        float cx = getWidth() / 2;
        float cy = getHeight() / 2;
        float d = (float) Math.abs(Math.sqrt((x - cx) * (x - cx) + (y - cy) * (y - cy)));
        return d <= (radio);
    }

    public interface OnColorSelectListener {
        void onStartSelect(int color);

        void onColorSelect(int color);

        void onStopSelect(int color);
    }

    public OnColorSelectListener getOnColorSelectListener() {
        return onColorSelectListener;
    }

    public void setOnColorSelectListener(OnColorSelectListener onColorSelectListener) {
        this.onColorSelectListener = onColorSelectListener;
    }

    /**
     * 回收Bitmap内存
     */
    public void recycle() {
        if (bp != null) {
            if (!bp.isRecycled()) {
                bp.recycle();
            }
            bp = null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap pointer = BitmapFactory.decodeResource(getResources(), R.mipmap.pointer);
        int measuredWidth = this.getMeasuredWidth();
        int measuredHeight = this.getMeasuredHeight();
        int width = pointer.getWidth();
        canvas.drawBitmap(pointer, (measuredWidth - width) / 2
                , (measuredHeight - bp_width) / 2, new Paint());
    }

    public boolean isRecycled() {
        return bp == null || bp.isRecycled();
    }
}
