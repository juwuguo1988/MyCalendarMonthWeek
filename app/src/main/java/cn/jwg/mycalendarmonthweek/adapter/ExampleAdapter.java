package cn.jwg.mycalendarmonthweek.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.jwg.mycalendarmonthweek.R;


public class ExampleAdapter extends BaseAdapter {

    private String[] titles;
    private Context mContext;

    public ExampleAdapter(Context mContext) {
        this.mContext = mContext;
        titles = mContext.getResources().getStringArray(R.array.titles);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return titles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewContentHolder vcholder;
        if (convertView == null) {
            vcholder = new ViewContentHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item, null);
            vcholder.text_view = (TextView) convertView.findViewById(R.id.text_view);
            convertView.setTag(vcholder);
        } else {
            vcholder = (ViewContentHolder) convertView.getTag();
        }
        vcholder.text_view.setText(titles[position]);
        return convertView;
    }

    static class ViewContentHolder {

        TextView text_view;
    }

}
