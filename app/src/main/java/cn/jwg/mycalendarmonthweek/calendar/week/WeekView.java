package cn.jwg.mycalendarmonthweek.calendar.week;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import cn.jwg.mycalendarmonthweek.R;
import cn.jwg.mycalendarmonthweek.bean.CalendarBean;
import cn.jwg.mycalendarmonthweek.calendar.CalendarUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.joda.time.DateTime;

/**
 * Created by Jimmy on 2016/10/7 0007.
 */
public class WeekView extends View {

    private static final int NUM_COLUMNS = 7;
    private Paint mPaint,mSelectBgPaint,mCirclePaint;
    private int mNormalDayColor;
    private int mSelectDayColor;
    private int mSelectBGColor;
    private int mSelectBGTodayColor;
    private int mCurrentDayColor;
    private int mCurrYear, mCurrMonth, mCurrDay;
    private int mSelYear, mSelMonth, mSelDay;
    private int mColumnSize, mRowSize, mSelectCircleSize;
    private int mDaySize;
    private int mCircleRadius = 6;
    private boolean mIsShowHint;
    private DateTime mStartDate;
    private DisplayMetrics mDisplayMetrics;
    private OnWeekClickListener mOnWeekClickListener;
    private GestureDetector mGestureDetector;

    public WeekView(Context context, DateTime dateTime) {
        this(context, null, dateTime);
    }

    public WeekView(Context context, TypedArray array, DateTime dateTime) {
        this(context, array, null, dateTime);
    }

    public WeekView(Context context, TypedArray array, AttributeSet attrs, DateTime dateTime) {
        this(context, array, attrs, 0, dateTime);
    }

    public WeekView(Context context, TypedArray array, AttributeSet attrs, int defStyleAttr, DateTime dateTime) {
        super(context, attrs, defStyleAttr);
        initAttrs(array, dateTime);
        initPaint();
        initWeek();
        initGestureDetector();
    }

    private void initAttrs(TypedArray array, DateTime dateTime) {
        if (array != null) {
            mSelectDayColor = array.getColor(R.styleable.WeekCalendarView_week_selected_text_color, Color.parseColor("#FFFFFF"));
            mSelectBGColor = array.getColor(R.styleable.WeekCalendarView_week_selected_circle_color, Color.parseColor("#E0E0E0"));
            mSelectBGTodayColor = array
                    .getColor(R.styleable.WeekCalendarView_week_selected_circle_today_color, Color.parseColor("#E14434"));
            mNormalDayColor = array.getColor(R.styleable.WeekCalendarView_week_normal_text_color, Color.parseColor("#333333"));
            mCurrentDayColor = array.getColor(R.styleable.WeekCalendarView_week_today_text_color, Color.parseColor("#333333"));
            mDaySize = array.getInteger(R.styleable.WeekCalendarView_week_day_text_size, 13);
            mIsShowHint = array.getBoolean(R.styleable.WeekCalendarView_week_show_task_hint, true);
        } else {
            mSelectDayColor = Color.parseColor("#FFFFFF");
            mSelectBGColor = Color.parseColor("#E0E0E0");
            mSelectBGTodayColor = Color.parseColor("#2BBA60");
            mNormalDayColor = Color.parseColor("#333333");
            mCurrentDayColor = Color.parseColor("#333333");
            mDaySize = 13;
            mIsShowHint = true;
        }
        mStartDate = dateTime;
    }

    private void initPaint() {
        mDisplayMetrics = getResources().getDisplayMetrics();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mDaySize * mDisplayMetrics.scaledDensity);

