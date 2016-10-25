package cn.lemon.whiteboard.widget.type;

import android.graphics.Path;

import java.io.Serializable;
import java.util.ArrayList;

public class WritablePath extends Path implements Serializable
{
    private static final long serialVersionUID = 1L;
    private ArrayList<float[]> pathPoints;

    public WritablePath()
    {
        super();
        pathPoints = new ArrayList<>();
    }

    public WritablePath(WritablePath path)
    {
        super(path);
        pathPoints = path.pathPoints;
    }

    public void addPathPoints(float[] points)
    {
        pathPoints.add(points);
    }

    public void loadPathPointsAsQuadTo()
    {
        float[] initPoints = pathPoints.remove(0);
        moveTo(initPoints[0], initPoints[1]);

        for (float[] pointSet : pathPoints)
        {
            quadTo(pointSet[0], pointSet[1], pointSet[2], pointSet[3]);
        }
    }
}