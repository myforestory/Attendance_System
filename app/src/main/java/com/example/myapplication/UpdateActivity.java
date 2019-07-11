package com.example.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UpdateActivity extends AppCompatActivity {

    private TextView tvUpdateTitle;
    private ImageView ivBackToMain;
    private ImageView ivSave;
    private TextView etStrTime_update;
    private TextView etEndTime_update;
    private TextView etDescription_update;
    private final static String logoutUrl = "http://kintai-api.ios.tokyo/user/logout";
    SharedPreferences logoutKey;
    static int nowHour, nowMin, nowYear, nowMonth, nowDay, position;
    String loginToken, userId, updateUrl, updateYear, updateMonth, updateDate, name, email;

    java.sql.Time timeValue;
    SimpleDateFormat format;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");

        setContentView(R.layout.activity_update);

        findViews();

        setTimepicker();
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

    public void findViews() {
        tvUpdateTitle = (TextView) findViewById(R.id.tvUpdateTitle);
        ivBackToMain = (ImageView) findViewById(R.id.ivBackToMain);
        ivSave = (ImageView) findViewById(R.id.ivSave);
        etStrTime_update = (EditText) findViewById(R.id.etStrTime_update);
        etEndTime_update = (EditText) findViewById(R.id.etEndTime_update);
        etDescription_update = (EditText)findViewById(R.id.etDescription_update);
        String date, day, end, month, remarks, start, worked_time, year, updateTitle;
        final Bundle bundle = this.getIntent().getExtras();


        date = bundle.getString("date");
        int dateLen = date.length();
        if(dateLen > 1) {
            date = (date.charAt(dateLen - 2) == '0') ? date.substring(dateLen - 1, dateLen) : date.substring(dateLen - 2, dateLen);
        }
        day = bundle.getString("day");
        end = bundle.getString("end");
        month= bundle.getString("month");
        int monthLen = month.length();
        if(monthLen > 1) {
            month = (month.charAt(monthLen - 2) == '0') ? month.substring(monthLen - 1, monthLen) : month.substring(monthLen - 2, monthLen);
        }
        remarks = bundle.getString("remarks");
        start = bundle.getString("start");
        worked_time = bundle.getString("worked_time");
        year = bundle.getString("year");

        updateTitle = year + "年" + month + "月" + date + "日" + "(" + day + ")";
        tvUpdateTitle.setText(updateTitle);
        etStrTime_update.setText(start);
        etEndTime_update.setText(end);
        etDescription_update.setText(remarks);


        ivBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateActivity.this.finish();
            }
        });

        ivSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInfo(bundle);
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
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.listview_maininfo, parent, false);
                holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
                holder.tvDay = (TextView) convertView.findViewById(R.id.tvDay);
