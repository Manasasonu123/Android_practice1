package com.example.tabnewlayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.util.ArrayList;

public class FirstTabFragment extends Fragment {
    private static final int CAMERA_PERMISSION_CODE = 100;

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private SharedViewModel sharedViewModel;
    private ImageView imageViewInBottomSheet;
    private Bitmap selectedImageBitmap = null; // Ensure it's initialized

    private final ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == requireActivity().RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    try {
                        selectedImageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
                        imageViewInBottomSheet.setImageBitmap(selectedImageBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error loading image", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private final ActivityResultLauncher<Intent> captureImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == requireActivity().RESULT_OK && result.getData() != null) {
                    Bundle extras = result.getData().getExtras();
                    if (extras != null) {
                        selectedImageBitmap = (Bitmap) extras.get("data");
                        imageViewInBottomSheet.setImageBitmap(selectedImageBitmap);
                    }
                }
            });

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        recyclerView = view.findViewById(R.id.recyclerview);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        adapter = new ItemAdapter(new ArrayList<>(), sharedViewModel);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        sharedViewModel.getTab1Items().observe(getViewLifecycleOwner(), adapter::updateItems);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Person person = adapter.getItem(viewHolder.getAdapterPosition());
                sharedViewModel.moveItemToTab2(person);
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
        Button btnUpload = sheetView.findViewById(R.id.btnuplaod);
        imageViewInBottomSheet = sheetView.findViewById(R.id.imageViewInBottomSheet);

        btnUpload.setOnClickListener(v -> openImagePickerDialog());

        btnAdd.setOnClickListener(v -> {
            String newName = inputName.getText().toString().trim();
            String newAge = inputAge.getText().toString().trim();

            if (newName.isEmpty() || newAge.isEmpty()) {
                Toast.makeText(getContext(), "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String imageBase64 = null;
            if (selectedImageBitmap != null) {
                imageBase64 = ImageEncryption.encodeImageToBase64(selectedImageBitmap);
            }

            sharedViewModel.addPersonToTab1(new Person(newName, newAge, 1, imageBase64));
            selectedImageBitmap = null; // Reset after adding
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    private void openImagePickerDialog() {
        String[] options = {"Gallery", "Camera"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Select Image Source")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        openGallery();
                    } else {
                        checkCameraPermissionAndOpenCamera();
                    }
                })
                .show();
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(galleryIntent);
    }

    private void checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureImageLauncher.launch(cameraIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
