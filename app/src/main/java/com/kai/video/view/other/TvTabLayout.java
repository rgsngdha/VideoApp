package com.kai.video.view.other;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;

public class TvTabLayout extends TabLayout {
    public TvTabLayout(@NonNull Context context) {
        this(context, null);
    }

    public TvTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TvTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //子View优先处理焦点
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
    }

    @NonNull
    @Override
    public Tab newTab() {
        Tab tab = super.newTab();
        tab.view.setOnFocusChangeListener((view, b) -> {
            if(b) {
                //tab.select();
            }
        });

        return tab;
    }


}
