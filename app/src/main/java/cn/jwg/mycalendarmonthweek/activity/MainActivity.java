package cn.jwg.mycalendarmonthweek.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jwg.mycalendarmonthweek.R;
import cn.jwg.mycalendarmonthweek.adapter.ExampleAdapter;
import cn.jwg.mycalendarmonthweek.calendar.OnCalendarClickListener;
import cn.jwg.mycalendarmonthweek.calendar.month.MonthCalendarView;
import cn.jwg.mycalendarmonthweek.calendar.schedule.ScheduleLayout;
import cn.jwg.mycalendarmonthweek.calendar.week.WeekCalendarView;
import cn.jwg.mycalendarmonthweek.common.CalendarListView;
import cn.jwg.mycalendarmonthweek.utils.AppConstant;
import cn.jwg.mycalendarmonthweek.utils.TimeUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Jimmy on 2016/10/11 0011.
 */
public class MainActivity extends Activity implements OnCalendarClickListener {

    private static final String TAG = "MainActivity";
    private ScheduleLayout sl_schedule_layout;
    private RelativeLayout rLNoTask;
    private int mCurrentSelectYear, mCurrentSelectMonth, mCurrentSelectDay;
    private MonthCalendarView mcv_calendar_view;
    private WeekCalendarView wcv_calendar_view;
    private TextView tv_select_day;
    private CalendarListView clv_data_list;
    private String beforeYesdayDate;//前天;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_schedule);
        sl_schedule_layout = (ScheduleLayout) findViewById(R.id.sl_schedule_layout);
        rLNoTask = (RelativeLayout) findViewById(R.id.rlNoTask);
        sl_schedule_layout.setOnCalendarClickListener(this);
        mcv_calendar_view = (MonthCalendarView) findViewById(R.id.mcv_calendar_view);
        wcv_calendar_view = (WeekCalendarView) findViewById(R.id.wcv_calendar_view);
        tv_select_day = (TextView) findViewById(R.id.tv_select_day);
        clv_data_list = (CalendarListView) findViewById(R.id.clv_data_list);

        clv_data_list.setAdapter(new ExampleAdapter(this));
        initDate();
    }

    private void initDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -2);
        sl_schedule_layout.initData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));

        beforeYesdayDate = TimeUtils.getBeforeYesdayDateForRequest();
        try {
            AppConstant.CALENDAR_SELECT_WEEK = TimeUtils.dayForWeek(beforeYesdayDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Integer> normalDateList = new ArrayList<>();
        List<Integer> abnormalDateList = new ArrayList<>();
        List<Integer> partDateList = new ArrayList<>();
        abnormalDateList.add(1);
        normalDateList.add(2);
        abnormalDateList.add(3);
        normalDateList.add(4);
        normalDateList.add(5);
        abnormalDateList.add(8);
        abnormalDateList.add(9);
        abnormalDateList.add(10);
        partDateList.add(13);
        partDateList.add(14);
        partDateList.add(15);
        normalDateList.add(18);
        normalDateList.add(22);
        abnormalDateList.add(26);
        partDateList.add(28);
        sl_schedule_layout.addTaskHints(normalDateList, getResources().getColor(R.color.color_schedule_start));
        sl_schedule_layout.addTaskHints(abnormalDateList, getResources().getColor(R.color.color_schedule_finish));
        sl_schedule_layout.addTaskHints(partDateList, getResources().getColor(R.color.color_schedule_yellow));

        tv_select_day.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sl_schedule_layout.initData(2017, 10, 5);
            }
        });
    }

    @Override
    public void onClickDate(int year, int month, int day) {
        AppConstant.CALENDAR_SELECT_WEEK = TimeUtils.dayForWeek(year + String.format("-%02d", month + 1) + String.format("-%02d", day));
        Log.e(TAG, "======>" + year + "=======" + month + "========" + day);
    }

    @Override
    public void onPageChange(int year, int month, int day) {
    }

    public int getCurrentCalendarPosition() {
        return sl_schedule_layout.getMonthCalendar().getCurrentItem();
    }

}
