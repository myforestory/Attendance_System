package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.util.Calendar;

public class LongRunningService extends Service {


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        //读者可以修改此处的Minutes从而改变提醒间隔时间
//        //此处是设置每隔90分钟启动一次
//        //这是90分钟的毫秒数
//        int Minutes = 90*60*1000;
//        //SystemClock.elapsedRealtime()表示1970年1月1日0点至今所经历的时间
//        long triggerAtTime = SystemClock.elapsedRealtime() + Minutes;
//        //此处设置开启AlarmReceiver这个Service
//        Intent i = new Intent(this, AlarmReceiver.class);
//        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
//        //ELAPSED_REALTIME_WAKEUP表示让定时任务的出发时间从系统开机算起，并且会唤醒CPU。


        Calendar calendar = Calendar.getInstance();


        calendar.set(Calendar.DAY_OF_MONTH, 18);
        calendar.set(Calendar.HOUR_OF_DAY, 16);
        calendar.set(Calendar.MINUTE, 25);
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pi = PendingIntent.getService(this, 0,
                new Intent(this, AlarmReceiver.class),PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        Log.d("mYear", String.valueOf(mYear));
        Log.d("mMonth", String.valueOf(mMonth));
        Log.d("mDay", String.valueOf(mDay));
        Log.d("hour", String.valueOf(hour));
        Log.d("minute", String.valueOf(minute));
        Log.d("second", String.valueOf(second));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.cancel(pi);
    }
}