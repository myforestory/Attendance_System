package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class UpdateActivity extends AppCompatActivity {

    private TextView tvUpdateTitle;
    private ImageView ivBackToMain;
    private ImageView ivSave;
    private TextView etStrTime_update;
    private TextView etEndTime_update;
    private TextView etDescription_update;

    private ListView lvTeam;
    private ListView lvMainInfo;
    private List<MainInfo> mainInfoList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;


    static int hour, min;

    TextView txtdate, txttime, txttime2;
    TextView btntimepicker, btndatepicker, btntimepicker2;
    Button ggg;

    java.sql.Time timeValue;
    SimpleDateFormat format;
    Calendar c;
    int year, month, day;


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
        ivBackToMain = (ImageView) findViewById(R.id.ivSave);
        ivSave = (ImageView) findViewById(R.id.ivBackToMain);
        etStrTime_update = (TextView) findViewById(R.id.etStrTime_update);
        etEndTime_update = (TextView) findViewById(R.id.etEndTime_update);
        etDescription_update = (TextView) findViewById(R.id.etDescription_update);

        ivBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateActivity.this.finish();
            }
        });

        ivSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateActivity.this.finish();
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

    private void setTimepicker() {
        c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        //txtdate = (TextView) findViewById(R.id.etEndTime_update);
        txttime = (TextView) findViewById(R.id.etStrTime_update);
        txttime2 = (TextView) findViewById(R.id.etEndTime_update);

//        btndatepicker =(Button)findViewById(R.id.ggg);
        btntimepicker =(TextView) findViewById(R.id.etStrTime_update);
        btntimepicker2 =(TextView) findViewById(R.id.etEndTime_update);

//        btndatepicker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(UpdateActivity.this, "YYYYYY", Toast.LENGTH_SHORT).show();
//                // Get Current Date
//
//                DatePickerDialog dd = new DatePickerDialog(UpdateActivity.this,
//                        new DatePickerDialog.OnDateSetListener() {
//
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
//
//                                try {
//                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//                                    String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
//                                    Date date = formatter.parse(dateInString);
//
//                                    txtdate.setText(formatter.format(date).toString());
//
//                                    formatter = new SimpleDateFormat("dd/MMM/yyyy");
//
//                                    txtdate.setText(txtdate.getText().toString()+"\n"+formatter.format(date).toString());
//
//                                    formatter = new SimpleDateFormat("dd-MM-yyyy");
//
//                                    txtdate.setText(txtdate.getText().toString()+"\n"+formatter.format(date).toString());
//
//                                    formatter = new SimpleDateFormat("dd.MMM.yyyy");
//
//                                    txtdate.setText(txtdate.getText().toString()+"\n"+formatter.format(date).toString());
//
//                                } catch (Exception ex) {
//
//                                }
//
//
//                            }
//                        }, year, month, day);
//                dd.show();
//            }
//        });
        btntimepicker.setOnClickListener(new View.OnClickListener() {
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
                                    txttime.setText(String.valueOf(timeValue));
                                    String amPm = hourOfDay % 12 + ":" + minute + " " + ((hourOfDay >= 12) ? "PM" : "AM");
                                    txttime.setText(amPm + "\n" + String.valueOf(timeValue));
                                } catch (Exception ex) {
                                    txttime.setText(ex.getMessage().toString());
                                }
                            }
                        },
                        hour, min,
                        DateFormat.is24HourFormat(UpdateActivity.this)
                );
                td.show();
            }
        });

        btntimepicker2.setOnClickListener(new View.OnClickListener() {
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
                                    txttime2.setText(String.valueOf(timeValue));
                                    String amPm = hourOfDay % 12 + ":" + minute + " " + ((hourOfDay >= 12) ? "PM" : "AM");
                                    txttime2.setText(amPm + "\n" + String.valueOf(timeValue));
                                } catch (Exception ex) {
                                    txttime2.setText(ex.getMessage().toString());
                                }
                            }
                        },
                        hour, min,
                        DateFormat.is24HourFormat(UpdateActivity.this)
                );
                td.show();
            }
        });
    }


}
