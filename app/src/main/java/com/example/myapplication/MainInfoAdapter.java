package com.example.myapplication;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.halfbit.pinnedsection.PinnedSectionListView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class MainInfoAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<MainInfo> mainInfoList;
    private android.content.Context context;
    public static final int ITEM = 0;
    public static final int SECTION = 1;
    TextView txTitleDate;

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
                holder.tvDate.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)context.getResources().getDimension(R.dimen.dp_30));
            } else {
                holder.tvDate.setTextSize(TypedValue.COMPLEX_UNIT_PX, (int)context.getResources().getDimension(R.dimen.dp_36));
            }
            WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;// 螢幕寬度(畫素)
            int height= dm.heightPixels; // 螢幕高度(畫素)
            float density = dm.density;//螢幕密度(0.75 / 1.0 / 1.5)
            int densityDpi = dm.densityDpi;//螢幕密度dpi(120 / 160 / 240)
            //螢幕寬度演算法:螢幕寬度(畫素)/螢幕密度
            int screenWidth = (int) (width/density);//螢幕寬度(dp)
            int screenHeight = (int)(height/density);//螢幕高度(dp)
            Log.e("Aloha", screenWidth + "=====" + screenHeight);


            if(width < 500) {
                RelativeLayout.LayoutParams paramTvDate = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                paramTvDate.setMargins((int)context.getResources().getDimension(R.dimen.dp_5),0,0,0);
                holder.tvDate.setLayoutParams(paramTvDate);
            }
            holder.tvDay.setText(maininfo.getDay());
            if ("土".equals(maininfo.getDay()) || "日".equals(maininfo.getDay()) || "休".equals(maininfo.getDay())) {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.colorMainLightGrey));
            } else {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.colorMainWhite));
            }
            holder.tvStart.setText(maininfo.getStart());
            if (!"".equals(maininfo.getStart()) && DateUtils.isAfterTime(maininfo.getStart(), "10:00")) {
                holder.tvStart.setTextColor(context.getResources().getColor(R.color.colorMainRed));
            } else {
                holder.tvStart.setTextColor(context.getResources().getColor(R.color.colorMainBlack));
            }
            holder.tvEnd.setText(maininfo.getEnd());
            if ("勤務中".equals(maininfo.getEnd())) {
                holder.tvEnd.setTextColor(context.getResources().getColor(R.color.colorMainRed));
            } else {
                holder.tvEnd.setTextColor(context.getResources().getColor(R.color.colorMainBlack));
            }
            holder.tvWorked_time.setText(maininfo.getWorked_time()+"H");
            if(width < 500) {
                holder.tvWorked_time.setTextSize(TypedValue.COMPLEX_UNIT_PX,(int)context.getResources().getDimension(R.dimen.dp_18));
            }
            holder.tvRemarks.setText(maininfo.getRemarks());
            if(screenWidth < 370) {
                holder.tvRemarks.getLayoutParams().width = (int)context.getResources().getDimension(R.dimen.dp_240);
            } else if (screenWidth >= 370 && screenWidth < 420) {
                holder.tvRemarks.getLayoutParams().width = (int)context.getResources().getDimension(R.dimen.dp_240);
            } else if (width >= 420) {
                holder.tvRemarks.getLayoutParams().width = (int)context.getResources().getDimension(R.dimen.dp_300);
            }
            if(!"".equals(maininfo.getStart()) && (!"".equals(maininfo.getEnd()) && !"勤務中".equals(maininfo.getEnd()))) {
                holder.imgStr.setBackgroundResource(R.drawable.ic_played);
                holder.imgEnd.setBackgroundResource(R.drawable.ic_power_setting);
            } else if (!"".equals(maininfo.getStart()) && ("".equals(maininfo.getEnd()) || "勤務中".equals(maininfo.getEnd()))) {
                holder.imgStr.setBackgroundResource(R.drawable.ic_played);
                holder.imgEnd.setBackgroundResource(0);
                holder.tvWorked_time.setText("");
            } else {
                holder.imgStr.setBackgroundResource(0);
                holder.imgEnd.setBackgroundResource(0);
                holder.tvWorked_time.setText("");
            }
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