//                holder.tvStrTime = (TextView) convertView.findViewById(R.id.tvStrTime);
//                holder.tvEndTime = (TextView) convertView.findViewById(R.id.tvEndTime);
//                holder.tvTotalTime = (TextView) convertView.findViewById(R.id.tvTotalTime);
//                holder.tvDiscretion = (TextView) convertView.findViewById(R.id.tvDiscretion);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            MainInfo maininfo = mainInfoList.get(position);
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
//            holder.tvStrTime.setText(maininfo.getStrTime());
//            holder.tvEndTime.setText(maininfo.getEndTime());
//            holder.tvTotalTime.setText(maininfo.getTotalTime());
//            holder.tvDiscretion.setText(maininfo.getDiscretion());


            return convertView;
        }

        private class ViewHolder {
            TextView tvDate, tvDay, tvStrTime, tvEndTime, tvTotalTime, tvDiscretion;
        }
    }

    private void setTimepicker() {
        c = Calendar.getInstance();
        nowHour = c.get(Calendar.HOUR_OF_DAY);
        nowMin = c.get(Calendar.MINUTE);

        nowYear = c.get(Calendar.YEAR);
        nowMonth = c.get(Calendar.MONTH);
        nowDay = c.get(Calendar.DAY_OF_MONTH);

        setTimeClock(etStrTime_update);
        setTimeClock(etEndTime_update);
    }

    private void setTimeClock(final TextView txttime) {
        txttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog td = new TimePickerDialog(UpdateActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                try {
                                    String dtStart = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                                    format = new SimpleDateFormat("HH:mm");

                                    timeValue = new java.sql.Time(format.parse(dtStart).getTime());
                                    txttime.setText(timeFormattedRoundDown(String.valueOf(timeValue)));
//                                    String amPm = hourOfDay % 12 + ":" + minute + " " + ((hourOfDay >= 12) ? "PM" : "AM");
//                                    txttime.setText(amPm + "\n" + String.valueOf(timeValue));

                                } catch (Exception ex) {
                                    txttime.setText(ex.getMessage().toString());
                                }
                            }
                        },
                        nowHour, nowMin,
                        DateFormat.is24HourFormat(UpdateActivity.this)
                );
                if(!"".equals(txttime.getText().toString())) {
                    String txttimeArray[] = txttime.getText().toString().split(":");
                    td.updateTime(Integer.valueOf(txttimeArray[0]), Integer.valueOf(txttimeArray[1]));
                }
                td.show();
            }
        });
    }

    private String timeFormattedRoundDown(String originalTime) {
        String formattedTime = "";
        String formattedMin = "";
        String hour = originalTime.split(":")[0];
        int originalMin = Integer.valueOf(originalTime.split(":")[1]);

        if (originalMin>=0 && originalMin<15) {
            formattedMin = "00";
        } else if (originalMin>=15 && originalMin<30) {
            formattedMin = "15";
        } else if (originalMin>=30 && originalMin<45) {
            formattedMin = "30";
        } else {
            formattedMin = "45";
        }
        formattedTime = hour + ":" + formattedMin;
        return formattedTime;
    }

    private void updateInfo(Bundle bundle) {
        userId = bundle.getString("userId");
        loginToken = bundle.getString("loginToken");
        updateYear = bundle.getString("year");
        updateMonth = bundle.getString("month");
        updateDate = bundle.getString("date");
        name = bundle.getString("name");
        email = bundle.getString("email");
        position = bundle.getInt("position");

        String start ,end;

        String dataTime = updateYear + "-" + updateMonth + "-" +updateDate;
        if(!"".equals(etStrTime_update.getText().toString())) {
            start = dataTime + " " + etStrTime_update.getText() + ":00";
        } else {
            start = "";
        }

        if(!"".equals(etEndTime_update.getText().toString())) {
            end = dataTime + " " + etEndTime_update.getText() + ":00";
        } else {
            end = "";
        }

        String remarks = etDescription_update.getText().toString();
        HashMap<String, String> updateMap = new HashMap<String, String>();
        updateMap.put("start", start);
        updateMap.put("end", end);
        Log.d("start",  start);
        Log.d("end",  end);
        if("".equals(remarks)){
            remarks = " ";
        }
        updateMap.put("remarks", remarks);

        updateUrl = "http://kintai-api.ios.tokyo/user/" + userId + "/date/" + dataTime;
        Log.d("updateUrl", updateUrl);;

        OkHttpGetPost.postAsycHttp(updateUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(UpdateActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                Log.d("onFailure",  e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody;
                responseBody = response.body().string();
                Log.d("updateResponseBody", responseBody);
                Log.d("update-loginToken",  loginToken);

                try {

                    JSONObject jsonObject = new JSONObject(responseBody);
                    if (isLegal(jsonObject)) {
                        Log.d("responseBody", responseBody);
                        String start = jsonObject.getJSONObject("data").getJSONObject("updated").getString("start").split("\\s+")[1];
                        String end = jsonObject.getJSONObject("data").getJSONObject("updated").getString("end").split("\\s+")[1];
                        start = start.substring(0, start.length()-3);
                        end = end.substring(0, end.length()-3);
                        String remarks = jsonObject.getJSONObject("data").getJSONObject("updated").getString("remarks");

                        Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("loginToken", loginToken);
                        bundle.putString("userId", userId);
                        bundle.putString("name",name);
                        bundle.putString("email", email);
                        bundle.putBoolean("update", true);
                        bundle.putInt("callbackYear", Integer.valueOf(updateYear));
                        bundle.putInt("callbackMonth", Integer.valueOf(updateMonth));
                        bundle.putInt("callbackDate", Integer.valueOf(updateDate));
                        bundle.putInt("position", Integer.valueOf(position));
                        bundle.putString("start", start);
                        bundle.putString("end", end);
                        bundle.putString("remarks", remarks);




                        intent.putExtras(bundle);

                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                } catch (JSONException e) {
                    Log.d("JSONException", e.toString());
                }
            }
        }, updateMap, "Authorization", loginToken);
    }

    private Boolean isLegal (JSONObject jsonObject) {
        Boolean isLegal = false;
        String returnCode = "";
        try {
            returnCode = jsonObject.getString("code");
            if ("E00002".equals(returnCode)) {
                logout();
                final String errorMsg = "重複するアカウントをログインする";
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UpdateActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
                isLegal = false;
            } else if (!returnCode.isEmpty() ) {
                final String errorMsg = jsonObject.getString("message");;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UpdateActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
                isLegal = false;
            } else {
                isLegal = true;
            }
        } catch (JSONException e) {
        }
        return isLegal;
    }

    private void logout() {
        Intent intent = new Intent();
        intent.setClass(UpdateActivity.this, LoginActivity.class);
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


}
