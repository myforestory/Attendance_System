package com.example.myapplication;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView lvTeam;
    private List<Team> teamList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        initTeams();
        findViews();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        TextView textView = (TextView) findViewById(R.id.aloha);
        SpannableString content = new SpannableString("Content");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        textView.setText(content);
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
        if (id == R.id.action_settings) {
            return true;
        }

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

    private void initTeams() {
        // teamList儲存ListView各列對應的資料
        teamList = new ArrayList<>();
        teamList.add(new Team(1, 4, "巴爾的摩金鶯"));
        teamList.add(new Team(2, 4, "芝加哥白襪"));
        teamList.add(new Team(3, 4, "洛杉磯天使"));
        teamList.add(new Team(4, 4, "波士頓紅襪"));
        teamList.add(new Team(5, 4, "克里夫蘭印地安人"));
        teamList.add(new Team(6, 4, "奧克蘭運動家"));
        teamList.add(new Team(7, 4, "紐約洋基"));
        teamList.add(new Team(8, 4, "底特律老虎"));
        teamList.add(new Team(9, 4, "西雅圖水手"));
        teamList.add(new Team(10, 4, "坦帕灣光芒"));
    }

    public void findViews() {
        lvTeam = (ListView) findViewById(R.id.lvTeam);
        // 呼叫setAdapter()方法設定掌控ListView內容物的Adapter
        lvTeam.setAdapter(new TeamAdapter(this, teamList));
        // 使用者點選ListView上的任一列時會呼叫onItemClick()方法並傳遞view參數，
        // view參數代表被點選的View元件，也就是TeamAdapter.getView()的convertView參數，
        // 在此為list_item.xml內的LinearLayout元件
        lvTeam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Team team = (Team) parent.getItemAtPosition(position);
                String info = team.getId() + " " + team.getName();
                Toast.makeText(MainActivity.this, info, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class TeamAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private List<Team> teamList;

        public TeamAdapter(Context context, List<Team> teamList) {
            this.teamList = teamList;
            // 呼叫getSystemService()方法取得LayoutInflater物件，
            // 可以透過該物件取得指定layout檔案內容後初始化成View物件
            layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            // 一樣做法
//            layoutInflater = LayoutInflater.from(context);

        }

        @Override
        // ListView總列數
        public int getCount() {
            return teamList.size();
        }


        @Override
        // 回傳該列物件
        public Object getItem(int position) {
            return teamList.get(position);
        }

        @Override
        // 較無實際應用
        public long getItemId(int position) {
            return teamList.get(position).getId();
        }

        @Override
        // 依照position回傳該列資料所需呈現的UI畫面(View)
        public View getView(int position, View convertView, ViewGroup parent) {
            // 一個convertView就是ListView一列資料的畫面，
            // 因為每一列資料外觀都一樣，只有資料值不同，所以載入相同layout檔案，
            // 第一次還未載入layout，所以必須呼叫layoutInflater.inflate()載入layout檔案並指派給convertView
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.listview_team, parent, false);
                holder.ivLogo = (TextView) convertView.findViewById(R.id.ivLogo);
                holder.tvId = (TextView) convertView.findViewById(R.id.tvId);
                holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // 依照position取得teamList內的team物件
            Team team = teamList.get(position);
//            holder.ivLogo.setText(team.getLogo());
            holder.tvId.setText(Integer.toString(team.getId()));
            holder.tvName.setText(team.getName());

            return convertView;
        }

        private class ViewHolder {
            TextView ivLogo;
            TextView tvName, tvId;
        }
    }
}
