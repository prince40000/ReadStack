package com.prince.readstack;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("ResourceAsColor")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);
        getSupportActionBar().hide();
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