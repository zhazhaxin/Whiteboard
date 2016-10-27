package cn.lemon.whiteboard.widget.shape;

import android.graphics.Canvas;

import cn.lemon.whiteboard.widget.BoardView;

/**
 * Created by linlongxin on 2016/10/24.
 */

public class LineShape extends DrawShape {

    public int mEndX;
    public int mEndY;

    public LineShape(BoardView boardView) {
        super(boardView);
    }

    @Override
    public void touchMove( int currentX, int currentY) {
        mEndX = currentX;
        mEndY = currentY;
        mDrawView.invalidate();
    }

    //ACTION_MOVE更新画布，调用这里
    @Override
    public void draw(Canvas canvas) {
        canvas.drawLine(mStartX, mStartY, mEndX, mEndY, mPaint);
    }
}
