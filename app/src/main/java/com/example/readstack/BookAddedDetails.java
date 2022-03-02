package com.example.readstack;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;

public class BookAddedDetails extends AppCompatActivity{
    String author, publisher, published_date, title, desc, thumbnail_link, info_link, id;
    BookItem book;
    TextView author_view, publisher_view, publish_date_view, title_view, description;
    EditText comments;
    ImageView thumbnail;
    Button remove_button, more_info_button;
    static final String FILE_NAME= "library.json";
    BookStore bookStore;
    Reader reader;
    File file;
    FileOutputStream fos;
    Gson gson = new Gson();

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_added_details2);
        author_view = findViewById(R.id.author_text);
        publisher_view = findViewById(R.id.publisher_text);
        publish_date_view = findViewById(R.id.publish_date_text);
        title_view = findViewById(R.id.title_text);
        thumbnail = findViewById(R.id.thumbnail_view_add_detail);
        remove_button = findViewById(R.id.remove_button);
        more_info_button = findViewById(R.id.more_info_button_add);
        description = findViewById(R.id.description_text);
        comments = findViewById(R.id.comments_text);

        author = getIntent().getStringExtra("author");
        publisher = getIntent().getStringExtra("publisher");
        published_date = getIntent().getStringExtra("published_date");
        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("book_description");
        thumbnail_link = getIntent().getStringExtra("thumbnail_link");
        info_link = getIntent().getStringExtra("info_link");
        id = getIntent().getStringExtra("id");
        book = (BookItem) getIntent().getSerializableExtra("book_item");

        author_view.setText(author);
        publisher_view.setText(publisher);
        publish_date_view.setText(published_date);
        description.setText(desc);
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
                //Log.d("Info_Click", "more_info_button clicked");
                Intent i = new Intent();
                i.setData(Uri.parse(book.info_link));
                BookAddedDetails.this.startActivity(i);
            }
        });

        //Remove Button
        remove_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Opens file and reader, imports bookStore from JSON
                    file = new File(getFilesDir(), FILE_NAME);
                    reader = new FileReader(file.getAbsoluteFile());
                    bookStore = gson.fromJson(reader, BookStore.class);

                    //Deletes existing JSON file, creates a new blank one
                    file.delete();
                    fos = openFileOutput(FILE_NAME, Context.MODE_APPEND);
                    fos.close();

                    //Removes book from bookStore, writes new bookStore to JSON
                    bookStore.removeBook(book.id);
                    writeToFile(gson.toJson(bookStore));
                    reader.close();

                    //Moves back to main display screen
                    Intent i = new Intent(BookAddedDetails.this, MainList.class);
                    i.putExtra("calling_class", "BookAddedDetails");
                    BookAddedDetails.this.startActivity(i);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void writeToFile(String json){
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput(FILE_NAME, Context.MODE_APPEND));
            outputStreamWriter.write(json);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
