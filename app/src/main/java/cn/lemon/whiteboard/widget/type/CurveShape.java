package cn.lemon.whiteboard.widget.type;

import android.graphics.Canvas;
import android.graphics.Rect;

import cn.lemon.whiteboard.widget.BoardView;

//曲线
public class CurveShape extends DrawShape {

    private Rect mRect;
    private WritablePath mPath;

    private float mStartX;
    private float mStartY;

    public CurveShape(BoardView view) {
        super(view);
        mPath = new WritablePath();
        mRect = new Rect();
    }

    public void touchMove(int startX, int startY, int currentX, int currentY) {
        if (mStartX == 0 && mStartY == 0) {
            mStartX = startX;
            mStartY = startY;
            //设置曲线开始点
            mPath.moveTo(mStartX, mStartY);
        }

        int border = (int) mPaint.getStrokeWidth();
        mRect.set((int) mStartX - border, (int) mStartY - border,
                (int) mStartX + border, (int) mStartY + border);

        float mMiddleX = (currentX + mStartX) / 2;
        float mMiddleY = (currentY + mStartY) / 2;

        // 贝赛尔曲线
        mPath.quadTo(mStartX, mStartY, mMiddleX, mMiddleY);

        //重新计算Rect的范围
        mRect.union((int) mMiddleX - border,
                (int) mMiddleY - border,
                (int) mMiddleX + border,
                (int) mMiddleY + border);

        //绘制Rect的曲线
        mDrawView.invalidate(mRect);
        mStartX = currentX;
        mStartY = currentY;
    }

    //把曲线绘制到画布上
    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }
}