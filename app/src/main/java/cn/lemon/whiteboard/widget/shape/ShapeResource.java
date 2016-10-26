package cn.lemon.whiteboard.widget.shape;

import java.io.Serializable;

/**
 * Created by linlongxin on 2016/10/26.
 */

public class ShapeResource implements Serializable{

    public int mType;
    public float mStartX;
    public float mStartY;
    public float mEndX;
    public float mEndY;

    public WritablePaint mPaint;

    //针对曲线：包含了一个WritablePaint
    public WritablePath mCurvePath;
}
