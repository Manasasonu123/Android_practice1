package com.example.tabnewlayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ImageView imgbutton;

    private TextView tvName,tvAge,tvAddress;
    private MenuItem loginMenu;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME="UserPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        imgbutton = findViewById(R.id.backArrow);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        tvName=findViewById(R.id.cardname);
        tvAge=findViewById(R.id.cardage);
        tvAddress=findViewById(R.id.cardadd);
        sharedPreferences=getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        loadUserData();

//        // Bottom Sheet setup
//        LinearLayout bottomSheet = findViewById(R.id.sheet);
//        BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
//
//        // Set peekHeight (you can adjust this value based on your design)
//        bottomSheetBehavior.setPeekHeight(200);  // Set the peek height to 200dp (or any value you prefer)
//
//        // Optional: Set BottomSheet state to collapsed initially
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 0) {
                    return new FirstTabFragment();
                } else {
                    return new SecondTabFragment();
                }
            }

            @Override
            public int getItemCount() {
                return 2; // Two tabs
            }
        });

        imgbutton.setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, MainActivity.class);
            startActivity(intent);
        });

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("First Tab");
            } else {
                tab.setText("Second Tab");
            }
        }).attach();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        loginMenu=menu.findItem(R.id.action_login);
        updateLoginLogoutText();
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.action_login){
            if(isUserLoggedIn()){
                logoutUser();
            }else{
                showLoginDialog();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showLoginDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Enter Your Details");
        View view=getLayoutInflater().inflate(R.layout.dialog_user_info,null);
        EditText etName=view.findViewById(R.id.etName);
        EditText etAge=view.findViewById(R.id.etAge);
        EditText etAddress=view.findViewById(R.id.etAddress);

        builder.setView(view);
        builder.setPositiveButton("Save",((dialog, which) -> {
            String name=etName.getText().toString().trim();
            String age=etAge.getText().toString().trim();
            String address=etAddress.getText().toString().trim();

            if(!name.isEmpty() && !age.isEmpty() && !address.isEmpty()){
                saveUserData(name,age,address);
            }else{
                Toast.makeText(SecondActivity.this,"Please enter all details",Toast.LENGTH_LONG).show();

            }
        }));
        builder.setNegativeButton("Cancel",((dialog, which) -> dialog.dismiss()));
        builder.create().show();
    }
    public void saveUserData(String name,String age,String address){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("name",name);
        editor.putString("age",age);
        editor.putString("address",address);
        editor.putBoolean("isLoggedIn",true);
        editor.apply();

        loadUserData();
        updateLoginLogoutText();
    }


    private void updateLoginLogoutText() {
        if(loginMenu!=null){
            loginMenu.setTitle(isUserLoggedIn()?"Logout":"Login");
        }
    }

    private boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn",false);
    }

    private void loadUserData() {
        String name = sharedPreferences.getString("name", "-");
        String age = sharedPreferences.getString("age", "-");
        String address = sharedPreferences.getString("address", "-");

        tvName.setText(name);
        tvAge.setText(age);
        tvAddress.setText(address);
    }
    private void logoutUser(){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.apply();
        loadUserData();
        updateLoginLogoutText();
        Toast.makeText(this, "Logged out Sucessfully", Toast.LENGTH_SHORT).show();
    }
}
