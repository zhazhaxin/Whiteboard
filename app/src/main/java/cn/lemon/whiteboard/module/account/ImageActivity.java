package cn.lemon.whiteboard.module.account;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import java.util.List;

import cn.alien95.util.Utils;
import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.common.base.presenter.RequirePresenter;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.whiteboard.R;

@RequirePresenter(ImagePresenter.class)
public class ImageActivity extends ToolbarActivity<ImagePresenter> {

    private RefreshRecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        useStatusPages(true);
        setContentView(R.layout.account_activity_image);

        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        mRecyclerView.setItemSpace(Utils.dip2px(4),Utils.dip2px(4),Utils.dip2px(4),Utils.dip2px(4));
        mAdapter = new ImageAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addRefreshAction(new Action() {
            @Override
            public void onAction() {
                getPresenter().getData();
            }
        });
    }

    public void setData(List<String> data){
        mAdapter.clear();
        mAdapter.addAll(data);
        mRecyclerView.dismissSwipeRefresh();
    }
}
