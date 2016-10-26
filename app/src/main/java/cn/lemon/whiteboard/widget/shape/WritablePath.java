package cn.lemon.whiteboard.widget.shape;

import android.graphics.Path;

import java.io.Serializable;
import java.util.ArrayList;

public class WritablePath extends Path implements Serializable {

    private static final long serialVersionUID = 1L;

    public WritablePaint mPaint;

    private ArrayList<float[]> mPathPoints;

    public WritablePath() {
        super();
        mPathPoints = new ArrayList<>();
    }

    public void addPathPoints(float[] points) {
        mPathPoints.add(points);
    }

    public void loadPathPointsAsQuadTo() {
        float[] initPoints = mPathPoints.remove(0);
        moveTo(initPoints[0], initPoints[1]);
        for (float[] points : mPathPoints) {
            quadTo(points[0], points[1], points[2], points[3]);
        }
    }
}