package com.kai.video.view.other;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kai.video.R;
import com.kai.video.adapter.VideoItemAdapter;

import java.util.HashMap;

public class GridSpacesItemDecoration extends RecyclerView.ItemDecoration {
    private static final HashMap<String, String> dic = new HashMap<>();
    static {
        dic.put("tencent", "腾讯视频");
        dic.put("iqiyi", "爱奇艺");
        dic.put("mgtv", "芒果TV");
        dic.put("bilibili", "哔哩哔哩");
        dic.put("bilibili1", "哔哩哔哩日漫");
        dic.put("youku", "优酷视频");
    }
    private static final HashMap<String, String> adic = new HashMap<>();
    static {
        adic.put("tv", "剧集");
        adic.put("film", "电影");
        adic.put("cartoon", "动漫");
        adic.put("zy", "综艺");
    }
    private static final String TAG = "SectionDecoration";
    private final TextPaint textPaint;
    private final Paint paint;
    private final int topGap;
    private Paint.FontMetrics fontMetrics;
    private final VideoItemAdapter adapter;
    private final String action;

    public GridSpacesItemDecoration(Context context, VideoItemAdapter adapter, String action) {
        this.adapter = adapter;
        Resources res = context.getResources();
        this.action = action;
        paint = new Paint();
        paint.setColor(res.getColor(R.color.colorPrimaryDark));

        textPaint = new TextPaint();
        textPaint.setTypeface(Typeface.DEFAULT);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(30);
        textPaint.setColor(Color.WHITE);
        textPaint.getFontMetrics(fontMetrics);
        textPaint.setTextAlign(Paint.Align.LEFT);
        fontMetrics = new Paint.FontMetrics();
        topGap = 60;
    }
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        if (adapter.getItemViewType(pos) == 2)
            outRect.top = topGap;
        else {
            outRect.top = 0;
        }
    }
    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            String header = adapter.getHeader(position);
            if (header != null) {
                float top = view.getTop() - topGap;
                float bottom = view.getTop();
                c.drawRect(left, top, right, bottom, paint);//绘制矩形
                c.drawText(dic.get(header) + "\t----\t最热" + adic.get(action), left + 15, bottom - 15, textPaint);//绘制文本
            }
        }
    }

}