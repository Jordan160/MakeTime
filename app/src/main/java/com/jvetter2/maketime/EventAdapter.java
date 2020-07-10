package com.jvetter2.maketime;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jvetter2.maketime.ui.home.HomeFragment;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private ArrayList<Event> eventList;
    private CardView eventView;
    private ItemClicked activity;

    public interface ItemClicked {
        void onItemClicked(int index);
    }

    public EventAdapter(HomeFragment fragment, ArrayList<Event> list) {
        eventList = list;
        activity = (ItemClicked) fragment;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView ivIcon;
        public TextView tvName;
        public TextView tvTime;
        public TextView tvDuration;
        public TextView tvDate;
        public CheckBox cbCompleted;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            mView = itemView;

            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvDate = itemView.findViewById(R.id.tvDate);
            cbCompleted = itemView.findViewById(R.id.cbCompleted);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.onItemClicked(eventList.indexOf(view.getTag()));
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
    public void onBindViewHolder(@NonNull final EventAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(eventList.get(position));

        Event event = eventList.get(position);
        ((ViewHolder) holder).tvName.setText(event.getEventName());
        ((ViewHolder) holder).tvTime.setText(event.getTime());
        ((ViewHolder) holder).tvDuration.setText(event.getDuration());
        ((ViewHolder) holder).tvDate.setText(event.getDate());


        holder.cbCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //eventView = holder.itemView.findViewById(R.id.eventCV);

                CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked()) {
                    Toast.makeText(view.getContext(), view.getContext().getString(R.string.event_completed), Toast.LENGTH_SHORT).show();
                    //eventView.setCardBackgroundColor(Color.GREEN);
                    ((ViewHolder) holder).ivIcon.setImageResource(R.drawable.done_icon);

                } else {
                    //eventView.setCardBackgroundColor(ContextCompat.getColor(holder.mView.getContext(), R.color.cardColor));
                    ((ViewHolder) holder).ivIcon.setImageResource(R.drawable.alert_icon);
                }
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
        return eventList.size();
    }
}
