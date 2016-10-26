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
    protected WritablePaint mPaint;
    private boolean isEraser = false; //是否是橡皮

    private static int mPaintColor = Color.BLACK;
    private static float mPaintWidth = 4f;

    public DrawShape(BoardView boardView) {
        //注入BoardView对象
        mDrawView = boardView;
        mPaint = new WritablePaint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mPaintColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mPaintWidth);
        mPaint.setDither(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public abstract void touchMove(int startX, int startY, int currentX, int currentY);

    public abstract void draw(Canvas canvas);

    public void setPaintColor(int color) {
        if(isEraser){
            return;
        }
        mPaintColor = color;
        mPaint.setColor(color);

    }

    public int getPaintColor() {
        return mPaintColor;
    }

    public void setPaintWidth(float width) {
        if(isEraser){
            return;
        }
        mPaintWidth = width;
        mPaint.setStrokeWidth(width);
    }

    public static float getPaintWidth() {
        return mPaintWidth;
    }

    public void setEraser(boolean isEraser){
        this.isEraser = isEraser;
    }
}
