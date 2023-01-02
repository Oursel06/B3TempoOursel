package com.example.b3tempooursel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView red_days_tv, blue_days_tv, white_days_tv;
    public static IEdfApi apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button history = findViewById(R.id.history_bt);
        history.setOnClickListener(this);
        apiInterface = ApiClient.get().create(IEdfApi.class);
        Call<TempoDaysLeft> call = apiInterface.getTempoDaysLeft(IEdfApi.EDF_TEMPO_API_ALERT_TYPE);
        call.enqueue(new Callback<TempoDaysLeft>() {
            @Override
            public void onResponse(Call<TempoDaysLeft> call, Response<TempoDaysLeft> response) {
                TextView blue_days_tv_txt = findViewById(R.id.blue_days_tv);
                TextView white_days_tv_txt = findViewById(R.id.white_days_tv);
                TextView red_days_tv_txt = findViewById(R.id.red_days_tv);
                TempoDaysLeft resource = response.body();
                if(response.code() == HttpURLConnection.HTTP_OK && resource != null) {
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
            }
            @Override
            public void onFailure(Call<TempoDaysColor> call, Throwable t) {
                Log.e("REPONSE", "ERREUR ");
            }
        });
    }
//    public void showHistory(View view){
//        Intent intent = new Intent();
//        intent.setClass(this, HistoryActivity.class);
//        startActivity(intent);
//    }

    @Override
    public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(this, HistoryActivity.class);
            startActivity(intent);
        }
    }