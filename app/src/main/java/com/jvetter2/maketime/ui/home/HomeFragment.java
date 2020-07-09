package com.jvetter2.maketime.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jvetter2.maketime.Event;
import com.jvetter2.maketime.EventAdapter;
import com.jvetter2.maketime.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements EventAdapter.ItemClicked {

    private HomeViewModel homeViewModel;
    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<Event> event;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        event = new ArrayList<Event>();
        event.add(new Event("Test Event", "30 minutes", "Morning", "01/01/2020"));
        event.add(new Event("Test Event", "30 minutes", "Morning", "01/01/2020"));
        event.add(new Event("Test Event", "30 minutes", "Morning", "01/01/2020"));
        event.add(new Event("Test Event", "30 minutes", "Morning", "01/01/2020"));
        event.add(new Event("Test Event", "30 minutes", "Morning", "01/01/2020"));
        event.add(new Event("Test Event", "30 minutes", "Morning", "01/01/2020"));
        event.add(new Event("Test Event", "30 minutes", "Morning", "01/01/2020"));
        event.add(new Event("Test Event", "30 minutes", "Morning", "01/01/2020"));
        event.add(new Event("Test Event", "30 minutes", "Morning", "01/01/2020"));
        event.add(new Event("Test Event", "30 minutes", "Morning", "01/01/2020"));
        event.add(new Event("Test Event", "30 minutes", "Morning", "01/01/2020"));

        myAdapter = new EventAdapter(this, event);

        recyclerView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();


        return root;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onItemClicked(int index) {
         Toast.makeText(getContext(), "Surname: " + event.get(index).getEventName(), Toast.LENGTH_SHORT).show();
    }
}