package com.example.myapplication;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView lvTeam;
    private List<Team> teamList;
    private ListView lvMainInfo;
    private List<MainInfo> mainInfoList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("");
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.your_LAYOUT);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
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
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
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
                 // creates call to onPrepareOptionsMenu()
            }

        };
        drawer.addDrawerListener(mDrawerToggle);

        //initTeams();
        initMainInfo();
        findViews();
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

    private void initMainInfo() {
        // MainInfo儲存ListView各列對應的資料
        mainInfoList = new ArrayList<>();
        mainInfoList.add(new MainInfo("1", "土", "9:30", "18:30", "8:00", "アンドロイド10倍CFDホーム画面ロードのバグ修正"));
        mainInfoList.add(new MainInfo("2", "日", "9:30", "18:30", "8:00", "アンドロイド10倍CFDホーム画面ロードのバグ修正"));
        mainInfoList.add(new MainInfo("3", "月", "9:30", "18:30", "8:00", "アンドロイド10倍CFDホーム画面ロードのバグ修正"));
        mainInfoList.add(new MainInfo("4", "火", "9:30", "20:30", "10:00", "アンドロイド10倍CFDホーム画面ロードのバグ修正"));
        mainInfoList.add(new MainInfo("5", "水", "9:30", "19:30", "9:00", "アンドロイド基本知識学習 / アンドロイド10倍CFDホーム画面ロードのバグ修正"));
        mainInfoList.add(new MainInfo("6", "木", "9:30", "18:30", "8:00", "アンドロイド基本知識学習 / アンドロイド10倍CFDホーム画面ロードのバグ修正"));
        mainInfoList.add(new MainInfo("7", "金", "9:30", "18:30", "8:00", "アンドロイド基本知識学習 / アンドロイド10倍CFDホーム画面ロードのバグ修正"));
        mainInfoList.add(new MainInfo("8", "土", "9:30", "18:30", "8:00", "アンドロイド基本知識学習 / アンドロイド10倍CFDホーム画面ロードのバグ修正"));
        mainInfoList.add(new MainInfo("9", "日", "9:30", "20:30", "10:00", "アンドロイド基本知識学習 / アンドロイド10倍CFDホーム画面ロードのバグ修正"));
        mainInfoList.add(new MainInfo("10", "月", "9:30", "18:30", "8:00", "アンドロイド10倍CFDホーム画面ロードのバグ修正"));
        mainInfoList.add(new MainInfo("11", "火", "9:30", "19:00", "8:30", "アンドロイド10倍CFDホーム画面ロードのバグ修正"));
        mainInfoList.add(new MainInfo("12", "水", "9:30", "18:30", "8:00", "アンドロイド10倍CFDホーム画面ロードのバグ修正"));
        mainInfoList.add(new MainInfo("13", "木", "9:30", "18:30", "8:00", "アンドロイド10倍CFDホーム画面ロードのバグ修正"));
        mainInfoList.add(new MainInfo("14", "金", "9:30", "20:30", "10:00", "アンドロイド基本知識学習 / アンドロイド10倍CFDホーム画面ロードのバグ修正"));
        mainInfoList.add(new MainInfo("15", "土", "9:30", "18:30", "8:00", "アンドロイド基本知識学習 / アンドロイド10倍CFDホーム画面ロードのバグ修正"));
    }

    public void findViews() {
        lvMainInfo = (ListView) findViewById(R.id.lvTeam);
        lvMainInfo.setAdapter(new MainInfoAdapter(this, mainInfoList));
        lvMainInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainInfo mainInfo = (MainInfo) parent.getItemAtPosition(position);
                String info = mainInfo.getDate();
                Toast.makeText(MainActivity.this, info, Toast.LENGTH_LONG).show();
            }
        });
    }

    private class MainInfoAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private List<MainInfo> mainInfoList;

	    public MainInfoAdapter(Context context, List<MainInfo> mainInfoList) {
            this.mainInfoList = mainInfoList;

            layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            //same way
            //layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mainInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return mainInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return Integer.valueOf(mainInfoList.get(position).getDate());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
	        ViewHolder holder;
            if(convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.listview_maininfo, parent, false);
                holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
                holder.tvDay = (TextView) convertView.findViewById(R.id.tvDay);
                holder.tvStrTime = (TextView) convertView.findViewById(R.id.tvStrTime);
                holder.tvEndTime = (TextView) convertView.findViewById(R.id.tvEndTime);
                holder.tvTotalTime = (TextView) convertView.findViewById(R.id.tvTotalTime);
                holder.tvDiscretion = (TextView) convertView.findViewById(R.id.tvDiscretion);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            MainInfo maininfo = mainInfoList.get(position);
            holder.tvDate.setText(maininfo.getDate());
            holder.tvDay.setText(maininfo.getDay());
            holder.tvStrTime.setText(maininfo.getStrTime());
            holder.tvEndTime.setText(maininfo.getEndTime());
            holder.tvTotalTime.setText(maininfo.getTotalTime());
            holder.tvDiscretion.setText(maininfo.getDiscretion());

            return convertView;
        }

        private class ViewHolder {
            TextView tvDate, tvDay, tvStrTime, tvEndTime, tvTotalTime, tvDiscretion;
        }
    }

    public void setTitleCurrentTime(){
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

    public void updateTitleTime(){
        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    while(!isInterrupted()){
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setTitleCurrentTime();
                            }
                        });
                    }
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

}
