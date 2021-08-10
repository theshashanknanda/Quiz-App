package com.example.codequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Dashboard extends AppCompatActivity {

    public Button allButton;
    public Button linuxButton;
    public Button bashButton;
    public Button playQuiz;

    public String category = "all";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        playQuiz = findViewById(R.id.playButton);

        playQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, quiz_activity.class);
            intent.putExtra("category", category);

            startActivity(intent);
        });
    }
}
