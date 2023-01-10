package com.example.b3tempooursel;

import static com.example.b3tempooursel.MainActivity.apiInterface;
import static com.example.b3tempooursel.Tools.getNowDate;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity {
    private static final String LOG_TAG = HistoryActivity.class.getSimpleName();
    // Data model
    List<TempoDate> tempoDates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        // Progress bar visible
        ProgressBar progress = findViewById(R.id.progress_bar);
        progress.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = findViewById(R.id.tempo_history_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TempoDateAdapter adapter = new TempoDateAdapter(tempoDates, this);
        recyclerView.setAdapter(adapter);
        getHistory(adapter, progress);
    }

    private void getHistory(TempoDateAdapter adapterh, ProgressBar progressh){
        if (apiInterface != null) {
            // Create call to getTempoDaysLeft
            Date now = new Date();
            TempoDateAdapter adapter = new TempoDateAdapter(tempoDates, this);
            String yearNow = getNowDate("yyyy");
            Integer yearBefore = Integer.valueOf(yearNow) - 1;
            Call<TempoHistory> call = apiInterface.getTempoHistory(yearBefore.toString(), yearNow);
            call.enqueue(new Callback<TempoHistory>() {
                @Override
                public void onResponse(@NonNull Call<TempoHistory> call, @NonNull Response<TempoHistory> response) {
                    tempoDates.clear();
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        // Progress bar supprimée (avec gone)
                        progressh.setVisibility(View.GONE);
                        tempoDates.addAll(response.body().getTempoDates());
                        // On inverse la list tempodates pour avoir la date la plus récente en haut
                        Collections.reverse(tempoDates);
                        Log.d(LOG_TAG,"nb elements => " + tempoDates.size());
                    }
                    adapterh.notifyDataSetChanged();
                }
                @Override
                public void onFailure(@NonNull Call<TempoHistory> call, @NonNull Throwable t) {
                }
            });
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        TempoDateAdapter adapter = new TempoDateAdapter(tempoDates, this);
        ProgressBar progress = findViewById(R.id.progress_bar);
        getHistory(adapter, progress);
    }
}