        mSelectBgPaint = new Paint();
        mSelectBgPaint.setAntiAlias(true);
        mSelectBgPaint.setColor(mSelectBGColor);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mSelectDayColor);
    }

    private void initWeek() {
        Calendar calendar = Calendar.getInstance();
        mCurrYear = calendar.get(Calendar.YEAR);
        mCurrMonth = calendar.get(Calendar.MONTH);
        mCurrDay = calendar.get(Calendar.DATE);
        DateTime endDate = mStartDate.plusDays(7);
        if (mStartDate.getMillis() <= System.currentTimeMillis() && endDate.getMillis() > System.currentTimeMillis()) {
            if (mStartDate.getMonthOfYear() != endDate.getMonthOfYear()) {
                if (mCurrDay < mStartDate.getDayOfMonth()) {
                    setSelectYearMonth(mStartDate.getYear(), endDate.getMonthOfYear() - 1, mCurrDay);
                } else {
                    setSelectYearMonth(mStartDate.getYear(), mStartDate.getMonthOfYear() - 1, mCurrDay);
                }
            } else {
                setSelectYearMonth(mStartDate.getYear(), mStartDate.getMonthOfYear() - 1, mCurrDay);
            }
        } else {
            setSelectYearMonth(mStartDate.getYear(), mStartDate.getMonthOfYear() - 1, mStartDate.getDayOfMonth());
        }
    }

    private void initGestureDetector() {
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                doClickAction((int) e.getX(), (int) e.getY());
                return true;
            }
        });
    }

    public void setSelectYearMonth(int year, int month, int day) {
        mSelYear = year;
        mSelMonth = month;
        mSelDay = day;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = mDisplayMetrics.densityDpi * 200;
        }
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = mDisplayMetrics.densityDpi * 300;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initSize();
        int selected = drawThisWeek(canvas);
        drawHintCircle(canvas, selected);
    }

    private void initSize() {
        mColumnSize = getWidth() / NUM_COLUMNS;
        mRowSize = getHeight();
        mSelectCircleSize = (int) (mColumnSize / 3.2);
        while (mSelectCircleSize > mRowSize / 2) {
            mSelectCircleSize = (int) (mSelectCircleSize / 1.3);
        }
    }

    private int drawThisWeek(Canvas canvas) {
        int selected = 0;
        for (int i = 0; i < 7; i++) {
            DateTime date = mStartDate.plusDays(i);
            int day = date.getDayOfMonth();
            String dayString = String.valueOf(day);
            int startX = (int) (mColumnSize * i + (mColumnSize - mPaint.measureText(dayString)) / 2);
            int startY = (int) (mRowSize / 2 - (mPaint.ascent() + mPaint.descent()) / 2);
            if (day == mSelDay) {
                int startRecX = mColumnSize * i;
                int endRecX = startRecX + mColumnSize;
                if (date.getYear() == mCurrYear && date.getMonthOfYear() - 1 == mCurrMonth && day == mCurrDay) {
                    mSelectBgPaint.setColor(mSelectBGTodayColor);
                } else {
                    List<CalendarBean> mDataList = CalendarUtils.getInstance(getContext()).getTaskHints(mSelYear, mSelMonth);
                    for (CalendarBean sl : mDataList) {
                        if (sl.getDay() == day) {
                            mSelectBgPaint.setColor(sl.getColor());
                            break;
                        } else {
                            mSelectBgPaint.setColor(mSelectBGColor);
                        }
                    }
                }
                canvas.drawCircle((startRecX + endRecX) / 2, mRowSize / 2, mSelectCircleSize, mSelectBgPaint);
            }
            if (day == mSelDay) {
                selected = i;
                mPaint.setColor(mSelectDayColor);
            } else if (date.getYear() == mCurrYear && date.getMonthOfYear() - 1 == mCurrMonth && day == mCurrDay && day != mSelDay
                    && mCurrYear == mSelYear) {
                mPaint.setColor(mCurrentDayColor);
            } else {
                mPaint.setColor(mNormalDayColor);
            }
            canvas.drawText(dayString, startX, startY, mPaint);
        }
        return selected;
    }

    /**
     * 绘制圆点提示
     */
    private void drawHintCircle(Canvas canvas, int selected) {
        if (mIsShowHint) {
            int startMonth = mStartDate.getMonthOfYear();
            int endMonth = mStartDate.plusDays(7).getMonthOfYear();
            int startDay = mStartDate.getDayOfMonth();
            if (startMonth == endMonth) {
                List<CalendarBean> mDataList = CalendarUtils.getInstance(getContext())
                        .getTaskHints(mStartDate.getYear(), mStartDate.getMonthOfYear() - 1);
                List<Integer> hints = new ArrayList<>();
                for (CalendarBean sl : mDataList) {
                    hints.add(sl.getDay());
                }

                for (int i = 0; i < 7; i++) {
                    for (CalendarBean sl : mDataList) {
                        if (sl.getDay() == startDay + i) {
                            mCirclePaint.setColor(sl.getColor());
                            break;
                        }
                    }

                    if (i == selected) {
                        mCirclePaint.setColor(mSelectDayColor);
                    }
                    drawHintCircle(hints, startDay + i, i, canvas);
                }
            } else {
                List<CalendarBean> mDataList = CalendarUtils.getInstance(getContext())
                        .getTaskHints(mStartDate.getYear(), mStartDate.getMonthOfYear() - 1);
                List<Integer> hints = new ArrayList<>();
                for (CalendarBean sl : mDataList) {
                    hints.add(sl.getDay());
                }
                List<CalendarBean> mNextDataList = CalendarUtils.getInstance(getContext())
                        .getTaskHints(mStartDate.getYear(), mStartDate.getMonthOfYear());
                List<Integer> nextHints = new ArrayList<>();
                for (CalendarBean sl : mNextDataList) {
                    nextHints.add(sl.getDay());
                }
                for (int i = 0; i < 7; i++) {
                    DateTime date = mStartDate.plusDays(i);

                    int month = date.getMonthOfYear();

                    if (month == startMonth) {
                        for (CalendarBean sl : mDataList) {
                            if (sl.getDay() == date.getDayOfMonth()) {
                                mCirclePaint.setColor(sl.getColor());
                                break;
                            }
                        }
                    } else {
                        for (CalendarBean sl : mNextDataList) {
                            if (sl.getDay() == date.getDayOfMonth()) {
                                mCirclePaint.setColor(sl.getColor());
                                break;
                            }
                        }
                    }

                    if (i == selected) {
                        mCirclePaint.setColor(mSelectDayColor);
                    }

                    if (month == startMonth) {
                        drawHintCircle(hints, date.getDayOfMonth(), i, canvas);
                    } else {
                        drawHintCircle(nextHints, date.getDayOfMonth(), i, canvas);
                    }
                }
            }
        }
    }

    private void drawHintCircle(List<Integer> hints, int day, int col, Canvas canvas) {
        if (!hints.contains(day)) {
            return;
        }
        float circleX = (float) (mColumnSize * col + mColumnSize * 0.5);
        float circleY = (float) (mRowSize * 0.75);
        canvas.drawCircle(circleX, circleY, mCircleRadius, mCirclePaint);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    private void doClickAction(int x, int y) {
        if (y > getHeight()) {
            return;
        }
        int column = x / mColumnSize;
        column = Math.min(column, 6);
        DateTime date = mStartDate.plusDays(column);
        clickThisWeek(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth());
    }

    public void clickThisWeek(int year, int month, int day) {
        if (mOnWeekClickListener != null) {
            mOnWeekClickListener.onClickDate(year, month, day);
        }
        setSelectYearMonth(year, month, day);
        invalidate();
    }

    public void setOnWeekClickListener(OnWeekClickListener onWeekClickListener) {
        mOnWeekClickListener = onWeekClickListener;
    }

    public DateTime getStartDate() {
        return mStartDate;
    }

    public DateTime getEndDate() {
        return mStartDate.plusDays(6);
    }

    /**
     * 获取当前选择年
     */
    public int getSelectYear() {
        return mSelYear;
    }

    /**
     * 获取当前选择月
     */
    public int getSelectMonth() {
        return mSelMonth;
    }


    /**
     * 获取当前选择日
     */
    public int getSelectDay() {
        return this.mSelDay;
    }

    /**
     * 添加多个圆点提示
     */
    public void addTaskHints(List<CalendarBean> hints) {
        if (mIsShowHint) {
            CalendarUtils.getInstance(getContext()).addTaskHints(mSelYear, mSelMonth, hints);
            invalidate();
        }
    }

    /**
     * 删除多个圆点提示
     */
    public void removeTaskHints(List<CalendarBean> hints) {
        if (mIsShowHint) {
            CalendarUtils.getInstance(getContext()).removeTaskHints(mSelYear, mSelMonth, hints);
            invalidate();
        }
    }

    /**
     * 添加一个圆点提示
     */
    public void addTaskHint(CalendarBean day) {
        if (mIsShowHint) {
            if (CalendarUtils.getInstance(getContext()).addTaskHint(mSelYear, mSelMonth, day)) {
                invalidate();
            }
        }
    }

    /**
     * 删除一个圆点提示
     */
    public void removeTaskHint(CalendarBean day) {
        if (mIsShowHint) {
            if (CalendarUtils.getInstance(getContext()).removeTaskHint(mSelYear, mSelMonth, day)) {
                invalidate();
            }
        }
    }

}
