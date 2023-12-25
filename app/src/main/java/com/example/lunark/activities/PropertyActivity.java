package com.example.lunark.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.lunark.R;
import com.example.lunark.databinding.ActivityPropertyBinding;

public class PropertyActivity extends AppCompatActivity {

    private ActivityPropertyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPropertyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        binding.thumbnail.setImageResource(intent.getIntExtra("thumbnail", 0));
        binding.name.setText(intent.getStringExtra("name"));
        binding.description.setText(intent.getStringExtra("description"));
        binding.location.setText(intent.getStringExtra("location"));
        binding.rating.setText(Double.toString(intent.getDoubleExtra("rating", 0.0)));
    }
}