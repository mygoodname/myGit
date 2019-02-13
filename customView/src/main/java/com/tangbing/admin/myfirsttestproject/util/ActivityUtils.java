package com.tangbing.admin.myfirsttestproject.util;

import android.content.Context;
import android.content.Intent;

/**
 * Created by tangbing on 2019/2/13.
 */

public class ActivityUtils {
    public static void startActivity(Context context, Class<?> cls){
        Intent intent=new Intent(context,cls);
        context.startActivity(intent);
    }
}
