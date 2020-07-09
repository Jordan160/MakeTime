package com.jvetter2.maketime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jvetter2.maketime.ui.home.HomeFragment;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private ArrayList<Event> event;
    ItemClicked activity;

    public interface ItemClicked {
        void onItemClicked(int index);
    }

    public EventAdapter(HomeFragment fragment, ArrayList<Event> list) {
        event = list;
        activity = (ItemClicked) fragment;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName;
        TextView tvTime;
        TextView tvDuration;
        TextView tvDate;
        CheckBox cbCompleted;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvDate = itemView.findViewById(R.id.tvDate);
            cbCompleted = itemView.findViewById(R.id.cbCompleted);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.onItemClicked(event.indexOf(view.getTag()));
                }
            });
        }
    }


    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.memory_list, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(event.get(position));


        holder.cbCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Clicky click", Toast.LENGTH_SHORT).show();
            }
        });
//        holder.tvName.setText(event.get(position).getName());
//        holder.tvSurname.setText(event.get(position).getSurname());
//
//        if (event.get(position).getPreference().equals("bus")) {
//            holder.ivPref.setImageResource(R.drawable.bus);
//        } else {
//            holder.ivPref.setImageResource(R.drawable.plane);
//        }

    }

    @Override
    public int getItemCount() {
        return event.size();
    }
}
