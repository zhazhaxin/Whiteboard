package cn.lemon.whiteboard.type;

import android.graphics.Canvas;

import cn.lemon.whiteboard.BoardView;

/**
 * Created by linlongxin on 2016/10/21.
 */

public class LineShape extends DrawShape {

    private int mStartX;
    private int mStartY;
    private int mEndX;
    private int mEndY;

    public LineShape(BoardView boardView) {
        super(boardView);
    }

    @Override
    public void touchMove(int startX, int startY, int currentX, int currentY) {
        if (mStartX == 0 && mStartY == 0) {
            mStartX = startX;
            mStartY = startY;
        }
        mEndX = currentX;
        mEndY = currentY;
        mDrawView.invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawLine(mStartX, mStartY, mEndX, mEndY, mPaint);
    }
}
