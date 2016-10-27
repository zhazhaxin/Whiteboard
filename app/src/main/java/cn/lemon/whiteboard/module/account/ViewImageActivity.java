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
import android.widget.TextView;

import java.io.File;
import java.util.List;

import cn.alien95.util.Utils;
import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.whiteboard.R;


/**
 * Created by linlongxin on 2016/1/22.
 */
public class ViewImageActivity extends ToolbarActivity {

    public static final String IMAGES_DATA_LIST = "DATA_LIST";
    public static final String IMAGE_NUM = "IMAGE_NUM";

    private ViewPager viewPager;
    private TextView number;
    private List<String> data;
    private int position;
    private int dataLength = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity_view_image);
        getWindow().setStatusBarColor(Color.BLACK);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        number = (TextView) findViewById(R.id.number);

        data = (List<String>) getIntent().getSerializableExtra(IMAGES_DATA_LIST);
        position = getIntent().getIntExtra(IMAGE_NUM, -1);
        dataLength = data.size();

        viewPager.setAdapter(new ViewImageAdapter(data, this));
        viewPager.setCurrentItem(position);
        number.setText(position + 1 + "/" + dataLength);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                number.setText(viewPager.getCurrentItem() + 1 + "/" + dataLength);
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
                share(data.get(viewPager.getCurrentItem()));
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
        }else {
            Utils.Toast("图片不存在");
        }
    }

}
