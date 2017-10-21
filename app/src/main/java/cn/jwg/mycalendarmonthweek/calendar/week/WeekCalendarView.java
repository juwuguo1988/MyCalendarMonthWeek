package cn.jwg.mycalendarmonthweek.calendar.week;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import cn.jwg.mycalendarmonthweek.R;
import cn.jwg.mycalendarmonthweek.calendar.OnCalendarClickListener;
import cn.jwg.mycalendarmonthweek.utils.AppConstant;
import cn.jwg.mycalendarmonthweek.utils.TimeUtils;
import java.util.Calendar;


/**
 * Created by Jimmy on 2016/10/7 0007.
 */
public class WeekCalendarView extends ViewPager implements OnWeekClickListener {

    private static final String TAG = "WeekCalendarView";
    private OnCalendarClickListener mOnCalendarClickListener;
    private WeekAdapter mWeekAdapter;

    public WeekCalendarView(Context context) {
        this(context, null);
    }

    public WeekCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        addOnPageChangeListener(mOnPageChangeListener);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        initWeekAdapter(context, context.obtainStyledAttributes(attrs, R.styleable.WeekCalendarView));
    }

    private void initWeekAdapter(Context context, TypedArray array) {
        mWeekAdapter = new WeekAdapter(context, array, this);
        setAdapter(mWeekAdapter);
        setCurrentItem(mWeekAdapter.getWeekCount() / 2, false);
    }

    @Override
    public void onClickDate(int year, int month, int day) {
        if (mOnCalendarClickListener != null) {
            try {
                AppConstant.CALENDAR_SELECT_WEEK = TimeUtils
                        .dayForWeek(year + String.format("-%02d", month + 1) + String.format("-%02d", day));
            } catch (Exception e) {
                e.printStackTrace();
            }

            mOnCalendarClickListener.onClickDate(year, month, day);
        }
    }

    private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(final int position) {
            WeekView weekView = mWeekAdapter.getViews().get(position);
            if (weekView != null) {
                if (mOnCalendarClickListener != null) {
                    Calendar ca = TimeUtils
                            .getYMDCalendar(AppConstant.CALENDAR_SELECT_WEEK, weekView.getSelectYear(), weekView.getSelectMonth(),
                                    weekView.getSelectDay());

                    Log.e(TAG, "========CALENDAR_SELECT_WEEK=======>" + AppConstant.CALENDAR_SELECT_WEEK);

                    Log.e(TAG, "========getSelectDay=======>" + weekView.getSelectDay());

                    Log.e(TAG, "========get(Calendar.DAY_OF_MONTH)=======>" + ca.get(Calendar.DAY_OF_MONTH));

                    weekView.setSelectYearMonth(ca.get(Calendar.YEAR), +(ca.get(Calendar.MONTH)), ca.get(Calendar.DAY_OF_MONTH));
                    mOnCalendarClickListener.onPageChange(weekView.getSelectYear(), weekView.getSelectMonth(), weekView.getSelectDay());
                }
                weekView.clickThisWeek(weekView.getSelectYear(), weekView.getSelectMonth(), weekView.getSelectDay());
            } else {
                WeekCalendarView.this.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onPageSelected(position);
                    }
                }, 50);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * 设置点击日期监听
     */
    public void setOnCalendarClickListener(OnCalendarClickListener onCalendarClickListener) {
        mOnCalendarClickListener = onCalendarClickListener;
    }

    public SparseArray<WeekView> getWeekViews() {
        return mWeekAdapter.getViews();
    }

    public WeekAdapter getWeekAdapter() {
        return mWeekAdapter;
    }

    public WeekView getCurrentWeekView() {
        return getWeekViews().get(getCurrentItem());
    }

}
