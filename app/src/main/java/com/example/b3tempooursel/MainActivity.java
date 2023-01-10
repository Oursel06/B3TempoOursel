package com.example.b3tempooursel;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView red_days_tv, blue_days_tv, white_days_tv;
    public static final String CHANNEL_ID = "tempo_alert_channel_id";
    public static IEdfApi apiInterface = ApiClient.get().create(IEdfApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button history = findViewById(R.id.history_bt);
        history.setOnClickListener(this);
        getDayLeft();
        getColor();
        createNotificationChannel();
        initAlarmManager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDayLeft();
        getColor();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(this, HistoryActivity.class);
        startActivity(intent);
    }

    public void getDayLeft() {
        Call<TempoDaysLeft> call = apiInterface.getTempoDaysLeft(IEdfApi.EDF_TEMPO_API_ALERT_TYPE);
        call.enqueue(new Callback<TempoDaysLeft>() {
            @Override
            public void onResponse(Call<TempoDaysLeft> call, Response<TempoDaysLeft> response) {
                TextView blue_days_tv_txt = findViewById(R.id.blue_days_tv);
                TextView white_days_tv_txt = findViewById(R.id.white_days_tv);
                TextView red_days_tv_txt = findViewById(R.id.red_days_tv);
                TempoDaysLeft resource = response.body();
                if (response.code() == HttpURLConnection.HTTP_OK && resource != null) {
                    Log.d("REPONSE", "getParamNbJBleu : " + resource.getParamNbJBleu());
                    Log.d("REPONSE", "getParamNbJBlanc : " + resource.getParamNbJBlanc());
                    Log.d("REPONSE", "getParamNbJRouge : " + resource.getParamNbJRouge());
                    blue_days_tv_txt.setText(resource.getParamNbJBleu().toString());
                    white_days_tv_txt.setText(resource.getParamNbJBlanc().toString());
                    red_days_tv_txt.setText(resource.getParamNbJRouge().toString());
                }
            }

            @Override
            public void onFailure(Call<TempoDaysLeft> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void getColor() {
        String formatDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Call<TempoDaysColor> call2 = apiInterface.getTempoDaysColor(formatDate, IEdfApi.EDF_TEMPO_API_ALERT_TYPE);
        call2.enqueue(new Callback<TempoDaysColor>() {
            @Override
            public void onResponse(Call<TempoDaysColor> call, Response<TempoDaysColor> response) {
                TempoDaysColor resource = response.body();
                Log.d("REPONSE", "getCouleurJourJ : " + resource.getCouleurJourJ());
                Log.d("REPONSE", "getCouleurJourJ1 : " + resource.getCouleurJourJ1());
                DayColorView dayColorView = findViewById(R.id.dayColorView);
                DayColorView dayColorView2 = findViewById(R.id.dayColorView2);
                dayColorView.setDayCircleColor(resource.getCouleurJourJ());
                dayColorView2.setDayCircleColor(resource.getCouleurJourJ1());
                Log.i("Oursel", "Oursel => " + resource.getCouleurJourJ1().toString());
                checkColorForNotif(resource.getCouleurJourJ1());
            }
            @Override
            public void onFailure(Call<TempoDaysColor> call, Throwable t) {
                Log.e("REPONSE", "ERREUR ");
            }
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void checkColorForNotif(TempoColor color) {
        if (color == TempoColor.RED || color == TempoColor.WHITE) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            Log.i("Oursel", "COLOR NOTIF => " + color);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle(getString(R.string.notif_title))
                    .setContentText(getString(R.string.notif_description) + " " + getString(color.getStringResId()))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(1, builder.build());
        }
    }

    private void initAlarmManager() {
        Intent intent = new Intent(this, TempoAlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 2023, intent, PendingIntent.FLAG_IMMUTABLE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 56);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

        // Pour voir alarm dans terminal :
//        adb shell
//        dumpsys alarm | grep b3tempo
    }
}