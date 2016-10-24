package cn.lemon.whiteboard;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.whiteboard.type.Type;

public class MainActivity extends ToolbarActivity implements View.OnClickListener {

    private BoardView mBoardView;
    private Button mClear;
    private Button mCurve;
    private Button mRectangle;
    private Button mOval;
    private Button mLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarHomeBack(false);
        setContentView(R.layout.activity_main);

        mBoardView = (BoardView) findViewById(R.id.board_view);
        mClear = (Button) findViewById(R.id.clear);
        mCurve = (Button) findViewById(R.id.curve);
        mRectangle = (Button) findViewById(R.id.rectangle);
        mOval = (Button) findViewById(R.id.oval);
        mLine = (Button) findViewById(R.id.line);
        mClear.setOnClickListener(this);
        mCurve.setOnClickListener(this);
        mRectangle.setOnClickListener(this);
        mOval.setOnClickListener(this);
        mLine.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.curve:
                mBoardView.setDrawType(Type.CURVE);
                break;
            case R.id.rectangle:
                mBoardView.setDrawType(Type.RECTANGLE);
                break;
            case R.id.oval:
                mBoardView.setDrawType(Type.OVAL);
                break;
            case R.id.line:
                mBoardView.setDrawType(Type.LINE);
                break;
            case R.id.clear:
                mBoardView.clear();
                break;
        }
    }
}
