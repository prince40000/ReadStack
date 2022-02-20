package com.example.readstack;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class BookAddedDetails extends AppCompatActivity{
    String author, publisher, published_date, title, desc, thumbnail_link, info_link;
    TextView author_view, publisher_view, publish_date_view, title_view;
    EditText comments;
    ImageView thumbnail;
    Button remove_button, more_info_button;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_added_details);
        author_view = findViewById(R.id.author_text);
        publisher_view = findViewById(R.id.publisher_text);
        publish_date_view = findViewById(R.id.publish_date_text);
        title_view = findViewById(R.id.title_text);
        thumbnail = findViewById(R.id.thumbnail_view_add_detail);
        remove_button = findViewById(R.id.remove_button);
        more_info_button = findViewById(R.id.more_info_button_add);
        comments = findViewById(R.id.comments_text);

        author = getIntent().getStringExtra("author");
        publisher = getIntent().getStringExtra("publisher");
        published_date = getIntent().getStringExtra("published_date");
        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("book_description");
        thumbnail_link = getIntent().getStringExtra("thumbnail_link");
        info_link = getIntent().getStringExtra("info_link");

        author_view.setText(author);
        publisher_view.setText(publisher);
        publish_date_view.setText(published_date);
        title_view.setText(title);
        //Log.d("Picasso", thumbnail_link);
        Picasso.get().load(thumbnail_link).into(thumbnail);

        /*add_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Log.d("Click", "add_button clicked");
                Intent i = new Intent(AddBook.this, MainList.class);
                i.putExtra("author", author);
                i.putExtra("publisher", publisher);
                i.putExtra("published_date", published_date);
                i.putExtra("title", title);
                i.putExtra("book_description", desc);
                i.putExtra("thumbnail_link", thumbnail_link);
                i.putExtra("info_link", info_link);
                i.putExtra("calling_class", "AddBook");
                AddBook.this.startActivity(i);
            }
        });*/

        more_info_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Log.d("Click", "more_info_button clicked");
            }
        });
    }
}
