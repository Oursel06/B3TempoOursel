package com.example.b3tempooursel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class TempoDateAdapterV2 extends RecyclerView.Adapter<TempoDateAdapterV2.TempoDateViewHolder>{
    private List<TempoDate> tempodatesv2;
    private Context context;

    public TempoDateAdapterV2(List<TempoDate> tempoDatesv2, Context context){
        this.tempodatesv2 = tempoDatesv2;
        this.context = context;
    }

    @NonNull
    @Override
    public TempoDateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_history_v2, viewGroup, false);
        return new TempoDateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TempoDateAdapterV2.TempoDateViewHolder holder, int position) {
        holder.dateres.setText(tempodatesv2.get(position).getDate());
        holder.colorres.setText(tempodatesv2.get(position).getCouleur().getStringResId());
        holder.framecol.setBackgroundColor(ContextCompat.getColor(context, tempodatesv2.get(position).getCouleur().getColorResId()));
    }

    @Override
    public int getItemCount() {
        return tempodatesv2.size();
    }

    public static class TempoDateViewHolder extends RecyclerView.ViewHolder {
        TextView dateres;
        TextView colorres;
        FrameLayout framecol;
        public TempoDateViewHolder(@NonNull View view) {
            super(view);
            dateres = view.findViewById(R.id.date_result);
            colorres = view.findViewById(R.id.color_result);
            framecol = view.findViewById(R.id.color_frame);
        }
    }
}
