package cn.lemon.whiteboard.module.account;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import java.util.List;

import cn.lemon.common.base.ToolbarActivity;
import cn.lemon.common.base.presenter.RequirePresenter;
import cn.lemon.view.RefreshRecyclerView;
import cn.lemon.view.adapter.Action;
import cn.lemon.whiteboard.R;

@RequirePresenter(NotePresenter.class)
public class NoteActivity extends ToolbarActivity<NotePresenter> {

    private RefreshRecyclerView mRecyclerView;
    private NoteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        useStatusPages(true);
        setContentView(R.layout.account_activity_note);

        mRecyclerView = (RefreshRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NoteAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addRefreshAction(new Action() {
            @Override
            public void onAction() {
                getPresenter().getData();
            }
        });
    }

    public void setData(List<Note> data){
        mAdapter.clear();
        mAdapter.addAll(data);
        mRecyclerView.dismissSwipeRefresh();
    }
}
