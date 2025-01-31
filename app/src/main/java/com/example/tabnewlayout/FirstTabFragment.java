package com.example.tabnewlayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.ArrayList;

public class FirstTabFragment extends Fragment {
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private SharedViewModel sharedViewModel;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        recyclerView = view.findViewById(R.id.recyclerview);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        adapter = new ItemAdapter(new ArrayList<>(),sharedViewModel);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        sharedViewModel.tab1Items.observe(getViewLifecycleOwner(), adapter::updateItems);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Person person = adapter.getItem(viewHolder.getAdapterPosition());
                sharedViewModel.moveItemToTab2(person); // Delete from Room Database
            }
        }).attachToRecyclerView(recyclerView);


        fab.setOnClickListener(v -> openBottomSheet());
        return view;
    }

    private void openBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View sheetView = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_dialog, null);
        bottomSheetDialog.setContentView(sheetView);

        EditText inputName = sheetView.findViewById(R.id.inputname);
        EditText inputAge = sheetView.findViewById(R.id.inputage);
        Button btnAdd = sheetView.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(v -> {
            String newName = inputName.getText().toString().trim();
            String newAge = inputAge.getText().toString().trim();
            if (!newName.isEmpty() && !newAge.isEmpty()) {
                sharedViewModel.addPersonToTab1(new Person(newName, newAge, 1));
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }
}
