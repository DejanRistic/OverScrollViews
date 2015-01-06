/*
 * ----------------------------------------------------------------------------
 * "THE BEER-WARE LICENSE" (Revision 42):
 * <phk@FreeBSD.ORG> wrote this file.  As long as you retain this notice you
 * can do whatever you want with this stuff. If we meet some day, and you think
 * this stuff is worth it, you can buy me a beer in return.
 * ----------------------------------------------------------------------------
 */

package com.fuzzydev.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EdgeEffect;
import android.widget.ScrollView;

import java.lang.reflect.Field;


/**
 * Naive ScrollView subclass that allows you to change the over scroll value and hook
 * a listener that will tell you how far you have over scrolled. Added OverScrollHeader
 * abillity to it. It also disables the glow effect at the top edge. The disabling is 
 * done by reflection, so it may at some point stop working if the field name changes, 
 * but a a global has been set for quick modification if need be.
 * <p>
 * <p>
 * Created by Dejan Ristic on 1/5/15.
 */
public class OverScrollView extends ScrollView {

    private static final String TAG = "OverScrollListView";

    private static final String TOP_EDGE_EFFECT_FIELD = "mEdgeGlowTop"; // Variable to change if field changes.

    private static final int DEFAULT_MAX_Y = 150;

    private int mMaxOverScrollY = DEFAULT_MAX_Y;

    private boolean didStartOverScroll = false;
    private boolean didFinishOverScroll = false;
    private boolean isClamped;

    private Drawable mHeaderDrawable;
    private EdgeEffect mTopEdgeEffect;
    private OverScrolledListener mListener;

    public interface OverScrolledListener {
        void overScrolled(int scrollY, int maxY, boolean exceededOffset, boolean didFinishOverScroll);
    }

    public OverScrollView(Context context) {
        super(context);
        init();
    }

    public OverScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OverScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setOverscrollHeader(Drawable drawable) {
        mHeaderDrawable = drawable;
    }

    public void setOverScrollListener(OverScrolledListener listener) {
        mListener = listener;
    }

    public void setOverScrollOffsetY(int offset) {
        mMaxOverScrollY = offset;
        if (mHeaderDrawable != null) {
            updateBounds();
        }
    }

    private void init() {
        setFadingEdgeLength(0);
        setVerticalFadingEdgeEnabled(false);
        getPrivateFieldMembers();
    }

    private void getPrivateFieldMembers() {
        try {
            mTopEdgeEffect = getTopEdgeEffect();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            Log.e(TAG, "The Reflection Failed! Check if the field name changed in AbsListView.java inside the AOSP!");
        }
    }

    private EdgeEffect getTopEdgeEffect() throws NoSuchFieldException, IllegalAccessException {
        Field f = ScrollView.class.getDeclaredField(TOP_EDGE_EFFECT_FIELD);
        if (f != null) {
            f.setAccessible(true);
            return (EdgeEffect) f.get(this);
        }
        return null;
    }

    private void reset() {
        smoothScrollTo(0, 0);
        didFinishOverScroll = true;
        didStartOverScroll = false;
        mListener.overScrolled(0, mMaxOverScrollY, false, true);
    }

    private void updateBounds() {
        if (mHeaderDrawable != null) {
            mHeaderDrawable.setBounds(0, -mMaxOverScrollY, getRight(), 0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        updateBounds();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                if (didStartOverScroll) {
                    if (isClamped) {
                        reset();
                        return true;
                    }
                }
                break;
        }

        return super.onTouchEvent(ev);

    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxOverScrollY, isTouchEvent);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);

        isClamped = clampedY;

        if (scrollY < 0 && !didStartOverScroll) {
            didStartOverScroll = true;
            didFinishOverScroll = false;
        }

        if (scrollY == 0 && didStartOverScroll) {
            didStartOverScroll = false;
            didFinishOverScroll = true;
        }

        if (mListener != null) {
            mListener.overScrolled(Math.abs(scrollY), mMaxOverScrollY, clampedY, didFinishOverScroll);
        } else {
            Log.v(TAG, "No scroll listener set");
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mTopEdgeEffect != null) {
            mTopEdgeEffect.finish();
            if (mHeaderDrawable != null) {
                mHeaderDrawable.draw(canvas);
            }
        }
    }
}
