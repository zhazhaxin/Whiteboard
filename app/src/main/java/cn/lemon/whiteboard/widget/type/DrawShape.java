package cn.lemon.whiteboard.widget.type;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import cn.lemon.whiteboard.widget.BoardView;

/**
 * Created by linlongxin on 2016/10/24.
 */

public abstract class DrawShape {

    protected BoardView mDrawView;
    protected Paint mPaint;
    protected static int mPaintColor = Color.BLACK;
    protected static float mPaintWidth = 4f;

    public DrawShape(BoardView boardView) {
        //注入BoardView对象
        mDrawView = boardView;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mPaintColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mPaintWidth);
        mPaint.setDither(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setPaintColor(int color) {
        mPaintColor = color;
        mPaint.setColor(color);
    }

    public int getPaintColor(){
        return mPaintColor;
    }

    public void setPaintWidth(float width) {
        mPaintWidth = width;
        mPaint.setStrokeWidth(width);
    }

    public abstract void touchMove(int startX, int startY, int currentX, int currentY);

    public abstract void draw(Canvas canvas);

}
