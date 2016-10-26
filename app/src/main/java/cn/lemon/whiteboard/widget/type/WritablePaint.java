package cn.lemon.whiteboard.widget.type;

import android.graphics.Paint;

import java.io.Serializable;

/**
 * Created by user on 2016/10/25.
 */

public class WritablePaint extends Paint implements Serializable {

    public int mColor;
    public float mWidth;

    public void loadPaint(){
        setAntiAlias(true);
        setColor(mColor);
        setStyle(Paint.Style.STROKE);
        setStrokeWidth(mWidth);
        setDither(true);
        setStrokeJoin(Paint.Join.ROUND);
        setStrokeCap(Paint.Cap.ROUND);
    }

}
