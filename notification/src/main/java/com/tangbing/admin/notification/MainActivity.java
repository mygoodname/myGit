package com.tangbing.admin.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;
import static android.support.v4.app.NotificationCompat.PRIORITY_DEFAULT;

public class MainActivity extends AppCompatActivity {
    private String broadcastAction = "com.haha.playServiceAction";
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    NotificationBroadcast notificationBroadcast;
    String id = "my_channel_02";
    String name = "有声书播放service";
    private PendingIntent audioPendingIntent;
    private Notification notification;//通知栏
    private RemoteViews remoteViews;//通知栏布局
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      /*  IntentFilter intentFilter=new IntentFilter(broadcastAction);
        notificationBroadcast=new NotificationBroadcast();
        registerReceiver(notificationBroadcast,intentFilter);*/
    }
    public void startNotification(View view){
//        startNotification();
        sendNotification();
    }
    public void startNotificationService(View view){
        Intent intent=new Intent(this,PlayService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }
    public void sendBroadcast(View view){
//        startNotification();
        Intent intent=new Intent(broadcastAction);
        intent.putExtra("playStatus","-1");
        sendBroadcast(intent);
    }
    private void startNotification(){
        IntentFilter intentFilter=new IntentFilter(broadcastAction);
        notificationBroadcast=new NotificationBroadcast();
        registerReceiver(notificationBroadcast,intentFilter);
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(this, id);
        } else {
            builder = new NotificationCompat.Builder(this);
            builder.setPriority(PRIORITY_DEFAULT);
        }
        setNotification();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT);
            //数字是随便写的“40”，
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
    private void setNotification() {
        notification = builder.setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
//                .setContentIntent(getDefaultIntent())
                .setCustomBigContentView(new RemoteViews(getPackageName(), R.layout.view_notify) )
//                .setAutoCancel(false)
                .setChannelId(id)
//                .setCustomContentView(getContentView())
                .build();
        notification.contentView=getContentView();
        notificationManager.notify(2, notification);
    }
    private RemoteViews getContentView() {
        remoteViews = new RemoteViews(getPackageName(), R.layout.view_notify);
        remoteViews.setTextViewText(R.id.bookName,"正在播放：这本书好好看看哦哦哦哦哦哦哦哦");
        Intent intent=new Intent(broadcastAction);
        intent.putExtra("playStatus","-1");
        intent.setClass(this,NotificationBroadcast.class);
        PendingIntent lastChapter = PendingIntent.getBroadcast(this,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.lastChapter,lastChapter);//点击的id，点击事件
        intent.putExtra("playStatus","0");
        PendingIntent audioPlay = PendingIntent.getBroadcast(this,2,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.audioPlay,audioPlay);//点击的id，点击事件
        intent.putExtra("playStatus","1");
        PendingIntent nextPlay = PendingIntent.getBroadcast(this,3,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.nextChapter,nextPlay);//点击的id，点击事件
        intent.putExtra("playStatus","2");
        PendingIntent stop = PendingIntent.getBroadcast(this,4,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.stop,stop);//点击的id，点击事件
        return remoteViews;
    }

    private void sendNotification(){

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification =new Notification();

        notification.icon=R.mipmap.ic_launcher;

        notification.when=System.currentTimeMillis();

        notification.priority=PRIORITY_DEFAULT;
        //跳转意图

//        Intent intent = new Intent(this,SettingsActivity.class);
        Intent intent=new Intent(this,NotificationBroadcast.class);
        intent.setAction(broadcastAction);
        intent.putExtra("playStatus","-1");
        //建立一个RemoteView的布局，并通过RemoteView加载这个布局

        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.view_notify);

        //为remoteView设置图片和文本

        remoteViews.setTextViewText(R.id.message,"第一条通知");

        remoteViews.setImageViewResource(R.id.image,R.mipmap.ic_launcher_round);

        //设置PendingIntent

//        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        //为id为openActivity的view设置单击事件

        remoteViews.setOnClickPendingIntent(R.id.audioPlay,pendingIntent);

        //将RemoteView作为Notification的布局

        notification.contentView =getContentView();

        //将pendingIntent作为Notification的intent，这样当点击其他部分时，也能实现跳转

        notification.contentIntent=pendingIntent;

        notificationManager.notify(1,notification);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消绑定
//        unregisterReceiver(notificationBroadcast);
    }

}
