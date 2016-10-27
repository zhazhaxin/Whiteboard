package cn.lemon.whiteboard.module.account;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import cn.lemon.whiteboard.R;


/**
 * Created by linlongxin on 2016/1/22.
 */
public class ViewImageActivity extends AppCompatActivity {

    public static final String IMAGES_DATA_LIST = "DATA_LIST";
    public static final String IMAGE_NUM = "IMAGE_NUM";

    private Toolbar toolbar;
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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        number = (TextView) findViewById(R.id.number);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = (List<String>) getIntent().getSerializableExtra(IMAGES_DATA_LIST);
        position = getIntent().getIntExtra(IMAGE_NUM, -1);
        dataLength = data.size();

        viewPager.setAdapter(new ViewImageAdapter(data,this));
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }


}
