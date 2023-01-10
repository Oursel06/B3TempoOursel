package com.example.b3tempooursel;

import static com.example.b3tempooursel.MainActivity.apiInterface;
import static com.example.b3tempooursel.Tools.getNowDate;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivityV2 extends Activity implements OnItemSelectedListener{
    List<TempoDate> tempoDates = new ArrayList<>();
    private static final String LOG_TAG = HistoryActivityV2.class.getSimpleName();
    // Date du jour
    private String yearNow = getNowDate("yyyy");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_v2);
        findViewById(R.id.progress_bar_v2).setVisibility(View.VISIBLE);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        // on ajoute des élements au spinner
        List<String> categories = new ArrayList<>();
        categories.add("2023");
        categories.add("2022");
        categories.add("2021");
        categories.add("2020");
        categories.add("2019");
        categories.add("2018");
        spinner.setOnItemSelectedListener(this);
        // Adapter du spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Liaison donne adapter vers spinner
        spinner.setAdapter(dataAdapter);
        // Partie recyclerView
        RecyclerView recyclerView = findViewById(R.id.tempo_history_rv_v2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TempoDateAdapterV2 adapter = new TempoDateAdapterV2(tempoDates, this);
        recyclerView.setAdapter(adapter);

        // fonction qui récupère les données de l'api (comme dans historyActivity) et alimente le recyclerView
        getHistoryV2(adapter, yearNow);
    }

    private void getHistoryV2(TempoDateAdapterV2 adapterh, String annee) {
        if (apiInterface != null) {
            // Create call to getTempoDaysLeft
            int yearAfter = Integer.parseInt(annee) + 1;
            Call<TempoHistory> call = apiInterface.getTempoHistory(annee, Integer.toString(yearAfter));
            call.enqueue(new Callback<TempoHistory>() {
                @Override
                public void onResponse(@NonNull Call<TempoHistory> call, @NonNull Response<TempoHistory> response) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        // Progress bar supprimée (avec gone)
                        tempoDates.clear();
                        if (response.body() != null) {
                            tempoDates.addAll(response.body().getTempoDates());
                        }
                        // On inverse la list tempodates pour avoir la date la plus récente en haut
                        Collections.reverse(tempoDates);
                        Log.d(LOG_TAG, "nb elements => " + tempoDates.size());
                        findViewById(R.id.progress_bar_v2).setVisibility(View.GONE);
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
        TempoDateAdapterV2 adapter = new TempoDateAdapterV2(tempoDates, this);
        getHistoryV2(adapter, yearNow);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        findViewById(R.id.progress_bar_v2).setVisibility(View.VISIBLE);
        TempoDateAdapterV2 adapter = new TempoDateAdapterV2(tempoDates, this);
        adapter.notifyDataSetChanged();
        String item = parent.getItemAtPosition(position).toString();
        getHistoryV2(adapter, item);
        Toast.makeText(parent.getContext(), "Historique de l'année " + item, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}