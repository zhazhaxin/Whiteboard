package cn.lemon.whiteboard.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.alien95.util.Utils;
import cn.lemon.whiteboard.widget.shape.CurveShape;
import cn.lemon.whiteboard.widget.shape.DrawShape;
import cn.lemon.whiteboard.widget.shape.LineShape;
import cn.lemon.whiteboard.widget.shape.OvalShape;
import cn.lemon.whiteboard.widget.shape.RectShape;
import cn.lemon.whiteboard.widget.shape.Type;
import cn.lemon.whiteboard.widget.shape.WipeShape;
import cn.lemon.whiteboard.widget.shape.WritablePath;

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

    private int mStartX = 0;
    private int mStartY = 0;

    private boolean isClear = false;
    private boolean isCanReCall = true; //是否能撤回

    private ArrayList<WritablePath> mSavePath;
    private ArrayList<WritablePath> mDeletePath;

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
        mDrawBitmap = createBitmap(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制到mDrawBitmap
        canvas.drawBitmap(mDrawBitmap, 0, 0, mPaint);
        //同步绘制到BoardView自己的画布上
        if (mShape != null && !isClear) {
            mShape.draw(canvas);
        }else if(isClear){
            isClear = false;
            isCanReCall = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int mCurrentX = (int) event.getX();
        int mCurrentY = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(mDownAction != null){
                    mDownAction.dealDownAction();
                }
                mStartX = (int) event.getX();
                mStartY = (int) event.getY();
                //曲线
                switch (mDrawType){
                    case Type.CURVE:
                        mShape = new CurveShape(this);
                        break;
                    case Type.WIPE:
                        mShape = new WipeShape(this);
                        break;
                    case Type.RECTANGLE:
                        isCanReCall = false;
                        mShape = new RectShape(this);
                        break;
                    case Type.OVAL:
                        isCanReCall = false;
                        mShape = new OvalShape(this);
                        break;
                    case Type.LINE:
                        isCanReCall = false;
                        mShape = new LineShape(this);
                        break;
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                mShape.touchMove(mStartX, mStartY, mCurrentX, mCurrentY);
                return true;

            case MotionEvent.ACTION_UP:
                //把之前的path保存绘制到mDrawBitmap上
                if (isCanReCall && mShape instanceof CurveShape) {
                    mSavePath.add(((CurveShape) mShape).getPath());
                }
                mShape.draw(mCanvas);
                return true;

            default:
                return false;
        }
    }

    //撤回
    public void reCall() {
        if (!isCanReCall || mSavePath.size() == 0) {
            Utils.Toast("对不起，不能撤回");
            return;
        }
        mDeletePath.add(mSavePath.get(mSavePath.size() - 1));
        mSavePath.remove(mSavePath.size() - 1);
        updateBitmap();
    }

    //绘制后一步
    public void recover() {
        if (!isCanReCall || mDeletePath.size() == 0) {
            Utils.Toast("对不起，不能恢复");
            return;
        }
        mSavePath.add(mDeletePath.get(mDeletePath.size() - 1));
        mDeletePath.remove(mDeletePath.size() - 1);
        updateBitmap();
    }

    //更新bitmap
    public void updateBitmap() {
        clearScreen();
        for (WritablePath path : mSavePath) {
            mCanvas.drawPath(path, path.mPaint);
        }
    }

    //清屏
    public void clearScreen() {
        mDrawBitmap.eraseColor(Color.WHITE);
        mCanvas = new Canvas(mDrawBitmap);
        mSavePath.clear();
        mDeletePath.clear();
        isClear = true;
        invalidate();
    }

    //从本地文件读取
    public void setDrawPaths(List<WritablePath> data){
        clearScreen();
        mSavePath.addAll(data);
        updateBitmapFromLocal();
    }

    public void updateBitmapFromLocal(){
        for (WritablePath path : mSavePath) {
            path.loadPathPointsAsQuadTo();
            path.mPaint.loadPaint();
            Utils.Log("paint.color : " + path.mPaint.getColor());
            Utils.Log("paint.width : " + path.mPaint.getStrokeWidth());
            Utils.Log("path.isEmpty() : " + path.isEmpty());
            mCanvas.drawPath(path, path.mPaint);
        }
    }

    public void setDrawType(int type) {
        mDrawType = type;
    }

    public DrawShape getCurrentShape(){
        return mShape;
    }

    public Bitmap getDrawBitmap(){
        return mDrawBitmap;
    }

    //只限制在笔迹模式下
    public ArrayList<WritablePath> getNotePath() {
        return mSavePath;
    }

    //创建白色背景的bitmap
    public Bitmap createBitmap(int width,int height){
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
    public void setOnDownAction(OnDownAction action){
        mDownAction = action;
    }

    public interface OnDownAction{
        void dealDownAction();
    }
}