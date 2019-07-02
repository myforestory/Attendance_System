package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.halfbit.pinnedsection.PinnedSectionListView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView lvMainInfo;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    public static final int ITEM = 0;
    public static final int SECTION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_main);

        drawerAction();

        setMainInfo();
        updateTitleTime();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.back_home) {
            // Handle the camera action
        } else if (id == R.id.log_out) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, LoginActivity.class);
            Bundle bundle = new Bundle();

            intent.putExtras(bundle);
            startActivity(intent);
        }
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_tools) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void drawerAction() {
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        mTitle = mDrawerTitle = getTitle();

        mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar
                , R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            public void onDrawerClosed(View view) {
                setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                TextView userNameTextView = (TextView) findViewById(R.id.user_name);
                TextView userEmailTextView = (TextView) findViewById(R.id.user_email);
                SpannableString nameString = new SpannableString("曾鄢畇");
                nameString.setSpan(new UnderlineSpan(), 0, nameString.length(), 0);
                userNameTextView.setText(nameString);
                SpannableString emailString = new SpannableString("sou@marcopolos.co.jp");
                userEmailTextView.setText(emailString);
                setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }

        };
        drawer.addDrawerListener(mDrawerToggle);

    }

    private void setMainInfo() {
        String dataTime;
        int year, month;

        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH)+1;


        getMainInfoData("2019", "06");
    }

    public void getMainInfoData(final String year,final String month) {
        String loginToken, userId, getDataUrl;
        Bundle bundle = this.getIntent().getExtras();
        String dataTime = year + "-" + month;

        loginToken = bundle.getString("loginToken");
        userId = bundle.getString("userId");
        getDataUrl = "http://kintai-api.ios.tokyo/user/" + userId + "/attendance/" + dataTime;

        Log.d("loginToken", loginToken);

        if(!loginToken.isEmpty() && !userId.isEmpty()) {
            OkHttpGetPost.getAsycHttp(getDataUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    Log.d("onFailure",  e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ArrayList <MainInfo>mainInfoList;
                    String responseBody, todayDate;

                    responseBody = response.body().string();
                    try {
                        mainInfoList = new ArrayList<>();

                        Log.d("ggggggggggg", responseBody);
                        JSONObject jsonObject = new JSONObject(responseBody);
                        JSONArray monthJsonArray = jsonObject.getJSONObject("data").getJSONArray("days");
                        todayDate = jsonObject.getJSONObject("data").getJSONObject("today").getString("date");
                        Log.d("todayDate", todayDate);
                        String date, day, start, end, worked_time, remarks;

                        mainInfoList.add(new MainInfo(SECTION,
                                "",
                                "1",
                                year + "年" + 6 + "月",
                                "",
                                "",
                                "",
                                ""));

                        for (int i = 0; i < monthJsonArray.length(); i++) {
                            JSONObject row = monthJsonArray.getJSONObject(i);
                            HashMap<String, String> mainInfoMap = mainInfoDataFormat(row);
                            date = mainInfoMap.get("date");
                            day = mainInfoMap.get("day");
                            start = mainInfoMap.get("start");
                            end = mainInfoMap.get("end");
                            worked_time = mainInfoMap.get("worked_time");
                            remarks = mainInfoMap.get("remarks");

                            mainInfoList.add(new MainInfo(ITEM,
                                    "",
                                    date,
                                    day,
                                    start,
                                    end,
                                    worked_time,
                                    remarks));
                        }

                        findViews(mainInfoList);
                    } catch (JSONException e) {

                    }
                }
            }, null, "Authorization", loginToken);
        }

        // MainInfo儲存ListView各列對應的資料
