package cn.lemon.whiteboard.widget.shape;

import android.graphics.Canvas;
import android.graphics.Rect;

import cn.lemon.whiteboard.widget.BoardView;

/**
 * Created by linlongxin on 2016/10/24.
 */
//曲线
public class CurveShape extends DrawShape {

    private Rect mRect;
    protected WritablePath mPath;

    public CurveShape() {
        mPath = new WritablePath();
        mRect = new Rect();
    }

    @Override
    public void touchDown(int startX, int startY) {
        super.touchDown(startX, startY);
        //设置曲线开始点
        mPath.moveTo(startX, startY);
        float[] a = {startX, startY, 0, 0};
        mPath.addPathPoints(a);
    }

    public void touchMove(int currentX, int currentY) {

        int border = (int) mPaint.getStrokeWidth();
        mRect.set( mStartX - border,  mStartY - border,
                 mStartX + border,  mStartY + border);

        float mMiddleX = (currentX + mStartX) / 2;
        float mMiddleY = (currentY + mStartY) / 2;

        // 贝赛尔曲线
        mPath.quadTo(mStartX, mStartY, mMiddleX, mMiddleY);

        float[] temp = {mStartX, mStartY, mMiddleX, mMiddleY};
        mPath.addPathPoints(temp);

        //重新计算Rect的范围
        mRect.union((int) mMiddleX - border,
                (int) mMiddleY - border,
                (int) mMiddleX + border,
                (int) mMiddleY + border);

        mStartX = currentX;
        mStartY = currentY;
    }

    //把曲线绘制到画布上
    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }

    public WritablePath getPath(){
        mPaint.mColor = mPaintColor;
        mPaint.mWidth = mPaintWidth;
        mPath.mPaint = mPaint;
        return mPath;
    }

}