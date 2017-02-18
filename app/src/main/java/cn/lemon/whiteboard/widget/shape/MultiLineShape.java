package cn.lemon.whiteboard.widget.shape;

import android.graphics.Canvas;

import cn.alien95.util.Utils;

/**
 * Created by user on 2016/10/27.
 */

public class MultiLineShape extends DrawShape {

    private static WritablePath mPath = new WritablePath();

    private static float mNextStartX = -1f;
    private static float mNextStartY = -1f;

    public void touchDown(int startX, int startY){
        if(mNextStartX == -1f && mNextStartY == -1f){
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
    }

    public void touchUp(int endX,int endY){
        super.touchUp(endX,endY); //给他们赋值
        mPath.lineTo(endX,endY);
        mNextStartX = endX;
        mNextStartY = endY;
    }

    @Override
    public void draw(Canvas canvas) {
        if(mEndX == 0 && mEndY == 0){
            return;
        }
        if(mStartX != mEndX || mStartY != mEndY){
            canvas.drawLine(mStartX,mStartY,mEndX,mEndY,mPaint);
            Utils.Log("MultiLineShape start-x : " + mStartX + "  start-y : " + mStartY +
            "  end-x : " + mEndX + "  end-y : " + mEndY);
        }
    }

    public static void clear(){
        mNextStartX = -1f;
        mNextStartY = -1f;
    }

    public static void setNewStartPoint(float x,float y){
        mNextStartX = x;
        mNextStartY = y;
    }

    public static float getNextPointX(){
        return mNextStartX;
    }
    public static float getNextPointY(){
        return mNextStartY;
    }
}
