package com.prince.readstack;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class AddBook extends AppCompatActivity{
    String author, publisher, published_date, title, desc, thumbnail_link, info_link, id;
    TextView author_view, publisher_view, publish_date_view, title_view, desc_view;
    ImageView thumbnail;
    Button add_button, more_info_button;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_book);
        author_view = findViewById(R.id.author_view);
        publisher_view = findViewById(R.id.publisher_view);
        publish_date_view = findViewById(R.id.publish_date_view);
        title_view = findViewById(R.id.title_view);
        desc_view = findViewById(R.id.desc_view);
        thumbnail = findViewById(R.id.thumbnail_view_detail);
        add_button = findViewById(R.id.add_button);
        more_info_button = findViewById(R.id.more_info_button);

        desc_view.setMovementMethod(new ScrollingMovementMethod());

        author = getIntent().getStringExtra("author");
        publisher = getIntent().getStringExtra("publisher");
        published_date = getIntent().getStringExtra("published_date");
        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("book_description");
        thumbnail_link = getIntent().getStringExtra("thumbnail_link");
        info_link = getIntent().getStringExtra("info_link");
        id = getIntent().getStringExtra("id");

        author_view.setText(author);
        publisher_view.setText(publisher);
        publish_date_view.setText(published_date);
        title_view.setText(title);
        desc_view.setText(desc);
        Picasso.get().load(thumbnail_link).into(thumbnail);

        add_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(AddBook.this, MainList.class);
                i.putExtra("author", author);
                i.putExtra("publisher", publisher);
                i.putExtra("published_date", published_date);
                i.putExtra("title", title);
                i.putExtra("book_description", desc);
                i.putExtra("thumbnail_link", thumbnail_link);
                i.putExtra("info_link", info_link);
                i.putExtra("id", id);
                i.putExtra("calling_class", "AddBook");
                AddBook.this.startActivity(i);
            }
        });

        more_info_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //Log.d("Info_Click", "more_info_button clicked");
                Intent i = new Intent();
                i.setData(Uri.parse(info_link));
                AddBook.this.startActivity(i);
            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                Intent i = new Intent(AddBook.this, BookSearch.class);
                i.putExtra("calling_class", "AddBook");
                //i.putExtra("title", title);
                AddBook.this.startActivity(i);
            default:
                return super.onKeyUp(keyCode, event);
        }
    }
}
