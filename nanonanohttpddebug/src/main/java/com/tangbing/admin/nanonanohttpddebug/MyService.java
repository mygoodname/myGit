package com.tangbing.admin.nanonanohttpddebug;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Created by tangbing on 2020/4/26.
 * Describe :
 */
public class MyService extends Service {

    private MyWebServer mHttpServer = null;//这个是HttpServer的句柄。
    public Context context;



    @Override
    public void onCreate() {
        //在这里开启HTTP Server。
        try {
            mHttpServer = new MyWebServer(getApplicationContext());
            mHttpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        //在这里关闭HTTP Server
        if(mHttpServer != null)
            mHttpServer.stop();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
