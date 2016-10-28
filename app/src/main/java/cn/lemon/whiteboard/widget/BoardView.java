package cn.lemon.whiteboard.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.alien95.util.Utils;
import cn.lemon.whiteboard.widget.shape.CurveShape;
import cn.lemon.whiteboard.widget.shape.DrawShape;
import cn.lemon.whiteboard.widget.shape.LineShape;
import cn.lemon.whiteboard.widget.shape.MultiLineShape;
import cn.lemon.whiteboard.widget.shape.OvalShape;
import cn.lemon.whiteboard.widget.shape.RectShape;
import cn.lemon.whiteboard.widget.shape.ShapeResource;
import cn.lemon.whiteboard.widget.shape.Type;
import cn.lemon.whiteboard.widget.shape.WipeShape;

/**
 * Created by linlongxin on 2016/10/24.
 */
public class BoardView extends View {

    private final String TAG = "BoardView";
    //类型：默认曲线
    private int mDrawType = Type.CURVE;
    private Bitmap mDrawBitmap;
    private Canvas mCanvas;
    private Paint mPaint;  //渲染画布
    private DrawShape mShape;

    private boolean isClearScreen = false;
    private boolean isRecentRecallOrUndo = false;

    private ArrayList<ShapeResource> mSavePath;
    private ArrayList<ShapeResource> mDeletePath;

    private OnDownAction mDownAction;

    public BoardView(Context context) {
        this(context, null);
    }

