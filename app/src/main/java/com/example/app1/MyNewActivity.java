package com.example.app1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MyNewActivity extends AppCompatActivity {
    TextView t1,t2,t3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_new);

        // Retrieve the passed data from the Intent
        Intent intent = getIntent();
        String selectedItem = intent.getStringExtra("selectedItem");
        String data1 = intent.getStringExtra("data1");
        String data2 = intent.getStringExtra("data2");
        t1 = findViewById(R.id.textView10);
        t2 = findViewById(R.id.textView11);
        t3 = findViewById(R.id.textView8);
        t1.setText(selectedItem);
        t2.setText(data1);
        t3.setText(data2);
        // Do whatever you want with the data
        Log.d("MyNewActivity", "Selected item: " + selectedItem);
        Log.d("MyNewActivity", "Data 1: " + data1);
        Log.d("MyNewActivity", "Data 2: " + data2);


    }
}
