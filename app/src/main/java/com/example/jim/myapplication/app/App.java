package com.example.jim.myapplication.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by jim on 2015/8/6.
 */
public class App extends Application{
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
