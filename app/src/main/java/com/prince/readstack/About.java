package com.prince.readstack;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;

public class About extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                Intent i = new Intent(About.this, MainList.class);
                i.putExtra("calling_class", "About");
                About.this.startActivity(i);
            default:
                return super.onKeyUp(keyCode, event);
        }
    }
}
