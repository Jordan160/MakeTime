package com.jvetter2.maketime.Data;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jvetter2.maketime.MainActivity;
import com.jvetter2.maketime.R;
import com.jvetter2.maketime.HomeFragment;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

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

        final Event event = eventList.get(position);
        ((ViewHolder) holder).tvName.setText(event.getEventName());
        ((ViewHolder) holder).tvTime.setText(event.getTime());
        ((ViewHolder) holder).tvDuration.setText(event.getDuration());
        ((ViewHolder) holder).tvDate.setText(event.getDate());

        if (event.getCompleted()) {
            holder.cbCompleted.setChecked(true);
            ((ViewHolder) holder).ivIcon.setImageResource(R.drawable.done_icon);
        } else {
            holder.cbCompleted.setChecked(false);
            ((ViewHolder) holder).ivIcon.setImageResource(R.drawable.alert_icon);
        }


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle(R.string.event_delete)
                        .setMessage(R.string.event_confirmation)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    SQLiteDatabase eventDatabase = view.getContext().openOrCreateDatabase("events", MODE_PRIVATE, null);
                                    deleteEvent(event.getEventName(), eventDatabase);

                                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                                    view.getContext().startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                // Continue with delete operation

                            }
                        })

                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            }
        });

        holder.cbCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventView = holder.itemView.findViewById(R.id.eventCV);
                SQLiteDatabase eventDatabase = view.getContext().openOrCreateDatabase("events", MODE_PRIVATE, null);

                CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked()) {
                    Toast.makeText(view.getContext(), view.getContext().getString(R.string.event_completed), Toast.LENGTH_SHORT).show();
                    //eventView.setCardBackgroundColor(Color.GREEN);
                    ((ViewHolder) holder).ivIcon.setImageResource(R.drawable.done_icon);
                    completeEvent(event.getEventName(), eventDatabase);

                } else {
                    //eventView.setCardBackgroundColor(ContextCompat.getColor(holder.mView.getContext(), R.color.cardColor));
                    ((ViewHolder) holder).ivIcon.setImageResource(R.drawable.alert_icon);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static boolean deleteEvent(String name, SQLiteDatabase eventDatabase) {
        return eventDatabase.delete("events", "name" + " = ?", new String[] { name }) > 0;
    }

    public void completeEvent(String name, SQLiteDatabase eventDatabase) {
        //return eventDatabase.update("events", "name" + " = ?", new String[] { name }) > 0;

        String selection = "name" + " LIKE ?";
        String[] selectionArgs = {name};

        ContentValues cv = new ContentValues();
        //cv.put("name", name);
        cv.put("status", "true");


            int count = eventDatabase.update(
                    "events",
                    cv,
                    selection,
                    selectionArgs);
    }
}
