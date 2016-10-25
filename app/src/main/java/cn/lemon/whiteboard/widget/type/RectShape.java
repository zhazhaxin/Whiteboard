package cn.lemon.whiteboard.widget.type;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import cn.lemon.whiteboard.widget.BoardView;

/**
 * Created by linlongxin on 2016/10/24.
 */

public class RectShape extends DrawShape {

    private final String TAG = "RectShape";
    protected int mLeft;
    protected int mTop;
    protected int mRight;
    protected int mBottom;
    private int mConstantPointX;
    private int mConstantPointY;
    private Rect mRect;

    public RectShape(BoardView boardView) {
        super(boardView);
        mRect = new Rect();
    }

    @Override
    public void touchMove(int startX, int startY, int currentX, int currentY) {
        //开始向右下方画
        if (mTop == 0 && mLeft == 0) {
            mLeft = startX;
            mTop = startY;
            mConstantPointX = mLeft;
            mConstantPointY = mTop;
        } else {

            //向右上方拖动
            if (mTop >= currentY && currentX >= mLeft) {
                mTop = currentY;
                mRight = currentX;
                mBottom = mConstantPointY;
            }
            //左下方拖动
            else if (mLeft >= currentX && currentY >= mTop) {
                mLeft = currentX;
                mBottom = currentY;
                mRight = mConstantPointX;
            }
            //左上方拖动
            else if (currentX <= mLeft && currentY <= mTop) {
                mLeft = currentX;
                mTop = currentY;
                mRight = mConstantPointX;
                mBottom = mConstantPointY;
            }
            //右下方拖动
            else {
                mRight = currentX;
                mBottom = currentY;

            }
        }

        mRect = new Rect(mLeft, mTop, mRight, mBottom);
        Log.i(TAG, "left : " + mLeft + " top : " + mTop + " right : " + mRight + " bottom : " + mBottom);
        mDrawView.invalidate(mRect); //调用下面draw()方法
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(mRect, mPaint);
    }
}
