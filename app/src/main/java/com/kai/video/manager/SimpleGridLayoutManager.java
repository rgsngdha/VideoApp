package com.kai.video.manager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 自定义GridLayoutManager，修改RecyelerView焦点乱跳的BUG
 * Created by Danxingxi on 2016/4/1.
 */
public class SimpleGridLayoutManager extends GridLayoutManager {

    /**
     * Constructor used when layout manager is set in XML by RecyclerView attribute
     * "layoutManager". If spanCount is not specified in the XML, it defaults to a
     * single column.
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     * @attr ref android.support.v7.recyclerview.R.styleable#RecyclerView_spanCount
     */
    public SimpleGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Creates a vertical GridLayoutManager
     *
     * @param context  Current context, will be used to access resources.
     * @param spanCount The number of columns in the grid
     */
    public SimpleGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    /**
     * @param context    Current context, will be used to access resources.
     * @param spanCount   The number of columns or rows in the grid
     * @param orientation  Layout orientation. Should be {@link #HORIZONTAL} or {@link
     *           #VERTICAL}.
     * @param reverseLayout When set to true, layouts from end to start.
     */

    public SimpleGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout, RecyclerView recyclerView) {
        super(context, spanCount, orientation, reverseLayout);

    }

    /**
     * Return the current number of child views attached to the parent RecyclerView.
     * This does not include child views that were temporarily detached and/or scrapped.
     *
     * @return Number of attached children
     */
    @Override
    public int getChildCount() {
        return super.getChildCount();
    }

    /**
     * Return the child view at the given index
     *
     * @param index Index of child to return
     * @return Child view at index
     */
    @Override
    public View getChildAt(int index) {
        return super.getChildAt(index);
    }

    /**
     * Returns the number of items in the adapter bound to the parent RecyclerView.
     * @return The number of items in the bound adapter
     */
    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    /**
     * Returns the item View which has or contains focus.
     *
     * @return A direct child of RecyclerView which has focus or contains the focused child.
     */
    @Override
    public View getFocusedChild() {
        return super.getFocusedChild();
    }

    /**
     * Returns the adapter position of the item represented by the given View. This does not
     * contain any adapter changes that might have happened after the last layout.
     *
     * @param view The view to query
     * @return The adapter position of the item which is rendered by this View.
     */
    @Override
    public int getPosition(@NonNull View view) {
        return super.getPosition(view);
    }

    /**
     * 获取列数
     * @return
     */
    @Override
    public int getSpanCount() {
        return super.getSpanCount();
    }

    /**
     * Called when searching for a focusable view in the given direction has failed for the current content of the RecyclerView.
     * This is the LayoutManager's opportunity to populate views in the given direction to fulfill the request if it can.
     * The LayoutManager should attach and return the view to be focused. The default implementation returns null.
     * 防止当recyclerview上下滚动的时候焦点乱跳
     * @param focused
     * @param focusDirection
     * @param recycler
     * @param state
     * @return
     */
    @Override
    public View onFocusSearchFailed(View focused, int focusDirection, RecyclerView.Recycler recycler, RecyclerView.State state) {

        // Need to be called in order to layout new row/column
        View nextFocus = super.onFocusSearchFailed(focused, focusDirection, recycler, state);

        if (nextFocus == null) {
            return null;
        }
        int fromPos = 0;
        try {
            fromPos = getPosition(focused);
        }catch (Exception e){
            e.printStackTrace();
        }

        int nextPos = getNextViewPos(fromPos, focusDirection);

        return findViewByPosition(nextPos);

    }

    /**
     * Manually detect next view to focus.
     *
     * @param fromPos from what position start to seek.
     * @param direction in what direction start to seek. Your regular {@code View.FOCUS_*}.
     * @return adapter position of next view to focus. May be equal to {@code fromPos}.
     */
    protected int getNextViewPos(int fromPos, int direction) {
        int offset = calcOffsetToNextView(direction);

        if (hitBorder(fromPos, offset)) {
            return fromPos;
        }

        return fromPos + offset;
    }

    /**
     * Calculates position offset.
     *
     * @param direction regular {@code View.FOCUS_*}.
     * @return position offset according to {@code direction}.
     */
    protected int calcOffsetToNextView(int direction) {
        int spanCount = getSpanCount();
        int orientation = getOrientation();

        if (orientation == VERTICAL) {
            switch (direction) {
                case View.FOCUS_DOWN:
                    return spanCount;
                case View.FOCUS_UP:
                    return -spanCount;
                case View.FOCUS_RIGHT:
                    return 1;
                case View.FOCUS_LEFT:
                    return -1;
            }
        } else if (orientation == HORIZONTAL) {
            switch (direction) {
                case View.FOCUS_DOWN:
                    return 1;
                case View.FOCUS_UP:
                    return -1;
                case View.FOCUS_RIGHT:
                    return spanCount;
                case View.FOCUS_LEFT:
                    return -spanCount;
            }
        }

        return 0;
    }

    /**
     * Checks if we hit borders.
     *
     * @param from from what position.
     * @param offset offset to new position.
     * @return {@code true} if we hit border.
     */
    private boolean hitBorder(int from, int offset) {
        int spanCount = getSpanCount();

        if (Math.abs(offset) == 1) {
            int spanIndex = from % spanCount;
            int newSpanIndex = spanIndex + offset;
            return newSpanIndex < 0 || newSpanIndex >= spanCount;
        } else {
            int newPos = from + offset;
            return newPos < 0 && newPos >= spanCount;
        }
    }
    public void scrolloffset(int offset){
        int current = getPosition(getFocusedChild());
        if (offset < 0 && current + getSpanCount() < getItemCount()){
            offsetChildrenVertical(offset);
        }
        else if (offset > 0 && current - getSpanCount() > 0){
            offsetChildrenVertical(offset);
        }
    }
    @Nullable
    @Override
    public View onInterceptFocusSearch(@NonNull View focused, int direction) {
        // 这里处理上下键，如果处理左右键，使用View.FOCUS_LEFT和View.FOCUS_RIGHT
        if (direction == View.FOCUS_DOWN || direction == View.FOCUS_UP) {
            if (direction == View.FOCUS_DOWN) {
                scrolloffset(-20);
            }
            if (direction == View.FOCUS_UP){
                scrolloffset(20);

            }

        }

        return super.onInterceptFocusSearch(focused, direction);
    }

}