package com.example.readstack;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
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
import java.util.ArrayList;

public class BookAddedDetails extends AppCompatActivity{
    String author, publisher, published_date, title, desc, thumbnail_link, info_link, id;
    ArrayList<String> tagList = new ArrayList<>();
    ArrayList<String> addTags = new ArrayList<>();
    BookItem book;
    TextView author_view, publisher_view, publish_date_view, title_view, description;
    EditText comments;
    ImageView thumbnail;
    Button remove_button, more_info_button, tag_button, favorite_button;
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
        tag_button = findViewById(R.id.tag_button);
        favorite_button = findViewById(R.id.fav_button);

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
        Picasso.get().load(thumbnail_link).into(thumbnail);

        tag_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTagList();
            }
        });

        more_info_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
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

    public void showTagList(){
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tags");

        // add a checkbox list
        boolean[] checkedItems = {false, false, false, false, false};
        String[] tagDisplayArray = tagList.toArray(new String[0]);
        builder.setMultiChoiceItems(tagDisplayArray, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
               String toAdd = tagDisplayArray[which];
               addTags.add(toAdd);
               try{
                   for(int i = 0; i < addTags.size(); i++) {
                       book.tags.add(addTags.get(i));
                   }
               }
               catch (Exception e){

               }
            }
        });

        // add OK and Cancel buttons
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
            }
        });
        builder.setNeutralButton("New", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog.Builder newTagBuilder = new AlertDialog.Builder(BookAddedDetails.this);
                newTagBuilder.setTitle("New Tag");
                EditText input = new EditText(BookAddedDetails.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                newTagBuilder.setView(input);

                newTagBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String tag = input.getText().toString();
                        tagList.add(tag);
                    }
                });

                newTagBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog newTagDialouge = newTagBuilder.create();
                newTagDialouge.show();
            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
