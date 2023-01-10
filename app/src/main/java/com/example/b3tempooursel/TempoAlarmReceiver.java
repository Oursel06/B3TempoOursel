package com.example.b3tempooursel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TempoAlarmReceiver extends BroadcastReceiver {
    private static final String Log_TAG = TempoAlarmReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(Log_TAG, "Alarm !");
    }
}
