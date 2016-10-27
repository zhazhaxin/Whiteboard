package cn.lemon.whiteboard.widget.shape;

import android.graphics.Canvas;

import cn.lemon.whiteboard.widget.BoardView;

/**
 * Created by user on 2016/10/27.
 */

public class MultiLineShape extends DrawShape {

    private static WritablePath mPath = new WritablePath();
    private float mEndX;
    private float mEndY;

    private static float mNextStartX;
    private static float mNextStartY;

    public MultiLineShape(BoardView boardView) {
        super(boardView);
    }

    public void touchDown(int startX, int startY){
        if(mNextStartX == 0 && mNextStartY == 0){
            mStartX = startX;
            mStartY = startY;
            mPath.moveTo(startX,startY);
        }else {
            mStartX = (int) mNextStartX;
            mStartY = (int) mNextStartY;
        }
    }


    @Override
    public void touchMove(int currentX, int currentY) {

        mEndX = currentX;
        mEndY = currentY;
        mDrawView.invalidate();
    }

    public void touchUp(int x,int y){
        mPath.lineTo(x,y);
        mNextStartX = x;
        mNextStartY = y;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawLine(mStartX,mStartY,mEndX,mEndY,mPaint);
    }
}
