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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

import cn.alien95.util.Utils;
import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.common.base.presenter.RequirePresenter;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.whiteboard.R;
import cn.lemon.whiteboard.app.Config;
import cn.lemon.whiteboard.module.account.ImageActivity;
import cn.lemon.whiteboard.module.account.NoteActivity;
import cn.lemon.whiteboard.widget.BoardView;
import cn.lemon.whiteboard.widget.FloatAdapter;
import cn.lemon.whiteboard.widget.FloatViewGroup;
import cn.lemon.whiteboard.widget.InputDialog;
import cn.lemon.whiteboard.widget.shape.DrawShape;
import cn.lemon.whiteboard.widget.shape.ShapeResource;


@RequirePresenter(MainPresenter.class)
public class MainActivity extends ToolbarActivity<MainPresenter>
        implements NavigationView.OnNavigationItemSelectedListener {

    private BoardView mBoardView;
    private FloatViewGroup mFloatViews;
    private FloatAdapter mAdapter;
    private long mFirstPressBackTime = 0;
    private Handler mHandler;

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
                Utils.SnackbarShort(getToolbar(), "再点击一次退出App");
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
            case R.id.save_image_to_app:
                getPresenter().saveImage(mBoardView.getDrawBitmap());
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
                default:break;
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
                getPresenter().startActivity(ImageActivity.class);
                break;
            case R.id.about:
                getPresenter().startActivity(AboutActivity.class);
                break;
                default:
                    break;
        }
        return true;
    }

    //保存笔迹
    public void showNoteDialog() {

        final InputDialog noteDialog = new InputDialog(this);
        noteDialog.setCancelable(false);
        noteDialog.setTitle("请输入标题");
        noteDialog.setHint("标题");
        if (getPresenter().getLocalNote() != null) {
            noteDialog.setContent(getPresenter().getLocalNote().mTitle);
        }
        noteDialog.show();
        noteDialog.setPositiveClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getPresenter().saveNote(noteDialog.getContent(),mBoardView.getNotePath());
                noteDialog.dismiss();
                mBoardView.clearScreen();
            }
        });
        noteDialog.setPassiveClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                noteDialog.dismiss();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBoardView.updateBitmap();
                    }
                }, 100);
            }
        });
    }

    public void updateDrawPaths(List<ShapeResource> paths){
        mBoardView.updateDrawFromPaths(paths);
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
        View view = LayoutInflater.from(this).inflate(R.layout.main_window_size_selector, null);
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.seek_bar);
        final TextView size = (TextView) view.findViewById(R.id.size);
        int numSize = (int) DrawShape.mPaintWidth;
        seekBar.setProgress(numSize);
        size.setText(numSize + "");

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

    //颜色选择器
    public void showColorSelectorWindow(){
        if (isShowingColorSelector) {
            return;
        } else if (isShowingSizeSelector) {
            mSizeWindow.dismiss();
            isShowingSizeSelector = false;
        }
        isShowingColorSelector = true;
        View view = LayoutInflater.from(this).inflate(R.layout.main_window_color_selector, null);
        mColorWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        RefreshRecyclerView recyclerView = (RefreshRecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        ColorAdapter adapter = new ColorAdapter(this, Config.COLORS, mColorWindow);
        recyclerView.setAdapter(adapter);

        mColorWindow.showAsDropDown(getToolbar());
    }

    public void setShowingColorSelector(boolean b) {
        isShowingColorSelector = b;
    }
}
