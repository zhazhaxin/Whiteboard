package cn.lemon.whiteboard.module.main;

import android.content.Context;

import cn.lemon.whiteboard.R;
import cn.lemon.whiteboard.widget.BoardView;
import cn.lemon.whiteboard.widget.FloatAdapter;
import cn.lemon.whiteboard.widget.type.Type;

class FunctionAdapter extends FloatAdapter {

    private String[] mHints = {"清屏","擦除","矩形", "圆形", "直线", "曲线"};
    private int[] mDrawables = {R.drawable.ic_clear,R.drawable.ic_wipe,R.drawable.ic_rectangle,
            R.drawable.ic_oval, R.drawable.ic_line, R.drawable.ic_curve};

    private BoardView mBoardView;

    public FunctionAdapter(Context context,BoardView boardView) {
        super(context);
        mBoardView = boardView;
    }

    @Override
    public int getCount() {
        return mHints.length;
    }

    @Override
    public String getItemHint(int position) {
        return mHints[position];
    }

    @Override
    public int getItemResource(int position) {
        return mDrawables[position];
    }

    @Override
    public int getMainResource() {
        return R.drawable.ic_float_switch;
    }

    @Override
    public void onItemClick(int position) {
        switch (position){
            case 0:
                mBoardView.clear();
                break;
            case 1:
                mBoardView.setWipeMode();
                break;
            case 2:
                mBoardView.setDrawType(Type.RECTANGLE);
                break;
            case 3:
                mBoardView.setDrawType(Type.OVAL);
                break;
            case 4:
                mBoardView.setDrawType(Type.LINE);
                break;
            case 5:
                mBoardView.setDrawType(Type.CURVE);
                break;
        }

    }
}