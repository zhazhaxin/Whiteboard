package cn.lemon.whiteboard.module.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import cn.alien95.util.Utils;
import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.whiteboard.R;
import cn.lemon.whiteboard.app.Config;
import cn.lemon.whiteboard.data.CurveModel;
import cn.lemon.whiteboard.data.NoteModel;
import cn.lemon.whiteboard.module.note.Note;
import cn.lemon.whiteboard.module.note.NoteActivity;
import cn.lemon.whiteboard.widget.BoardView;
import cn.lemon.whiteboard.widget.FloatAdapter;
import cn.lemon.whiteboard.widget.FloatViewGroup;

import static cn.lemon.whiteboard.R.id.note;

public class MainActivity extends ToolbarActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BoardView mBoardView;
    private FloatViewGroup mFloatViews;
    private FloatAdapter mAdapter;
    private long mFirstPressBackTime = 0;
    private Handler mHandler;
    private Note mNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.Log("onCreate");
        super.onCreate(savedInstanceState);
        setToolbarHomeBack(false);
        setContentView(R.layout.main_activity);

        mHandler = new Handler(getMainLooper());

        mFloatViews = (FloatViewGroup) findViewById(R.id.float_view_group);
        mBoardView = (BoardView) findViewById(R.id.board_view);

        //系统生成
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAdapter = new FunctionAdapter(this,mBoardView);
        mFloatViews.setAdapter(mAdapter);
        mBoardView.setOnDownAction(new BoardView.OnDownAction() {
            @Override
            public void dealDownAction() {
                mFloatViews.checkShrinkViews();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() - mFirstPressBackTime > 1000) {
                mFirstPressBackTime = System.currentTimeMillis();
                Utils.Toast("再点击一次退出App");
            } else
                super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_image:
                CurveModel.getInstance().saveCurve(mBoardView.getDrawBitmap());
                break;
            case R.id.save_note:
                if(mBoardView.isCanSaveNote()){
                    showNoteDialog();
                }else {
                    Utils.Toast("不能保存");
                }
                break;
            case R.id.color:
                mBoardView.getCurrentShape().setPaintColor(Color.BLUE);
                break;
            case R.id.size:
                mBoardView.getCurrentShape().setPaintWidth(15f);
                break;
            case R.id.recall:
                mBoardView.reCall();
                break;
            case R.id.recover:
                mBoardView.recover();
                break;
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.draw_board) {

        } else if (id == note) {
            Intent intent = new Intent(new Intent(this, NoteActivity.class));
            startActivityForResult(intent, Config.NOTE_REQUEST_CODE);
        } else if (id == R.id.about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //保存笔迹
    public void showNoteDialog() {

        final EditText inputContent = new EditText(this);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT - Utils.dip2px(32), ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(Utils.dip2px(16), 0, Utils.dip2px(16), 0);
        inputContent.setLayoutParams(params);
        if (mNote != null) {
            inputContent.setText(mNote.mTitle);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog inputDialog = builder.create();
        builder.setTitle("请输入标题")
                .setView(inputContent)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextUtils.isEmpty(inputContent.getText())) {
                            Utils.Toast("标题不能为空");
                            return;
                        }
                        Note note = new Note();
                        note.mTitle = inputContent.getText().toString();
                        note.mPaths = mBoardView.getNotePath();
                        long time = System.currentTimeMillis();
                        note.mCreateTime = time;
                        note.mFileName = time + "";
                        NoteModel.getInstance().saveNote(note);
                        if (mNote != null) {
                            NoteModel.getInstance().deleteNoteFile(mNote.mFileName);
                            mNote = null;
                        }
                        inputDialog.dismiss();
                        Utils.Toast("保存成功");
                        mBoardView.clearScreen();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inputDialog.dismiss();
                    }
                }).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Config.NOTE_REQUEST_CODE && resultCode == Config.NOTE_RESULT_CODE){
            mNote = (Note) data.getSerializableExtra(Config.NOTE_DATA);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mBoardView.setDrawPaths(mNote.mPaths);
                    Utils.Log("paths.size() :　" + mNote.mPaths.size());
                }
            });

        }
    }

    public void setNoteNull(){
        mNote = null;
    }
}
