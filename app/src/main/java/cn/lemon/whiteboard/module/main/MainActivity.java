package cn.lemon.whiteboard.module.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import cn.alien95.util.Utils;
import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.whiteboard.R;
import cn.lemon.whiteboard.app.Config;
import cn.lemon.whiteboard.data.AccountModel;
import cn.lemon.whiteboard.data.CurveModel;
import cn.lemon.whiteboard.module.account.ImageActivity;
import cn.lemon.whiteboard.module.account.Note;
import cn.lemon.whiteboard.module.account.NoteActivity;
import cn.lemon.whiteboard.widget.BoardView;
import cn.lemon.whiteboard.widget.FloatAdapter;
import cn.lemon.whiteboard.widget.FloatViewGroup;
import cn.lemon.whiteboard.widget.InputDialog;
import cn.lemon.whiteboard.widget.shape.DrawShape;

public class MainActivity extends ToolbarActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BoardView mBoardView;
    private FloatViewGroup mFloatViews;
    private FloatAdapter mAdapter;
    private long mFirstPressBackTime = 0;
    private Handler mHandler;
    private Note mNote;

    private boolean isShowingColorSelector = false;
    private boolean isShowingSizeSelector = false;

    private PopupWindow mColorWindow;
    private PopupWindow mSizeWindow;

    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarHomeBack(false);
        setContentView(R.layout.main_activity);

        mHandler = new Handler(getMainLooper());

        mFloatViews = (FloatViewGroup) findViewById(R.id.float_view_group);
        mBoardView = (BoardView) findViewById(R.id.board_view);

        //系统生成
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (isShowingColorSelector) {
                    mColorWindow.dismiss();
                    isShowingColorSelector = false;
                } else if (isShowingSizeSelector) {
                    mSizeWindow.dismiss();
                    isShowingSizeSelector = false;
                }
            }
        };
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        mAdapter = new FunctionAdapter(this, mBoardView);
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
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.recall:
                mBoardView.reCall();
                break;
            case R.id.recover:
                mBoardView.undo();
                break;
            case R.id.save_note:
                showNoteDialog();
                break;
            case R.id.save_image_album:
                CurveModel.getInstance().saveCurveToAlbum(mBoardView.getDrawBitmap());
                break;
            case R.id.save_image_to_app:
                CurveModel.getInstance().saveCurveToApp(mBoardView.getDrawBitmap());
                break;
            case R.id.color:
                if (isShowingColorSelector) {
                    mColorWindow.dismiss();
                    isShowingColorSelector = false;
                } else {
                    showColorSelectorWindow();
                }
                break;
            case R.id.size:
                if (isShowingSizeSelector) {
                    mSizeWindow.dismiss();
                    isShowingSizeSelector = false;
                } else {
                    showSizeSelectorWindow();
                }
                break;
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mDrawer.closeDrawer(GravityCompat.START);
        int id = item.getItemId();
        switch (id) {
            case R.id.draw_board:
                break;
            case R.id.note:
                Intent intent = new Intent(new Intent(this, NoteActivity.class));
                startActivityForResult(intent, Config.NOTE_REQUEST_CODE);
                break;
            case R.id.image:
                startActivity(ImageActivity.class);
                break;
            case R.id.about:
                break;
        }
        return true;
    }

    public void startActivity(Class target) {
        Intent intent = new Intent(this, target);
        startActivity(intent);
    }

    //保存笔迹
    public void showNoteDialog() {

        final InputDialog noteDialog = new InputDialog(this);
        noteDialog.setTitle("请输入标题");
        noteDialog.setHint("标题");
        if (mNote != null) {
            noteDialog.setContent(mNote.mTitle);
        }
        noteDialog.show();
        noteDialog.setPositiveClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(noteDialog.getContent())) {
                    Utils.Toast("标题不能为空");
                    return;
                }
                Note note = new Note();
                note.mTitle = noteDialog.getContent().toString();
                note.mPaths = mBoardView.getNotePath();
                long time = System.currentTimeMillis();
                note.mCreateTime = time;
                note.mFileName = time + "";
                AccountModel.getInstance().saveNote(note);
                if (mNote != null) {
                    AccountModel.getInstance().deleteNoteFile(mNote.mFileName);
                    mNote = null;
                }
                noteDialog.dismiss();
                Utils.Toast("保存成功");
                mBoardView.clearScreen();
            }
        });
    }

    //设置画笔大小
    public void showSizeSelectorWindow() {
        if (isShowingSizeSelector) {
            return;
        } else if (isShowingColorSelector) {
            mColorWindow.dismiss();
            isShowingColorSelector = false;
        }
        isShowingSizeSelector = true;
        View view = LayoutInflater.from(this).inflate(R.layout.window_size_selector, null);
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.seek_bar);
        final TextView size = (TextView) view.findViewById(R.id.size);
        if(mBoardView.getCurrentShape() == null){
            seekBar.setProgress(4);
            size.setText("4");
        }else {
            int numSize = (int) mBoardView.getCurrentShape().getPaintWidth();
            seekBar.setProgress(numSize);
            size.setText(numSize + "");
        }

        mSizeWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mSizeWindow.showAsDropDown(getToolbar());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                size.setText(progress + "");
                DrawShape.mPaintWidth = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mSizeWindow.dismiss();
                isShowingSizeSelector = false;
            }
        });
    }


    public void showColorSelectorWindow(){
        if (isShowingColorSelector) {
            return;
        } else if (isShowingSizeSelector) {
            mSizeWindow.dismiss();
            isShowingSizeSelector = false;
        }
        isShowingColorSelector = true;
        View view = LayoutInflater.from(this).inflate(R.layout.window_color_selector, null);
        mColorWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        RefreshRecyclerView recyclerView = (RefreshRecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        ColorAdapter adapter = new ColorAdapter(this, Config.COLORS, mColorWindow);
        recyclerView.setAdapter(adapter);

        mColorWindow.showAsDropDown(getToolbar());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.NOTE_REQUEST_CODE && resultCode == Config.NOTE_RESULT_CODE) {
            mNote = (Note) data.getSerializableExtra(Config.NOTE_DATA);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mBoardView.setDrawPaths(mNote.mPaths);
                }
            });

        }
    }

    public void setNoteNull() {
        mNote = null;
    }

    public void setShowingColorSelector(boolean b) {
        isShowingColorSelector = b;
    }
}
