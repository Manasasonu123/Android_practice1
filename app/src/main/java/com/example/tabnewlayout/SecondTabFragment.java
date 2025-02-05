package com.example.tabnewlayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class SecondTabFragment extends Fragment {
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private SharedViewModel sharedViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2, container, false);

        recyclerView = view.findViewById(R.id.recyclerview);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        adapter = new ItemAdapter(new ArrayList<>(), sharedViewModel);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        sharedViewModel.getTab2Items().observe(getViewLifecycleOwner(), updatedList -> {
            if (updatedList != null) {
                adapter.updateItems(updatedList);
            }
        });

        return view;
    }
}
