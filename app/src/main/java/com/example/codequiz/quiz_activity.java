package com.example.codequiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class quiz_activity extends AppCompatActivity {

    public String category;
    public String url;
    public String userChoice;
    public int pos = 1;
    public int score = 0;
    public String isSelected = "";
    public int j;

    public List<QAModel> modelList = new ArrayList<>();

    public TextView optionAButton, optionBButton, optionCButton, optionDButton, submitButton;
    public TextView question, currentPos;
    public ImageView nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_activity);

        optionAButton = findViewById(R.id.optionAButton);
        optionBButton = findViewById(R.id.optionBButton);
        optionCButton = findViewById(R.id.optionCButton);
        optionDButton = findViewById(R.id.optionDButton);
        submitButton = findViewById(R.id.submitButton);
        question = findViewById(R.id.questionTextView);
        currentPos = findViewById(R.id.currentPosTextView);

        category = getIntent().getStringExtra("category");

        if(category.equals("all")){
            url = "https://quizapi.io/api/v1/questions?apiKey=yrLhP9iuXGf3aXG57SJYVCKjCqipeED4W4hRXhyu&limit=20";
        }
        else if(category.equals("linux")){
            url = "https://quizapi.io/api/v1/questions?apiKey=yrLhP9iuXGf3aXG57SJYVCKjCqipeED4W4hRXhyu&category=linux&limit=20";
        }
        else if(category.equals("bash")){
            url = "https://quizapi.io/api/v1/questions?apiKey=yrLhP9iuXGf3aXG57SJYVCKjCqipeED4W4hRXhyu&category=bash&limit=20";
        }
        else{
            url = "https://quizapi.io/api/v1/questions?apiKey=yrLhP9iuXGf3aXG57SJYVCKjCqipeED4W4hRXhyu&limit=20";
        }

        modelList = getData();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setText();

                submitButton.setOnClickListener(v -> {
                    if(isSelected.equals("yes")){
                        if(pos < modelList.size()){
                            pos++;
                            setText();
                        }
                        else{
                            Intent intent = new Intent(quiz_activity.this, ResultActivity.class);
                            intent.putExtra("score", score);
                            startActivity(intent);
                        }
                    }
                    else{
                        Toast.makeText(quiz_activity.this, "Please select a option", Toast.LENGTH_SHORT).show();
                    }

                    if(isSelected.equals("yes")){
                        QAModel model = modelList.get(pos-1);
                        if(userChoice.equals(model.getAnswer())){
                            score++;
                        }
                    }
                    else{
                        Toast.makeText(quiz_activity.this, "Please select a option", Toast.LENGTH_SHORT).show();
                    }

                    isSelected = "no";

                    optionAButton.setBackgroundResource(R.drawable.button_white);
                    optionBButton.setBackgroundResource(R.drawable.button_white);
                    optionCButton.setBackgroundResource(R.drawable.button_white);
                    optionDButton.setBackgroundResource(R.drawable.button_white);
                });
            }
        },5000);

        optionAButton.setOnClickListener(v -> {
            optionAButton.setBackgroundResource(R.drawable.button_select);
            optionBButton.setBackgroundResource(R.drawable.button_white);
            optionCButton.setBackgroundResource(R.drawable.button_white);
            optionDButton.setBackgroundResource(R.drawable.button_white);

            userChoice = "a";
            isSelected = "yes";
        });

        optionBButton.setOnClickListener(v -> {
            optionAButton.setBackgroundResource(R.drawable.button_white);
            optionBButton.setBackgroundResource(R.drawable.button_select);
            optionCButton.setBackgroundResource(R.drawable.button_white);
            optionDButton.setBackgroundResource(R.drawable.button_white);

            userChoice = "b";
            isSelected = "yes";
        });

        optionCButton.setOnClickListener(v -> {
            optionAButton.setBackgroundResource(R.drawable.button_white);
            optionBButton.setBackgroundResource(R.drawable.button_white);
            optionCButton.setBackgroundResource(R.drawable.button_select);
            optionDButton.setBackgroundResource(R.drawable.button_white);

            userChoice = "c";
            isSelected = "yes";
        });

        optionDButton.setOnClickListener(v -> {
            optionAButton.setBackgroundResource(R.drawable.button_white);
            optionBButton.setBackgroundResource(R.drawable.button_white);
            optionCButton.setBackgroundResource(R.drawable.button_white);
            optionDButton.setBackgroundResource(R.drawable.button_select);

            userChoice = "d";
            isSelected = "yes";
        });
    }

    public void setText(){
        QAModel model = modelList.get(pos-1);

        question.setText(model.getQuestion());
        currentPos.setText(String.valueOf(pos));

        optionAButton.setText(model.getOptionA());
        optionBButton.setText(model.getOptionB());
        optionCButton.setText(model.getOptionC());
        optionDButton.setText(model.getOptionD());
    }

        private List<QAModel> getData() {
        // getting data from API
        List<QAModel> modelList1 = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject mainObject = jsonArray.getJSONObject(i);
                        JSONObject optionObject = mainObject.getJSONObject("answers");
                        JSONObject correctAnswerObject = mainObject.getJSONObject("correct_answers");

                        QAModel model = new QAModel();
                        model.setQuestion(mainObject.getString("question"));
                        model.setOptionA(optionObject.getString("answer_a"));
                        model.setOptionB(optionObject.getString("answer_b"));
                        model.setOptionC(optionObject.getString("answer_c"));
                        model.setOptionD(optionObject.getString("answer_d"));

                        String ansA = correctAnswerObject.getString("answer_a_correct");
                        String ansB = correctAnswerObject.getString("answer_b_correct");
                        String ansC = correctAnswerObject.getString("answer_c_correct");
                        String ansD = correctAnswerObject.getString("answer_d_correct");

                        String finalAns = "";
                        if(ansA.equals("true")){
                            finalAns = "a";
                        }
                        if(ansB.equals("true")){
                            finalAns = "b";
                        }

                        if(ansC.equals("true")){
                            finalAns = "c";
                        }

                        if(ansD.equals("true")){
                            finalAns = "d";
                        }

                        model.setAnswer(finalAns);

                        modelList1.add(model);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(quiz_activity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(quiz_activity.this);
        requestQueue.add(stringRequest);

        return modelList1;
    }
}
