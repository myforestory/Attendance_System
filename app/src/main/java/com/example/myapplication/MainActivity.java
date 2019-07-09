package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import de.halfbit.pinnedsection.PinnedSectionListView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView lvMainInfo;
    private TextView txTitleDate;
    private TextView txTitleTime;
    private Button btUpdate;
    private MainInfoAdapter mainInfoAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private SwipeRefreshLayout laySwipe;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    public static final int ITEM = 0;
    public static final int SECTION = 1;
    ArrayList <MainInfo> mainInfoList;
    ArrayList <MainInfo> mainInfoList2;
    Bundle bundle;

    private final static String logoutUrl = "http://kintai-api.ios.tokyo/user/logout";
    String loginToken, userId, getDataUrl, name, email, finalUpdateTime, updateUrl;
    String updateYear, updateMonth, updateDate, todayYMD;
    SharedPreferences logoutKey, status;
    int nowYear, nowMonth, nowDate;
    int statusCode, todayPosition, updatePosition;
    Boolean isInitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = this.getIntent().getExtras();
        name = bundle.getString("name");
        email = bundle.getString("email");
        mainInfoList = new ArrayList<>();
        mainInfoList2 = new ArrayList<>();
        setTitle("");
        setContentView(R.layout.activity_main);
        status = getSharedPreferences("status", MODE_PRIVATE);
        isInitial = true;

        drawerAction();
        setInitialMainInfo();
        setBtUpdateAction();
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.back_home) {
            // test
            btUpdate = findViewById(R.id.btUpdate);
            status.edit()
                    .putInt("statusCode", 1)
                    .commit();
            setBtUpdateStyle();


        } else if (id == R.id.log_out) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, LoginActivity.class);
            Bundle bundle = new Bundle();

            OkHttpGetPost.postAsycHttp(logoutUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    logoutKey = getSharedPreferences("logoutKey", MODE_PRIVATE);
                    logoutKey.edit()
                            .putString("userName", "")
                            .putString("password", "")
                            .putString("loginToken", "").commit();
                }
            }, null,"Authorization", loginToken);

            intent.putExtras(bundle);
            startActivity(intent);
        }

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
                SpannableString nameString = new SpannableString(name);
                nameString.setSpan(new UnderlineSpan(), 0, nameString.length(), 0);
                userNameTextView.setText(nameString);
                SpannableString emailString = new SpannableString(email);
                userEmailTextView.setText(emailString);
                setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        drawer.addDrawerListener(mDrawerToggle);
    }

    private void setInitialMainInfo() {
        String dataTime;

        Calendar c = Calendar.getInstance();
//        nowYear = c.get(Calendar.YEAR);
//        nowMonth = c.get(Calendar.MONTH)+1;
//        todayDate = c.get(Calendar.DATE);

        nowYear = 2019;
        updateYear = String.valueOf(nowYear);
        nowMonth = 6;
        updateMonth = String.valueOf(nowMonth);
        nowDate = 10;
        updateDate = String.valueOf(nowDate);
        todayYMD = nowYear + "-" + nowMonth + "-" + nowDate;
        todayPosition = nowDate;
        getMainInfoData(String.valueOf(nowYear), String.valueOf(nowMonth));
    }

    public void getMainInfoData(final String year, final String month) {
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
                    String responseBody, todayDate;
                    responseBody = response.body().string();
                    mainInfoList2.clear();
                    try {
                        Log.d("responseBody", responseBody);
                        JSONObject jsonObject = new JSONObject(responseBody);
                        JSONArray monthJsonArray = jsonObject.getJSONObject("data").getJSONArray("days");
                        todayDate = jsonObject.getJSONObject("data").getJSONObject("today").getString("date");
                        Log.d("todayDate", todayDate);
                        String date, day, start, end, worked_time, remarks;

                        mainInfoList2.add(new MainInfo(SECTION,
                                "",
                                "1",
                                year + "年" + month + "月",
                                "",
                                "",
                                "",
                                "",
                                month,
                                year));

                        for (int i = 0; i < monthJsonArray.length(); i++) {
                            JSONObject row = monthJsonArray.getJSONObject(i);
                            HashMap<String, String> mainInfoMap = mainInfoDataFormat(row);
                            date = mainInfoMap.get("date");
                            day = mainInfoMap.get("day");
                            start = mainInfoMap.get("start");
                            end = mainInfoMap.get("end");
                            worked_time = mainInfoMap.get("worked_time");
                            remarks = mainInfoMap.get("remarks");

                            mainInfoList2.add(new MainInfo(ITEM,
                                    "",
                                    date,
                                    day,
                                    start,
                                    end,
                                    worked_time,
                                    remarks,
                                    month,
                                    year));
                        }
                        mainInfoList.addAll(0,mainInfoList2);
                        updatePosition = mainInfoList2.size();
                        if (isInitial) {
                            todayPosition = todayPosition;
                        } else {
                            todayPosition = todayPosition + mainInfoList2.size();
                        }
                        Log.d("todayPosition", String.valueOf(todayPosition));
                        findViews(mainInfoList);
                    } catch (JSONException e) {}
                }
            }, null, "Authorization", loginToken);
        }
    }

    public void findViews(final ArrayList <MainInfo>mainInfoList) {
        lvMainInfo = (ListView) findViewById(R.id.lvMainInfo);
        lvMainInfo.setOnScrollListener(onListScroll);
        laySwipe = (SwipeRefreshLayout) findViewById(R.id.laySwipe);
        laySwipe.setOnRefreshListener(onSwipeToRefresh);
        laySwipe.setColorSchemeResources(
        android.R.color.holo_red_light,
        android.R.color.holo_blue_light,
        android.R.color.holo_green_light,
        android.R.color.holo_orange_light);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainInfoAdapter = new MainInfoAdapter(getApplicationContext(), mainInfoList);
                lvMainInfo.setAdapter(mainInfoAdapter);
            }
        });
        lvMainInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, UpdateActivity.class);
                Bundle bundle = new Bundle();
                MainInfo mainInfo = (MainInfo) parent.getItemAtPosition(position);

                if(mainInfo.getType() == ITEM) {
                    bundle.putString("date", mainInfo.getDate());
                    bundle.putString("day", mainInfo.getDay());
                    bundle.putString("end", mainInfo.getEnd());
                    bundle.putString("month", mainInfo.getMonth());
                    bundle.putString("remarks", mainInfo.getRemarks());
                    bundle.putString("start", mainInfo.getStart());
                    bundle.putString("worked_time", mainInfo.getWorked_time());
                    bundle.putString("year", mainInfo.getYear());
                    bundle.putString("loginToken", loginToken);
                    bundle.putString("userId", userId);
                    bundle.putString("name", name);
                    bundle.putString("email", email);

                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(isInitial) {
                    lvMainInfo.setSelectionFromTop(todayPosition, 90);
                } else {
                    lvMainInfo.setSelectionFromTop(updatePosition, 90);
                }
            }
        });
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
        txTitleDate = (TextView) findViewById(R.id.txTitleDate);
        txTitleTime = (TextView) findViewById(R.id.txTitleTime);

        Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT-11:00"), Locale.US);

        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        String formattedTime = tf.format(c.getTime());

        txTitleDate.setText(formattedDate);
        txTitleTime.setText(formattedTime);
    }

    public HashMap<String, String> mainInfoDataFormat(JSONObject row) {
        HashMap<String, String> mainInfoMap = new HashMap<String, String>();
        String date="", day="", start="", end="", worked_time="", remarks="";
        String[] startArr, endArr;
        try {
            date = row.getString("date");
            day = "";
            if (!date.isEmpty()) {
                day = DateUtils.getJpWeekday (date);
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
        } catch (JSONException e) {}
        return mainInfoMap;
    }

    private void setBtUpdateAction() {
        btUpdate = findViewById(R.id.btUpdate);
        checkTodayTime();
        setBtUpdateStyle();

        btUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
//               String todayDate = dateFormat.format(date);
//               String todayDateTime = dateTimeFormat.format(date);
                Date date = new Date();

                ///////test/////////
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String todayTime = dateTimeFormat.format(date);

                String todayDate = nowYear+"-"+nowMonth+"-"+nowDate;
                String todayDateTime = "2019-05-10 " + DateUtils.timeFormattedRoundDown(sdf.format(cal.getTime()));
//                String todayDateTime = "2019-05-10 " + timeFormattedRoundDown("21:50");
                ///////test/////////

                HashMap<String, String> updateMap;

                switch (statusCode) {
                    case 1:
                        String start = todayDateTime;
                        updateMap = new HashMap<String, String>();
                        updateMap.put("start", start);

                        updateUrl = "http://kintai-api.ios.tokyo/user/" + userId + "/date/" + todayDate;
                        Log.d("updateUrl", updateUrl);;

                        OkHttpGetPost.postAsycHttp(updateUrl, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                Log.d("onFailure",  e.toString());
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseBody;
                                responseBody = response.body().string();
                                Log.d("responseBody", responseBody);
                                mainInfoList.clear();
                                getMainInfoData(String.valueOf(nowYear), String.valueOf(nowMonth));
                            }
                        }, updateMap, "Authorization", loginToken);

                        status.edit()
                                .putInt("statusCode", 2)
                                .putString("finalUpdateTime", todayDate)
                                .commit();
                        setBtUpdateStyle();
                        backInitInfo();
                        break;
                    case 2:
                        String end = todayDateTime;
                        updateMap = new HashMap<String, String>();
                        updateMap.put("end", end);

                        updateUrl = "http://kintai-api.ios.tokyo/user/" + userId + "/date/" + todayDate;
                        Log.d("updateUrl", updateUrl);

                        OkHttpGetPost.postAsycHttp(updateUrl, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                Log.d("onFailure",  e.toString());
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseBody;
                                responseBody = response.body().string();
                                Log.d("responseBody", responseBody);
                                mainInfoList.clear();
                                getMainInfoData(String.valueOf(nowYear), String.valueOf(nowMonth));
                            }
                        }, updateMap, "Authorization", loginToken);
                        status.edit()
                                .putInt("statusCode", 3)
                                .putString("finalUpdateTime", todayDate)
                                .commit();
                        setBtUpdateStyle();
                        backInitInfo();
                        break;
                    case 3:
                        setBtUpdateStyle();
                        backInitInfo();
                        break;
                }
            }
        });
    }

    private void setBtUpdateStyle() {
        txTitleDate = (TextView) findViewById(R.id.txTitleDate);
        txTitleTime = (TextView) findViewById(R.id.txTitleTime);
        statusCode = getSharedPreferences("status", MODE_PRIVATE).getInt("statusCode", 0);
        switch (statusCode) {
            case 1:
                btUpdate.setVisibility (View.VISIBLE);
                txTitleDate.setPadding(0,0, (int) getResources().getDimension(R.dimen.dp_100),0);
                txTitleTime.setPadding(0,0, (int) getResources().getDimension(R.dimen.dp_100),0);
                btUpdate.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.style_button_red));
                btUpdate.setText("出勤");
                break;
            case 2:
                btUpdate.setVisibility (View.VISIBLE);
                txTitleDate.setPadding(0,0, (int) getResources().getDimension(R.dimen.dp_100),0);
                txTitleTime.setPadding(0,0, (int) getResources().getDimension(R.dimen.dp_100),0);
                btUpdate.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.style_button_blue));
                btUpdate.setText("退勤");
                break;
            case 3:
                btUpdate.setVisibility (View.GONE);
                txTitleDate.setPadding(0,0, (int) getResources().getDimension(R.dimen.dp_50),0);
                txTitleTime.setPadding(0,0, (int) getResources().getDimension(R.dimen.dp_50),0);
                break;
        }
    }

    private void checkTodayTime() {
        try {
            Date finalDate, reloadDate, todayDateFormat;

            finalUpdateTime = getSharedPreferences("status", MODE_PRIVATE).getString("finalUpdateTime", "");
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
            finalDate = sdFormat.parse(finalUpdateTime);
            todayDateFormat = sdFormat.parse(todayYMD);
            Calendar c = Calendar.getInstance();
            c.setTime(finalDate);
            c.add(Calendar.DATE, 1);
            reloadDate = c.getTime();
            Log.d("finalUpdateTime", finalUpdateTime);
            Log.d("reloadDate", reloadDate.toString());
            Log.d("nowDate", todayDateFormat.toString());

            Date nowDate = new Date();
            LocalTime startAllowed = LocalTime.of(6, 00);
            LocalTime currentTime = LocalTime.now();

            if (todayDateFormat.after(reloadDate) && currentTime.isAfter(startAllowed)) {
                status.edit().putInt("statusCode", 1).commit();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void titleONClick(View view){
        lvMainInfo.setSelectionFromTop(todayPosition, 90);
    }

    private SwipeRefreshLayout.OnRefreshListener onSwipeToRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            laySwipe.setRefreshing(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    laySwipe.setRefreshing(false);
                    Toast.makeText(getApplicationContext(), "Refresh done!", Toast.LENGTH_SHORT).show();
                    String updateDateArray[] = DateUtils.subMonth(updateYear+"-"+updateMonth+"-"+updateDate);
                    getMainInfoData(updateDateArray[0], updateDateArray[1]);
                    updateYear = updateDateArray[0];
                    updateMonth = updateDateArray[1];
                    isInitial = false;
                }
            }, 300);
        }
    };

    private ArrayAdapter<String> getAdapter(){
        //fake data
        String[] data = new String[20];
        int len = data.length;
        for (int i = 0; i < len; i++) {
            data[i] = Double.toString(Math.random() * 1000);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , data);
        return adapter;
    }

    private AbsListView.OnScrollListener onListScroll = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            if (firstVisibleItem == 0) {
                laySwipe.setEnabled(true);
            }else{
                laySwipe.setEnabled(false);
            }
        }
    };

    private void backInitInfo() {
        isInitial = true;
        updateYear = String.valueOf(nowYear);
        updateMonth = String.valueOf(nowMonth);
        updateDate = String.valueOf(nowDate);
        todayPosition = nowDate;
    }
}
