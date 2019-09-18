package com.juaracoding.absensidika.Application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


;

/**
 * Created by Mounzer on 8/22/2017.
 */
//@Database(name = AppController.NAME, version = AppController.VERSION)

public class AppController extends Application {

    public static final String NAME = "ABSENSI_DIKA";
    public static final int VERSION = 1;

    public static final String TAG = AppController.class.getSimpleName();




    public static void setUsername(String username) {
        AppController.username = username;
    }

    public static String username;


    private static AppController singleton;



    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
       // FlowManager.init(new FlowConfig.Builder(this).build());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
        AppController.context = getApplicationContext();



    }

    public static Context getAppContext() {
        return AppController.context;
    }

    public static AppController getInstance(){
        return singleton;
    }

    Handler lochandler;
    public Handler getLocationHandler(){
        return lochandler;
    }
    Handler currhandler;

}
