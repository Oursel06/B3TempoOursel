package com.example.b3tempooursel;

import static com.example.b3tempooursel.MainActivity.apiInterface;

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
        if (apiInterface != null) {
            // Create call to getTempoDaysLeft
            Call<TempoHistory> call = apiInterface.getTempoHistory("2022", "2023");
            call.enqueue(new Callback<TempoHistory>() {

                @Override
                public void onResponse(@NonNull Call<TempoHistory> call, @NonNull Response<TempoHistory> response) {
                    tempoDates.clear();
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        // Progress bar supprimée (avec gone)
                        progress.setVisibility(View.GONE);
                        tempoDates.addAll(response.body().getTempoDates());
                        Log.d(LOG_TAG,"nb elements => " + tempoDates.size());
                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onFailure(@NonNull Call<TempoHistory> call, @NonNull Throwable t) {
                }
            });
        }
    }
}