    public BoardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.DITHER_FLAG);
        mSavePath = new ArrayList<>();
        mDeletePath = new ArrayList<>();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        mDrawBitmap = createBitmap(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas绘制到mDrawBitmap
        canvas.drawBitmap(mDrawBitmap, 0, 0, mPaint);
        //绘制path到canvas
        if (mShape != null && !isClearScreen) {
            mShape.draw(canvas);
        } else if (isClearScreen) {
            isClearScreen = false;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int currentX = (int) event.getX();
        int currentY = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mDownAction != null) {
                    mDownAction.dealDownAction();
                }
                int mStartX = (int) event.getX();
                int mStartY = (int) event.getY();
                //曲线
                switch (mDrawType) {
                    case Type.CURVE:
                        mShape = new CurveShape();
                        break;
                    case Type.WIPE:
                        mShape = new WipeShape();
                        break;
                    case Type.RECTANGLE:
                        mShape = new RectShape();
                        break;
                    case Type.OVAL:
                        mShape = new OvalShape();
                        break;
                    case Type.LINE:
                        mShape = new LineShape();
                        break;
                    case Type.MULTI_LINE:
                        mShape = new MultiLineShape();
                        break;
                }
                mShape.touchDown(mStartX, mStartY);
                return true;

            case MotionEvent.ACTION_MOVE:
                mShape.touchMove(currentX, currentY);
                invalidate();
                return true;

            case MotionEvent.ACTION_UP:
                mShape.touchUp(currentX,currentY);
                //把之前的path保存绘制到mDrawBitmap上
                ShapeResource resource = new ShapeResource();
                if (mShape instanceof WipeShape) {
                    resource.mType = Type.WIPE;
                    resource.mCurvePath = ((CurveShape) mShape).getPath();
                } else if (mShape instanceof CurveShape) {
                    resource.mType = Type.CURVE;
                    resource.mCurvePath = ((CurveShape) mShape).getPath();
                } else if (mShape instanceof LineShape) {
                    saveShapeResource(resource,Type.LINE);
                } else if (mShape instanceof OvalShape) {
                    saveShapeResource(resource,Type.OVAL);
                } else if (mShape instanceof RectShape) {
                    saveShapeResource(resource,Type.RECTANGLE);
                } else if(mShape instanceof MultiLineShape){
                    //多边形分解成直线
                    saveShapeResource(resource,Type.LINE);
                }
                mSavePath.add(resource);
                invalidate();
                mShape.draw(mCanvas);
                return true;
            default:
                return false;
        }
    }

    //每次ACTION_UP事件保存路径参数
    public void saveShapeResource(ShapeResource resource,int type){
        resource.mType = type;
        resource.mStartX = (mShape).getStartX();
        resource.mStartY = (mShape).getStartY();
        resource.mEndX = ( mShape).getEndX();
        resource.mEndY = ( mShape).getEndY();
        resource.mPaint = mShape.getPaint();
    }

    //撤回
    public void reCall() {
        if (mSavePath.size() == 0) {
            Utils.SnackbarShort(this,"对不起，不能撤回");
            return;
        }
        ShapeResource resource = mSavePath.get(mSavePath.size() - 1);
        mDeletePath.add(resource);
        mSavePath.remove(mSavePath.size() - 1);
        if (mShape instanceof MultiLineShape) {
            MultiLineShape.setNewStartPoint(resource.mStartX, resource.mStartY);
        }
        updateBitmap();
        isRecentRecallOrUndo = true;
    }

    //恢复
    public void undo() {
        if (mDeletePath.size() == 0) {
            Utils.SnackbarShort(this,"对不起，不能恢复");
            return;
        }
        ShapeResource resource = mDeletePath.get(mDeletePath.size() - 1);
        if (mShape instanceof MultiLineShape) {
            if(resource.mStartX != MultiLineShape.getNextPointX() || resource.mStartY != MultiLineShape.getNextPointY()){
                Utils.SnackbarShort(this,"对不起，不能恢复");
                return;
            }else {
                MultiLineShape.setNewStartPoint(resource.mEndX, resource.mEndY);
            }
        }
        mSavePath.add(resource);
        mDeletePath.remove(mDeletePath.size() - 1);
        updateBitmap();
        isRecentRecallOrUndo = true;
    }

    //更新bitmap
    public void updateBitmap() {
        mDrawBitmap.eraseColor(Color.WHITE);
        mCanvas = new Canvas(mDrawBitmap);
        isClearScreen = true;
        invalidate();
        for (ShapeResource resource : mSavePath) {

            switch (resource.mType) {
                case Type.WIPE:
                case Type.CURVE:
                    mCanvas.drawPath(resource.mCurvePath, resource.mCurvePath.mPaint);
                    break;
                case Type.LINE:
                    mCanvas.drawLine(resource.mStartX, resource.mStartY,
                            resource.mEndX, resource.mEndY, resource.mPaint);
                    break;
                case Type.OVAL:
                    RectF rectF = new RectF(resource.mStartX, resource.mStartY,
                            resource.mEndX, resource.mEndY);
                    mCanvas.drawOval(rectF, resource.mPaint);
                    break;
                case Type.RECTANGLE:
                    mCanvas.drawRect(resource.mStartX, resource.mStartY,
                            resource.mEndX, resource.mEndY, resource.mPaint);
                    break;
            }
        }
    }

    //清屏
    public void clearScreen() {
        mDrawBitmap.eraseColor(Color.WHITE);
        mCanvas = new Canvas(mDrawBitmap);
        mSavePath.clear();
        mDeletePath.clear();
        isClearScreen = true;
        mShape = null;
        MultiLineShape.clear();
        invalidate();
    }

    //从本地文件读取的笔迹
    public void updateDrawFromPaths(List<ShapeResource> data) {
        clearScreen();
        mSavePath.addAll(data);
        loadBitmapFromLocal();
        Utils.Log("setDrawPaths : data.size() : " + data.size());
    }

    //加载本地文件笔迹
    public void loadBitmapFromLocal() {
        for (ShapeResource resource : mSavePath) {
            if(resource.mPaint != null){
                resource.mPaint.loadPaint();
                Utils.Log("paint.color : " + resource.mPaint.getColor());
                Utils.Log("paint.width : " + resource.mPaint.getStrokeWidth());
            }
            switch (resource.mType) {
                case Type.WIPE:
                case Type.CURVE:
                    resource.mCurvePath.loadPathPointsAsQuadTo();
                    resource.mCurvePath.mPaint.loadPaint();
                    mCanvas.drawPath(resource.mCurvePath, resource.mCurvePath.mPaint);
                    break;
                case Type.LINE:
                    mCanvas.drawLine(resource.mStartX, resource.mStartY,
                            resource.mEndX, resource.mEndY, resource.mPaint);
                    break;
                case Type.OVAL:
                    RectF rectF = new RectF(resource.mStartX, resource.mStartY,
                            resource.mEndX, resource.mEndY);
                    mCanvas.drawOval(rectF, resource.mPaint);
                    break;
                case Type.RECTANGLE:
                    mCanvas.drawRect(resource.mStartX, resource.mStartY,
                            resource.mEndX, resource.mEndY, resource.mPaint);
                    break;
            }
        }
    }

    public void setDrawType(int type) {
        //如果点击撤回或者恢复后重新绘制，则清空mDeletePath
        if (isRecentRecallOrUndo) {
            mDeletePath.clear();
            isRecentRecallOrUndo = false;
        }
        mDrawType = type;
    }

    public Bitmap getDrawBitmap() {
        return mDrawBitmap;
    }

    public ArrayList<ShapeResource> getNotePath() {
        return mSavePath;
    }

    //创建白色背景的bitmap
    public Bitmap createBitmap(int width, int height) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        Bitmap bitmap = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(bitmap);
        mCanvas.drawRect(0, 0, width, height, paint);
        mCanvas.drawBitmap(bitmap, 0, 0, paint);
        return bitmap;
    }

    //暴露down事件给FloatViewGroup
    public void setOnDownAction(OnDownAction action) {
        mDownAction = action;
    }

    public interface OnDownAction {
        void dealDownAction();
    }
}