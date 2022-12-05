package com.example.b3tempooursel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView responseText;
    IEdfApi apiInterface;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        responseText = (TextView) findViewById(R.id.response_text);
        apiInterface = ApiClient.get().create(IEdfApi.class);

        Call<TempoDaysLeft> call = apiInterface.getTempoDaysLeft(IEdfApi.EDF_TEMPO_API_ALERT_TYPE);
        call.enqueue(new Callback<TempoDaysLeft>() {
            @Override
            public void onResponse(Call<TempoDaysLeft> call, Response<TempoDaysLeft> response) {
                TempoDaysLeft resource = response.body();
                if(response.code() == HttpURLConnection.HTTP_OK && resource != null) {
                    responseText.setText(resource.getParamNbJBlanc().toString());
                    Log.d("REPONSE", "getParamNbJBleu : " + resource.getParamNbJBleu());
                    Log.d("REPONSE", "getParamNbJBlanc : " + resource.getParamNbJBlanc());
                    Log.d("REPONSE", "getParamNbJRouge : " + resource.getParamNbJRouge());
                }
            }
            @Override
            public void onFailure(Call<TempoDaysLeft> call, Throwable t) {
                call.cancel();
            }
        });
    }
}