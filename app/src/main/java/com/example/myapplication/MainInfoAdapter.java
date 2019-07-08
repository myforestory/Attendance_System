package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.halfbit.pinnedsection.PinnedSectionListView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class MainInfoAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<MainInfo> mainInfoList;
    private android.content.Context context;
    public static final int ITEM = 0;
    public static final int SECTION = 1;

    public ArrayList<MainInfo> getList() {
        return mainInfoList;
    }

    public void setList(ArrayList<MainInfo> list) {
        if (list != null) {
            this.mainInfoList = list;
        } else {
            mainInfoList = new ArrayList<MainInfo>();
        }
    }
    public MainInfoAdapter(Context context, ArrayList<MainInfo> mainInfoList) {
        super();
        this.mainInfoList = mainInfoList;
        this.context = context;

        layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MainInfo getItem(int position) {
        return mainInfoList.get(position);
    }

    @Override
    public int getCount() {
        return mainInfoList.size();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return (getItem(position)).getType();
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == SECTION;
    }

    @Override
    public long getItemId(int position) {
        return Integer.valueOf(mainInfoList.get(position).getDate());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        MainInfo maininfo = mainInfoList.get(position);
        if (maininfo.getType() == ITEM) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.listview_maininfo, parent, false);
                holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
                holder.tvDay = (TextView) convertView.findViewById(R.id.tvDay);
                holder.tvStart = (TextView) convertView.findViewById(R.id.tvStart);
                holder.tvEnd = (TextView) convertView.findViewById(R.id.tvEnd);
                holder.tvWorked_time = (TextView) convertView.findViewById(R.id.tvWorked_time);
                holder.tvRemarks = (TextView) convertView.findViewById(R.id.tvRemarks);
                holder.imgStr = (ImageView) convertView.findViewById(R.id.imgStr);
                holder.imgEnd = (ImageView) convertView.findViewById(R.id.imgEnd);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvDate.setText(maininfo.getDate());
            if ("1".equals(maininfo.getDate())){
            }
            if (maininfo.getDate().length() == 2) {
                holder.tvDate.setTextSize(context.getResources().getDimension(R.dimen.dp_12));
            } else {
                holder.tvDate.setTextSize(context.getResources().getDimension(R.dimen.dp_16));
            }
            holder.tvDay.setText(maininfo.getDay());
            if ("土".equals(maininfo.getDay()) || "日".equals(maininfo.getDay()) || "休".equals(maininfo.getDay())) {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.colorMainLightGrey));
            } else {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.colorMainWhite));
            }
            holder.tvStart.setText(maininfo.getStart());
            holder.tvEnd.setText(maininfo.getEnd());
            holder.tvWorked_time.setText(maininfo.getWorked_time());
            holder.tvRemarks.setText(maininfo.getRemarks());
            holder.imgStr.setBackgroundResource(R.drawable.ic_play);
            holder.imgEnd.setBackgroundResource(R.drawable.ic_power_setting);
        } else {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.listview_title, parent, false);
                holder.tvDate = (TextView) convertView.findViewById(R.id.tvTitle);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvDate.setText(maininfo.getDay());
        }

        return convertView;
    }

    private class ViewHolder {
        TextView tvDate, tvDay, tvStart, tvEnd, tvWorked_time, tvRemarks;
        ImageView imgStr, imgEnd;
    }
}