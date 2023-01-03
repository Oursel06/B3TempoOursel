package com.example.b3tempooursel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView red_days_tv, blue_days_tv, white_days_tv;
    public static IEdfApi apiInterface = ApiClient.get().create(IEdfApi.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button history = findViewById(R.id.history_bt);
        history.setOnClickListener(this);
        getDayLeft();
    }

    @Override
    public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(this, HistoryActivity.class);
            startActivity(intent);
        }

        public void getDayLeft(){
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
        }
    }