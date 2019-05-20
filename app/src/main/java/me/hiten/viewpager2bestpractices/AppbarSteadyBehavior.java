package me.hiten.viewpager2bestpractices;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.appbar.AppBarLayout;

/**
 * 解决AppbarLayout和横向滑动RecyclerView冲突，提供更加稳定的滑动体验
 */
public class AppbarSteadyBehavior extends AppBarLayout.Behavior {

    private float mTouchSlop;

    public AppbarSteadyBehavior(Context context) {
        super();
        final ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
    }

    public AppbarSteadyBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        final ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
    }


    private int mTotalCheckScrollY = 0;
    private boolean mStartScrolled;

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, int[] consumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
        int consumedY = consumed[1];
        if (consumedY != 0) {
            dispatchState(State.START);
        }
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
        mTotalCheckScrollY += dy;
        if (Math.abs(mTotalCheckScrollY) >= mTouchSlop) {
            mStartScrolled = true;
        }
        if (mStartScrolled) {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        } else {
            consumed[1] = dy;
        }

        int consumedY = consumed[1];
        if (mStartScrolled && consumedY != 0) {
            dispatchState(State.START);
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes, int type) {
        boolean result = super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type);
        if (result) {
            mTotalCheckScrollY = 0;
            mStartScrolled = false;
        }
        return result;
    }


    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, abl, target, type);
        dispatchState(State.STOP);
    }


    private void dispatchState(State state) {
        if (mOnBehaviorListener != null && state != null) {
            mOnBehaviorListener.onNestedScrollState(state);
        }
    }

    private OnBehaviorListener mOnBehaviorListener;

    public void setOnBehaviorListener(OnBehaviorListener onBehaviorListener) {
        this.mOnBehaviorListener = onBehaviorListener;
    }

    public interface OnBehaviorListener {
        void onNestedScrollState(State state);
    }

    public enum State {
        START, STOP
    }
}
