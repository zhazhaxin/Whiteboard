package cn.lemon.whiteboard.module.account;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import cn.alien95.util.Utils;
import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.whiteboard.R;


public class ViewImageActivity extends ToolbarActivity {

    public static final String IMAGES_DATA_LIST = "DATA_LIST";
    public static final String IMAGE_NUM = "IMAGE_NUM";

    private ViewPager mViewPager;
    private TextView mNumber;
    private List<String> mData;
    private int mDataSize = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity_view_image);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ViewGroup contentView = (ViewGroup) findViewById(android.R.id.content);
            View statusBarView = new View(this);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    Utils.getStatusBarHeight(this));
            statusBarView.setBackgroundColor(Color.BLACK);
            contentView.addView(statusBarView, lp);
        }

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mNumber = (TextView) findViewById(R.id.number);

        mData = (List<String>) getIntent().getSerializableExtra(IMAGES_DATA_LIST);
        int mPosition = getIntent().getIntExtra(IMAGE_NUM, -1);
        mDataSize = mData.size();

        mViewPager.setAdapter(new ViewImageAdapter(mData, this));
        mViewPager.setCurrentItem(mPosition);
        mNumber.setText(mPosition + 1 + "/" + mDataSize);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mNumber.setText(mViewPager.getCurrentItem() + 1 + "/" + mDataSize);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_view_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.share:
                share(mData.get(mViewPager.getCurrentItem()));
                break;
            default:
                break;
        }
        return true;
    }

    public void share(String imgPath) {
        File f = new File(imgPath);
        if (f.exists() && f.isFile()) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpg");
            Uri u = Uri.fromFile(f);
            intent.putExtra(Intent.EXTRA_STREAM, u);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Utils.Toast("图片不存在");
        }
    }

}
