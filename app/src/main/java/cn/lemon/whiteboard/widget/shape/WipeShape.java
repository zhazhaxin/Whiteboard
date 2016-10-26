package cn.lemon.whiteboard.widget.shape;

import android.graphics.Color;

import cn.lemon.whiteboard.widget.BoardView;

/**
 * Created by user on 2016/10/26.
 */

public class WipeShape extends CurveShape {

    public WipeShape(BoardView view) {
        super(view);

        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(100);
        setEraser(true);
    }

    public WritablePath getPath(){
        mPaint.mColor = Color.WHITE;
        mPaint.mWidth = 100;
        mPath.mPaint = mPaint;
        return mPath;
    }
}
