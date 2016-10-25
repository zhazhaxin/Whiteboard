package cn.lemon.whiteboard.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alien on 2015/6/15.
 */
public class FloatViewGroup extends ViewGroup {
    private FloatAdapter adapter;
    private ObjectAnimator animator;
    private ImageView mSwitchView;
    private List<View> views = new ArrayList<>();
    private int width, height;
    private static boolean IS_EXPAND = false;
    private static int EXTENT_TIME = 200, SHRINK_TIME = 200;

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
            views.add(view);
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
        final int itemNum = adapter.getCount();
        final View[] items = new View[itemNum];
        for (int i = 0; i < itemNum; i++) {
            items[i] = adapter.getItem(i);
            items[i].setVisibility(INVISIBLE);
            addView(items[i]);
        }
        addView(mSwitchView);

        mSwitchView.setClickable(true);
        mSwitchView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!IS_EXPAND) {
                    ObjectAnimator.ofFloat(mSwitchView, "rotation", 0f, 180f + 45f).setDuration(EXTENT_TIME).start();
                    for (int i = 0; i < itemNum; i++) {
                        float curTranslationY = items[i].getTranslationY();
                        animator = ObjectAnimator.ofFloat(items[i], "translationY", curTranslationY, -(i + 1) * items[i].getMeasuredHeight());
                        animator.setInterpolator(new OvershootInterpolator());
                        animator.setDuration(EXTENT_TIME).start();
                        items[i].setVisibility(VISIBLE);
                    }
                    IS_EXPAND = true;
                } else {
                    ObjectAnimator.ofFloat(mSwitchView, "rotation", 180f + 45f, 0f).setDuration(SHRINK_TIME).start();
                    for (int i = 0; i < itemNum; i++) {
                        float curTranslationY = items[i].getTranslationY();
                        animator = ObjectAnimator.ofFloat(items[i], "translationY", curTranslationY, 0f);
                        animator.setDuration(SHRINK_TIME).start();
                        final int finalI = i;
                        animator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                items[finalI].setVisibility(INVISIBLE);
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
            }
        });
    }

}
