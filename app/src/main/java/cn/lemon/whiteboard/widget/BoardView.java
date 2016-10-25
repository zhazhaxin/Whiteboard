package cn.lemon.whiteboard.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import cn.lemon.whiteboard.widget.type.CurveShape;
import cn.lemon.whiteboard.widget.type.DrawShape;
import cn.lemon.whiteboard.widget.type.LineShape;
import cn.lemon.whiteboard.widget.type.OvalShape;
import cn.lemon.whiteboard.widget.type.RectShape;
import cn.lemon.whiteboard.widget.type.Type;

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

    private final float WIPE_SIZE = 50f;

    private boolean isClear = false;
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
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
                    case Type.RECTANGLE:
                        mShape = new RectShape(this);
                        break;
                    case Type.OVAL:
                        mShape = new OvalShape(this);
                        break;
                    case Type.LINE:
                        mShape = new LineShape(this);
                        break;
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                int mCurrentX = (int) event.getX();
                int mCurrentY = (int) event.getY();
                mShape.touchMove(mStartX, mStartY, mCurrentX, mCurrentY);
                return true;

            case MotionEvent.ACTION_UP:
                //把之前的path保存绘制到mDrawBitmap上
                mShape.draw(mCanvas);
                invalidate();
                return true;

            default:
                return false;
        }
    }

    public void clear() {
        mDrawBitmap.eraseColor(Color.WHITE);
        mCanvas = new Canvas(mDrawBitmap);
        isClear = true;
        invalidate();
    }

    public void setWipeMode(boolean isWipe){
        if(isWipe){
            mShape.setPaintColor(Color.WHITE);
            mShape.setPaintWidth(WIPE_SIZE);
        }
    }

    public DrawShape getCurrentShape(){
        return mShape;
    }

    public void setDrawType(int type){
        mDrawType = type;
    }

    public Bitmap getDrawBitmap(){
        return mDrawBitmap;
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

    //暴露down事件给floatviewgroup
    public void setOnDownAction(OnDownAction action){
        mDownAction = action;
    }

    public interface OnDownAction{
        void dealDownAction();
    }
}