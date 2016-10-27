package cn.lemon.whiteboard.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by user on 2016/10/27.
 */

public class ColorView extends View {

    private int mWidth;
    private int mHeight;
    private Paint mCirclePaint;
    private Paint mLinePaint;
    private boolean isChecked = false;

    public ColorView(Context context) {
        this(context, null);
    }

    public ColorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);

        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setStrokeWidth(8f);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setDither(true);
        mLinePaint.setStrokeJoin(Paint.Join.ROUND);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = mWidth;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x = mWidth / 2;
        float y = mHeight / 2;
        float r = mWidth / 2;
        canvas.drawCircle(x, y, r, mCirclePaint);

        if (isChecked) {
            Path path = new Path();
            int startX;
            int startY;
            startX = mWidth / 5;
            startY = mHeight / 7 * 4;
            path.moveTo(startX, startY);
            //先只考虑width == height;后期完善
            int midX = mWidth / 5 * 2;
            int midY = mHeight / 4 * 3;
            path.lineTo(midX, midY);
            int endX = mWidth / 5 * 4;
            int endY = mHeight / 3;
            path.lineTo(endX, endY);
            canvas.drawPath(path, mLinePaint);
        }
    }

    public void setColor(int color, boolean isChecked) {
        mCirclePaint.setColor(color);
        this.isChecked = isChecked;
        invalidate();
    }

    public void setChecked(boolean isChecked) {
        if (this.isChecked != isChecked) {
            this.isChecked = isChecked;
            invalidate();
        }
    }

}
