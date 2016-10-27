package cn.lemon.whiteboard.widget.shape;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import cn.alien95.util.Utils;
import cn.lemon.whiteboard.widget.BoardView;

/**
 * Created by linlongxin on 2016/10/24.
 */

public abstract class DrawShape {

    protected BoardView mDrawView;
    protected WritablePaint mPaint;
    protected int mStartX;
    protected int mStartY;

    public static int mPaintColor = Color.BLACK;
    public static float mPaintWidth = 5f;

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

    public void touchDown(int startX,int startY){
        mStartX = startX;
        mStartY = startY;
        Utils.Log("start-x : " + mStartX + "  start-y : " + mStartY);
    }

    public abstract void touchMove(int currentX, int currentY);

    public abstract void draw(Canvas canvas);

    public WritablePaint getPaint(){
        mPaint.mColor = mPaintColor;
        mPaint.mWidth = mPaintWidth;
        return mPaint;
    }

    public int getStartX(){
        return mStartX;
    }
    public int getStartY(){
        return mStartY;
    }
}