//        mainInfoList = new ArrayList<>();
//        mainInfoList.add(new MainInfo(SECTION,"", "1", "2019年6月", "", "", "", ""));
//
//        mainInfoList.add(new MainInfo(ITEM,"", "1", "土", "9:30", "18:30", "8:00", "アンドロイド10倍CFDホーム画面ロードのバグ修正"));
    }

    public void findViews(final ArrayList <MainInfo>mainInfoList) {
        lvMainInfo = (ListView) findViewById(R.id.lvMainInfo);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lvMainInfo.setAdapter(new MainInfoAdapter(MainActivity.this, mainInfoList));
            }
        });


        lvMainInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, UpdateActivity.class);
                Bundle bundle = new Bundle();

                MainInfo mainInfo = (MainInfo) parent.getItemAtPosition(position);
                String date = mainInfo.getDate();
                bundle.putString("date", date);

                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

    }

    public class MainInfoAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
        private LayoutInflater layoutInflater;
        private ArrayList<MainInfo> mainInfoList;
        private Context context;

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
            this.context=context;

            layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
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
            //置顶的栏目
            return viewType == SECTION;
        }

        @Override
        public long getItemId(int position) {
            return Integer.valueOf(mainInfoList.get(position).getDate());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            //对listview进行缓存
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
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.tvDate.setText(maininfo.getDate());
                if (maininfo.getDate().length() == 2) {
                    holder.tvDate.setTextSize(getResources().getDimension(R.dimen.dp_12));
                } else {
                    holder.tvDate.setTextSize(getResources().getDimension(R.dimen.dp_16));
                }
                holder.tvDay.setText(maininfo.getDay());
                if ("土".equals(maininfo.getDay()) || "日".equals(maininfo.getDay()) || "土".equals(maininfo.getDay())) {
                    convertView.setBackgroundColor(getResources().getColor(R.color.colorMainLightGrey));
                } else {
                    convertView.setBackgroundColor(getResources().getColor(R.color.colorMainWhite));
                }

                holder.tvStart.setText(maininfo.getStart());
                holder.tvEnd.setText(maininfo.getEnd());
                holder.tvWorked_time.setText(maininfo.getWorked_time());
                holder.tvRemarks.setText(maininfo.getRemarks());
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
        }
    }

    public void updateTitleTime() {
        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setTitleCurrentTime();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public void setTitleCurrentTime() {
        TextView titleDate = (TextView) findViewById(R.id.titleDate);
        TextView titleTime = (TextView) findViewById(R.id.titleTime);

        Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT-11:00"), Locale.US);

        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        String formattedTime = tf.format(c.getTime());

        titleDate.setText(formattedDate);
        titleTime.setText(formattedTime);
    }

    public HashMap<String, String> mainInfoDataFormat(JSONObject row) {
        HashMap<String, String> mainInfoMap = new HashMap<String, String>();
        String date="", day="", start="", end="", worked_time="", remarks="";
        String[] startArr, endArr;
        try {
            date = row.getString("date");
            day = "";
            if (!date.isEmpty()) {
                day = getJpWeekday (date);
                int len = date.length();
                date = (date.charAt(len - 2) == '0') ? date.substring(len - 1, len) : date.substring(len - 2, len);
            }
            startArr = row.getString("start").split("\\s+");
            if (startArr.length > 1) {
                start = startArr[1].substring(0, 5);
            }
            endArr = row.getString("end").split("\\s+");
            if (endArr.length > 1) {
                end = endArr[1].substring(0, 5);
            }
            worked_time = row.getString("worked_time");
            if (worked_time.isEmpty() || "null".equals(worked_time)){
                worked_time = "0";
            }
            remarks = row.getString("remarks");
            if (remarks.isEmpty() || "null".equals(remarks)) {
                remarks = "";
            }
            mainInfoMap.put("date", date);
            mainInfoMap.put("day", day);
            mainInfoMap.put("start", start);
            mainInfoMap.put("end", end);
            mainInfoMap.put("worked_time", worked_time);
            mainInfoMap.put("remarks", remarks);
        } catch (JSONException e) {

        }
        return mainInfoMap;
    }

    public String getJpWeekday (String infoDate) {
        String weekday = "";
        int year, month, date;
        year = Integer.valueOf(infoDate.split("-")[0]);
        month = Integer.valueOf(infoDate.split("-")[1]) - 1;
        date = Integer.valueOf(infoDate.split("-")[2]);

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, date);

        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                weekday = "日";
                break;
            case Calendar.MONDAY:
                weekday = "月";
                break;
            case Calendar.TUESDAY:
                weekday = "火";
                break;
            case Calendar.WEDNESDAY:
                weekday = "水";
                break;
            case Calendar.THURSDAY:
                weekday = "木";
                break;
            case Calendar.FRIDAY:
                weekday = "金";
                break;
            case Calendar.SATURDAY:
                weekday = "土";
                break;
        }
        return weekday;
    }
}
