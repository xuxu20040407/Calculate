package com.example.calculate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerK;
    private Spinner spinnercol;
    private Spinner spinnerNum;
    private int k = 10; // 默认题目数量
    private int num = 5; // 默认数字范围
    private  int col =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerK = findViewById(R.id.spinner_k);
        spinnercol = findViewById(R.id.spinner_col);
        spinnerNum = findViewById(R.id.spinner_num);
        findViewById(R.id.btn_generate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQuestions();
            }
        });

        // 设置题目数量Spinner
        setupSpinner(spinnerK, new int[]{10, 20, 50, 100}, new String[]{"10题", "20题", "50题", "100题"});
        // 设置数字范围Spinner
        setupSpinner(spinnerNum, new int[]{5, 10, 20, 50, 100}, new String[]{"5以内", "10以内", "20以内", "50以内", "100以内"});
        // 设置数字范围Spinner
        setupSpinner(spinnercol, new int[]{1,2,3,4,5}, new String[]{"每行1个", "每行2个", "每行3个", "每行4个", "每行5个"});
    }

    private void setupSpinner(Spinner spinner, int[] values, String[] labels) {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                labels
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner == spinnerK) {
                    k = values[position];
                } else if (spinner == spinnerNum) {
                    num = values[position];
                }  else if (spinner == spinnercol) {
                    col = values[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void generateQuestions() {
        // 跳转到显示题目的界面
        Intent intent = new Intent(this, QuestionsActivity.class);
        intent.putExtra("k", k);
        intent.putExtra("num", num);
        intent.putExtra("col", col);
        startActivity(intent);
    }
}