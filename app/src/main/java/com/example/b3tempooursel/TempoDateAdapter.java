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

class TempoDateAdapter extends RecyclerView.Adapter<TempoDateAdapter.TempoDateViewHolder>{
    private List<TempoDate> tempodates;
    private Context context;


    public TempoDateAdapter(List<TempoDate> tempoDates, Context context){
        this.tempodates = tempoDates;
        this.context = context;
    }

    @NonNull
    @Override
    public TempoDateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_history, viewGroup, false);
//        TempoDateItemBinding binding = TempoDateItemBinding.bind(view);
//        return new TempoDateViewHolder(binding);
        return new TempoDateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TempoDateViewHolder holder, int position) {
        holder.dateTv.setText(tempodates.get(position).getDate());
        holder.colorFrame.setBackgroundColor(ContextCompat.getColor(context, tempodates.get(position).getCouleur().getResId()));
    }

    @Override
    public int getItemCount() {
        return tempodates.size();
    }

    public static class TempoDateViewHolder extends RecyclerView.ViewHolder {
        TextView dateTv;
        FrameLayout colorFrame;
        public TempoDateViewHolder(View view) {
            super(view);
            dateTv = view.findViewById(R.id.date_tv);
            colorFrame = view.findViewById(R.id.color_fl);
        }
    }

//    public class TempoDateViewHolder extends RecyclerView.ViewHolder {
//        TempoDateItemBinding binding;
//
//        public TempoDateViewHolder(@NonNull TempoDateItemBinding binding) {
//            super(binding.getRoot());
//            this.binding = binding;
//        }
//    }
}