package cn.jwg.mycalendarmonthweek.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * Created by hasee on 2016/7/15.
 */
public class CalendarListView extends ListView {
    public CalendarListView(Context context) {
        super(context);
    }

    public CalendarListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CalendarListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isScrollTop() {
        return computeVerticalScrollOffset() == 0;
    }

    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        if (getOnFocusChangeListener() != null) {
            getOnFocusChangeListener().onFocusChange(child, false);
            getOnFocusChangeListener().onFocusChange(focused, true);
        }
    }

}
