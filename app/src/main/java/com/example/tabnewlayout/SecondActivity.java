package com.example.tabnewlayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ImageView imgbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        imgbutton = findViewById(R.id.backArrow);

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
}
