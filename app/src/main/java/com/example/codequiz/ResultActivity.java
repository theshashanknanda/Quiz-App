package com.example.codequiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    public Button dashBoardButton;
    public TextView scoreTextView;
    public int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        scoreTextView = findViewById(R.id.scoreTextView);
        dashBoardButton = findViewById(R.id.dashBoardButton);

        score = getIntent().getIntExtra("score", 0);
        scoreTextView.setText(String.valueOf(score) + " Correct");

        dashBoardButton.setOnClickListener(v -> {
            startActivity(new Intent(ResultActivity.this, Dashboard.class));
        });
    }
}
