package com.example.readstack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);
        Button enter_button = findViewById(R.id.enter_button);
        enter_button.setOnClickListener(enter);
    }

    View.OnClickListener enter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(MainActivity.this, MainList.class);
            i.putExtra("calling_class", "MainActivity");
            startActivity(i);
        }
    };
}