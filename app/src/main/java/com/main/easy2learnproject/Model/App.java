package com.main.easy2learnproject.Model;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.main.easy2learnproject.Control.FireBase;

public class App extends Application {
    @Override

    public void onCreate() {
        Log.d("pttt", "onCreate: APP ");
        super.onCreate();
        FirebaseApp.initializeApp(this);
        FireBase.init(this);
    }
}
