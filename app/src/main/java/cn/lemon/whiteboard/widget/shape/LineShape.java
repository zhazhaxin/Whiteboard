package cn.lemon.whiteboard.widget.shape;

import android.graphics.Canvas;

/**
 * Created by linlongxin on 2016/10/24.
 */

public class LineShape extends DrawShape {

    @Override
    public void touchMove( int currentX, int currentY) {
        mEndX = currentX;
        mEndY = currentY;
    }

    //ACTION_MOVE更新画布，调用这里
    @Override
    public void draw(Canvas canvas) {
        if(mEndX == 0 && mEndY == 0){
            return;
        }
        canvas.drawLine(mStartX, mStartY, mEndX, mEndY, mPaint);
    }
}
