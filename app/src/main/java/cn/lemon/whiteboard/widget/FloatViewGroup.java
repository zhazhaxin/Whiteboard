package cn.lemon.whiteboard.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

public class FloatViewGroup extends ViewGroup implements View.OnClickListener {
    private FloatAdapter adapter;
    private ObjectAnimator animator;
    private ImageView mSwitchView;
    private int width, height;
    private int mItemCount;
    private View[] mItems;
    private boolean IS_EXPAND = false;
    private int CHANGE_TIME = 200;

    public FloatViewGroup(Context context) {
        this(context, null);
    }

    public FloatViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        width = r - l;
        height = b - t;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.layout(width - view.getMeasuredWidth(),
                    height - view.getMeasuredHeight(),
                    width, height);
        }
    }

    public void setAdapter(FloatAdapter adapter) {
        this.adapter = adapter;
        initView();
    }

    public void initView() {
        if (adapter == null) {
            return;
        }
        mSwitchView = adapter.getSwitchView();
        mItemCount = adapter.getCount();
        mItems = new View[mItemCount];
        for (int i = 0; i < mItemCount; i++) {
            mItems[i] = adapter.getItem(i);
            mItems[i].setVisibility(INVISIBLE);
            addView(mItems[i]);
        }
        addView(mSwitchView);

        mSwitchView.setClickable(true);
        mSwitchView.setOnClickListener(this);
    }

    //打开
    public void expandViews() {
        ObjectAnimator.ofFloat(mSwitchView, "rotation", 0f, 45f).setDuration(CHANGE_TIME).start();
        for (int i = 0; i < mItemCount; i++) {
            float curTranslationY = mItems[i].getTranslationY();
            animator = ObjectAnimator.ofFloat(mItems[i], "translationY", curTranslationY, -(i + 1) * mItems[i].getMeasuredHeight());
            animator.setInterpolator(new OvershootInterpolator());
            animator.setDuration(CHANGE_TIME).start();
            mItems[i].setVisibility(VISIBLE);
        }
        IS_EXPAND = true;
    }

    public void checkShrinkViews(){
        if(IS_EXPAND){
            shrinkViews();
        }
    }

    //关闭
    public void shrinkViews() {
        ObjectAnimator.ofFloat(mSwitchView, "rotation", 45f, 0f).setDuration(CHANGE_TIME).start();
        for (int i = 0; i < mItemCount; i++) {
            float curTranslationY = mItems[i].getTranslationY();
            animator = ObjectAnimator.ofFloat(mItems[i], "translationY", curTranslationY, 0f);
            animator.setDuration(CHANGE_TIME).start();
            final int finalI = i;
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mItems[finalI].setVisibility(INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            }
        IS_EXPAND = false;
    }

    @Override
    public void onClick(View v) {
        if (IS_EXPAND) {
            shrinkViews();
        } else {
            expandViews();
        }
    }
}
