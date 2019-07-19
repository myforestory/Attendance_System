package com.example.myapplication;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        //设置通知内容并在onReceive()这个函数执行时开启
//        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification=new Notification(R.drawable.ic_launcher,"用电脑时间过长了！白痴！"
//                ,System.currentTimeMillis());
//        notification.setLatestEventInfo(context, "快去休息！！！",
//                "一定保护眼睛,不然遗传给孩子，老婆跟别人跑啊。", null);
//        notification.defaults = Notification.DEFAULT_ALL;
//        manager.notify(1, notification);
        SharedPreferences status = context.getSharedPreferences("status", MODE_PRIVATE);
        Button btUpdate = ((Activity)context).findViewById(R.id.btUpdate);
        TextView txTitleDate = ((Activity)context).findViewById(R.id.txTitleDate);
        TextView txTitleTime = ((Activity)context).findViewById(R.id.txTitleTime);

        status.edit().putInt("statusCode", 1).commit();
        btUpdate.setVisibility (View.VISIBLE);
        txTitleDate.setPadding(0,0, (int) context.getResources().getDimension(R.dimen.dp_100),0);
        txTitleTime.setPadding(0,0, (int) context.getResources().getDimension(R.dimen.dp_100),0);
        btUpdate.setBackground(ContextCompat.getDrawable(context, R.drawable.style_button_red));
        btUpdate.setText("出勤");


        Log.d("Aloha", "2222222222222");

        Intent i = new Intent(context, LongRunningService.class);
        context.startService(i);
    }
}
