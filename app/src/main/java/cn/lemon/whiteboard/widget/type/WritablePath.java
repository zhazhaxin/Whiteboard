package cn.lemon.whiteboard.widget.type;

import android.graphics.Path;

import java.io.Serializable;

public class WritablePath extends Path implements Serializable
{
    public WritablePaint mPaint;

    public WritablePath()
    {
        super();
    }

    public WritablePath(WritablePaint mPaint) {
        super();
        this.mPaint = mPaint;
    }
}