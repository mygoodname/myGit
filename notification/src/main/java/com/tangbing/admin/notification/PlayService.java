package com.tangbing.admin.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.util.Log;
import static android.support.v4.app.NotificationCompat.PRIORITY_DEFAULT;

/**
 * $desc$
 * 作   者 :彭付林
 * 邮   箱 :pengfl@kingchannels.com
 * 日   期 :2019/4/26
 * 描   述 :个人信息
 */
public class PlayService extends Service {

    private static final String TAG = "==PlayService==";
    public static final String ACTION_PLAY = "com.kingchannels.kezhiphone.ui.read.audio.PlayService.ACTION_PLAY";
    public static final String ACTION_EXIT = "com.kingchannels.kezhiphone.ui.read.audio.PlayService.ACTION_EXIT";
    public static final String PLAY_LAST = "com.kingchannels.kezhiphone.ui.read.audio.PlayService.PLAY_LAST";
    public static final String SHOW_WINDWO = "com.kingchannels.kezhiphone.ui.read.audio.PlayService.SHOW_WINDWO";
    public static final String DELETE_WINDWO = "com.kingchannels.kezhiphone.ui.read.audio.PlayService.DELETE_WINDWO";
    public static final String STARTSOTP_WINDWO = "com.kingchannels.kezhiphone.ui.read.audio.PlayService.STARTSOTP_WINDWO";
    public static final String STOPPLAY="com.kingchannels.kezhiphone.ui.read.audio.PlayService.STOP";

    private String broadcastAction = "com.haha.playServiceAction";
    private int mPlayingPosition;
    private MediaPlayer mMediaPlayer;
    private PowerManager.WakeLock mWakeLock = null;//获取设备电源锁，防止锁屏后服务被停止
    private NotificationBroadcast notificationBroadcast;
    private Notification notification;//通知栏
    private RemoteViews remoteViews;//通知栏布局
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;
    String id = "my_channel_02";
    String name = "有声书播放service";
    private PendingIntent audioPendingIntent;
    public static final int PRE = 1;
    public static final int PAUSE = 2;
    public static final int NEXT = 3;
    public static final int EXIT = 4;
    public static final int CANCLE = 5;

    private Intent intent;
    // 单线程池
    private ExecutorService mProgressUpdatedListener = Executors
            .newSingleThreadExecutor();


    public class PlayBinder extends Binder {
        public PlayService getService() {
            return PlayService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
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
//        sendNotification();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT);
            //数字是随便写的“40”，
            notificationManager.createNotificationChannel(notificationChannel);
            //其中的2，是也随便写的，正式项目也是随便写
            startForeground(2,notification);
            android.util.Log.e("sunning", "onStartCommand");
        } else
            startForeground(2, notification);
    }

    private void sendNotification(){
        Notification notification = builder.build();

        notification.icon=R.mipmap.ic_launcher;

        notification.when=System.currentTimeMillis();

        //跳转意图

        Intent intent = new Intent(this,SettingsActivity.class);

        //建立一个RemoteView的布局，并通过RemoteView加载这个布局

        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.view_notify);

        //为remoteView设置图片和文本

        remoteViews.setTextViewText(R.id.message,"第一条通知");

        remoteViews.setImageViewResource(R.id.image,R.mipmap.ic_launcher_round);

        //设置PendingIntent

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        //为id为openActivity的view设置单击事件

        remoteViews.setOnClickPendingIntent(R.id.audioPlay,pendingIntent);

        //将RemoteView作为Notification的布局

        notification.contentView =getContentView();

        //将pendingIntent作为Notification的intent，这样当点击其他部分时，也能实现跳转

//        notification.contentIntent=pendingIntent;

        notificationManager.notify(1,notification);

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void setNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setWhen(System.currentTimeMillis())
//                .setContentIntent(getDefaultIntent())
                    .setCustomBigContentView(new RemoteViews(getPackageName(), R.layout.view_notify) )
//                .setAutoCancel(false)
                    .setChannelId(id)
                    .setCustomContentView(getContentView())
                    .build();
        } else {
            notification=builder.build();

            notification.icon=R.mipmap.ic_launcher;

            notification.when=System.currentTimeMillis();

            //跳转意图

            Intent intent = new Intent(this,SettingsActivity.class);

            //建立一个RemoteView的布局，并通过RemoteView加载这个布局

            RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.view_notify);

            //为remoteView设置图片和文本

            remoteViews.setTextViewText(R.id.message,"第一条通知");

            remoteViews.setImageViewResource(R.id.image,R.mipmap.ic_launcher_round);

            //设置PendingIntent

            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

            //为id为openActivity的view设置单击事件

            remoteViews.setOnClickPendingIntent(R.id.audioPlay,pendingIntent);

            //将RemoteView作为Notification的布局

            notification.contentView =getContentView();
        }
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

}
