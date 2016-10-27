package cn.lemon.whiteboard.widget.shape;

import android.graphics.Canvas;
import android.graphics.Rect;

import cn.alien95.util.Utils;

/**
 * Created by linlongxin on 2016/10/24.
 */

public class RectShape extends DrawShape {

    private final String TAG = "RectShape";
    private int mConstantPointX;
    private int mConstantPointY;

    public RectShape() {
    }

    @Override
    public void touchDown(int startX, int startY) {
        super.touchDown(startX, startY);
        mConstantPointX = startX;
        mConstantPointY = startY;
    }

    @Override
    public void touchMove(int currentX, int currentY) {
        //mStartX,mStartY,mEndX,mEndY 代表矩形两个点
        //向右上方拖动
        if (mConstantPointX <= currentX && mConstantPointY >= currentY) {
            mStartY = currentY;
            mEndX = currentX;
        }
        //左下方拖动
        else if (mConstantPointX >= currentX && mConstantPointY <= currentY) {
            mStartX = currentX;
            mEndY = currentY;
        }
        //左上方拖动
        else if (mConstantPointX >= currentX && mConstantPointY >= currentY) {
            mStartX = currentX;
            mStartY = currentY;
            mEndX = mConstantPointX;
            mEndY = mConstantPointY;
        }
        //右下方拖动
        else if (mConstantPointX <= currentX && mConstantPointY <= currentY) {
            mEndX = currentX;
            mEndY = currentY;
        }

        Utils.Log("Rectangle startX : " + mStartX + " startY : "
                + mStartY + " endX : " + mEndX + " endY : " + mEndY);
    }

    @Override
    public void draw(Canvas canvas) {
        if (mStartX == 0 && mStartY == 0) {
            return;
        }
        Rect rect = new Rect(mStartX, mStartY, mEndX, mEndY);
        canvas.drawRect(rect, mPaint);
    }
}
