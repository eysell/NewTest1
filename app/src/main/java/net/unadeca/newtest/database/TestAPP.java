package net.unadeca.newtest.database;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

public class TestAPP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
    }
}
