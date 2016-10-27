package cn.lemon.whiteboard.widget.shape;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import cn.lemon.whiteboard.widget.BoardView;

/**
 * Created by linlongxin on 2016/10/24.
 */

public class RectShape extends DrawShape {

    private final String TAG = "RectShape";
    public int mEndX;
    public int mEndY;
    private int mConstantPointX;
    private int mConstantPointY;
    private Rect mRect;

    public RectShape(BoardView boardView) {
        super(boardView);
        mRect = new Rect();
    }

    @Override
    public void touchDown(int startX, int startY) {
        super.touchDown(startX, startY);
        mConstantPointX = startX;
        mConstantPointY = startY;
    }

    @Override
    public void touchMove(int currentX, int currentY) {
        

            //向右上方拖动
            if (mStartY >= currentY && currentX >= mStartX) {
                mStartY = currentY;
                mEndX = currentX;
                mEndY = mConstantPointY;
            }
            //左下方拖动
            else if (mStartX >= currentX && currentY >= mStartY) {
                mStartX = currentX;
                mEndY = currentY;
                mEndX = mConstantPointX;
            }
            //左上方拖动
            else if (currentX <= mStartX && currentY <= mStartY) {
                mStartX = currentX;
                mStartY = currentY;
                mEndX = mConstantPointX;
                mEndY = mConstantPointY;
            }
            //右下方拖动
            else {
                mEndX = currentX;
                mEndY = currentY;

            }

        mRect = new Rect(mStartX, mStartY, mEndX, mEndY);
        Log.i(TAG, "left : " + mStartX + " top : " + mStartY + " right : " + mEndX + " bottom : " + mEndY);
        mDrawView.invalidate(mRect); //调用下面draw()方法
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(mRect, mPaint);
    }
}
