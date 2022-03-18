package com.example.readstack;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;

public class BookAddedDetails extends AppCompatActivity{
    String author, publisher, published_date, title, desc, thumbnail_link, info_link, id;
    TagStore tagList;
    ArrayList<String> bookTags;
    ArrayList<String> addTags = new ArrayList<>();
    BookItem book;
    TextView author_view, publisher_view, publish_date_view, title_view, description, progressText;
    EditText comments;
    ImageView thumbnail;
    Spinner status_spinner;
    RatingBar rating;
    SeekBar progress;
    LinearLayout footer, progressBarLayout;
    Button remove_button, more_info_button, tag_button, favorite_button;
    static final String FILE_NAME= "library.json";
    static final String TAG_FILE_NAME = "tag_list.json";
    BookStore bookStore;
    Reader reader, tagreader;
    File file, tagfile;
    FileOutputStream fos;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    boolean isKeyboardShowing = false;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_added_details2);
        getSupportActionBar().hide();
        author_view = findViewById(R.id.author_text);
        publisher_view = findViewById(R.id.publisher_text);
        publish_date_view = findViewById(R.id.publish_date_text);
        title_view = findViewById(R.id.title_text);
        thumbnail = findViewById(R.id.thumbnail_view_add_detail);
        remove_button = findViewById(R.id.remove_button);
        more_info_button = findViewById(R.id.more_info_button_add);
        description = findViewById(R.id.description_text);
        description.setMovementMethod(new ScrollingMovementMethod());
        comments = findViewById(R.id.comments_text);
        tag_button = findViewById(R.id.tag_button);
        favorite_button = findViewById(R.id.fav_button);
        footer = findViewById(R.id.bookAddedDetailsFooter);
        status_spinner = findViewById(R.id.status_spinner);
        rating = findViewById(R.id.ratingBar);
        progress = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);
        progressBarLayout = findViewById(R.id.progressBarLayout);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.status_spinner_array, R.layout.status_spinner);
        adapter.setDropDownViewResource(R.layout.status_spinner_dropdown);
        status_spinner.setPrompt("Status");
        status_spinner.setAdapter(new CustomSpinnerAdapter(adapter, R.layout.status_spinner_default, this));
        status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 4) {
                    progressBarLayout.setVisibility(View.GONE);
                    rating.setVisibility(View.VISIBLE);
                    try {
                        rating.setRating(book.getRaiting());
                    }
                    catch (NullPointerException e){
                    }
                }
                if (i == 3 || i ==5) {
                    rating.setVisibility(View.GONE);
                    progressBarLayout.setVisibility(View.VISIBLE);
                }
                if(i == 1 || i == 2){
                    progressBarLayout.setVisibility(View.GONE);
                    rating.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                progressBarLayout.setVisibility(View.GONE);
                rating.setVisibility(View.GONE);
            }
        });

        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int progressPrint = 5*(Math.round(progress.getProgress()/5));
                progressText.setText(progressPrint + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progressPrint = 5*(Math.round(progress.getProgress()/5));
                progressText.setText(progressPrint + "%");
                saveChanges();
            }
        });
        try {
            //Opens file and reader, imports bookStore from JSON
            file = new File(getFilesDir(), FILE_NAME);
            reader = new FileReader(file.getAbsoluteFile());
            bookStore = gson.fromJson(reader, BookStore.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        author = getIntent().getStringExtra("author");
        publisher = getIntent().getStringExtra("publisher");
        published_date = getIntent().getStringExtra("published_date");
        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("book_description");
        thumbnail_link = getIntent().getStringExtra("thumbnail_link");
        info_link = getIntent().getStringExtra("info_link");
        id = getIntent().getStringExtra("id");
        int index = bookStore.findIndex(id);
        book = bookStore.getBook(index);
        try {
            status_spinner.setSelection(book.getStatus());
            progress.setProgress(book.getProgress());
        }
        catch (NullPointerException e){

        }
        comments.setText(book.getNotes());
        if(book.isFav()){
            favorite_button.setText("Unfavorite");
        }

        author_view.setText(author);
        publisher_view.setText(publisher);
        publish_date_view.setText(published_date);
        description.setText(desc);
        title_view.setText(title);
        Picasso.get().load(thumbnail_link).into(thumbnail);

        ArrayList<String> currentTags = book.getTags();
        try {
            if (currentTags.contains("Favorite")) {
                favorite_button.setText("Unfavorite");
            }
        }
        catch (NullPointerException e){}
        tag_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTagList();
            }
        });

        favorite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!book.isFav()){
                    book.setFav(true);
                    favorite_button.setText("Unfavorite");

                    try {
                        file.delete();
                        fos = openFileOutput(FILE_NAME, Context.MODE_APPEND);
                        fos.close();
                        writeToFile(gson.toJson(bookStore));
                        reader.close();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else{
                    book.setFav(false);
                    favorite_button.setText("Favorite");

                    try {
                        file.delete();
                        fos = openFileOutput(FILE_NAME, Context.MODE_APPEND);
                        fos.close();
                        writeToFile(gson.toJson(bookStore));
                        reader.close();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        more_info_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent();
                i.setData(Uri.parse(book.info_link));
                BookAddedDetails.this.startActivity(i);
            }
        });

        remove_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Opens file and reader, imports bookStore from JSON
                    //file = new File(getFilesDir(), FILE_NAME);
                    //reader = new FileReader(file.getAbsoluteFile());
                    //bookStore = gson.fromJson(reader, BookStore.class);

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

        View mainLayout = findViewById(R.id.bookAddedDetailsLayout);
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect r = new Rect();
                        mainLayout.getWindowVisibleDisplayFrame(r);
                        int screenHeight = mainLayout.getRootView().getHeight();
                        int keypadHeight = screenHeight - r.bottom;
                        if (keypadHeight > screenHeight * 0.15) {
                            // keyboard is opened
                            if (!isKeyboardShowing) {
                                isKeyboardShowing = true;
                                onKeyboardVisibilityChanged(true);
                            }
                        }
                        else {
                            // keyboard is closed
                            if (isKeyboardShowing) {
                                isKeyboardShowing = false;
                                onKeyboardVisibilityChanged(false);
                            }
                        }
                    }
                });
    }
    public void saveChanges(){
        book.setNotes(comments.getText().toString());
        book.setStatus(status_spinner.getSelectedItemPosition());
        book.setProgress(progress.getProgress());
        book.setRaitng(rating.getRating());
        file.delete();
        try {
            fos = openFileOutput(FILE_NAME, Context.MODE_APPEND);
            fos.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        writeToFile(gson.toJson(bookStore));
    }
    void onKeyboardVisibilityChanged(boolean opened) {
        View footer = findViewById(R.id.bookAddedDetailsFooter);
        if(opened){
            footer.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
        }
        else{
            footer.setVisibility(View.VISIBLE);
            description.setVisibility(View.VISIBLE);
            saveChanges();
        }
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
    public void writeToTagList(String json){
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput(TAG_FILE_NAME, Context.MODE_APPEND));
            outputStreamWriter.write(json);
            outputStreamWriter.close();
            Log.d("FileWriter", json);
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    public void showTagList(){
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tags");
        try{
            tagfile = new File(getFilesDir(), TAG_FILE_NAME);
            tagreader = new FileReader(tagfile.getAbsoluteFile());
            tagList = gson.fromJson(tagreader, TagStore.class);
        }
        catch (Exception e){
            e.printStackTrace();
            tagList = new TagStore();
        }

        // add a checkbox list
        boolean[] checkedItems = new boolean[tagList.length()];
        try {
            bookTags = book.getTags();
            book.clearTags();
            for (int i = 0; i < bookTags.size(); i++) {
                int index = tagList.findIndex(bookTags.get(i));
                checkedItems[index] = true;
            }
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        String[] tagDisplayArray = tagList.toArray();
        //Displays checklist
        builder.setMultiChoiceItems(tagDisplayArray, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                String toAdd = tagDisplayArray[which];
                if(addTags.contains(tagDisplayArray[which])){
                    addTags.remove(tagDisplayArray[which]);
                    checkedItems[which] = false;
                }
                else {
                    addTags.add(toAdd);
                    checkedItems[which] = true;
                }
            }
            //Save changes to tags/applies tags
        }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(int i=0; i<addTags.size(); i++){
                    try {
                        if (bookTags.contains(addTags.get(i))) {
                        } else {
                            book.addTag(addTags.get(i));
                        }
                    }
                    catch (NullPointerException e){
                        book.addTag(addTags.get(i));
                    }
                }
                try {
                    for (int c = 0; c < bookTags.size(); c++) {
                        addTags.add(bookTags.get(c));
                    }
                }
                catch (NullPointerException e){
                }
                file.delete();
                try {
                    fos = openFileOutput(FILE_NAME, Context.MODE_APPEND);
                    fos.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                writeToFile(gson.toJson(bookStore));
            }
        });

        //New Tag Button
        builder.setNeutralButton("New", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Displays tag creation dialogue
                AlertDialog.Builder newTagBuilder = new AlertDialog.Builder(BookAddedDetails.this);
                newTagBuilder.setTitle("New Tag");
                EditText input = new EditText(BookAddedDetails.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                newTagBuilder.setView(input);
                //Adds new tag to master list
                newTagBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String tag = input.getText().toString();
                        //Log.d("Tags", tag);
                        if(tagList.contains(tag)){
                            Toast.makeText(getBaseContext(), "Error tag already exists", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tagList.addTag(tag);
                                    tagfile.delete();
                                    try {
                                        fos = openFileOutput(TAG_FILE_NAME, Context.MODE_APPEND);
                                        fos.close();
                                        writeToTagList(gson.toJson(tagList));
                                        tagreader = new FileReader(tagfile.getAbsoluteFile());
                                        tagList = gson.fromJson(tagreader, TagStore.class);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                });
                //Cancels adding new tag
                newTagBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog newTagDialog = newTagBuilder.create();
                newTagDialog.create();
                newTagDialog.show();
            }
        });
        //Cancels adding any tags
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.create();
        dialog.show();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch(keyCode){
            case KeyEvent.KEYCODE_BACK:
                Intent i = new Intent(BookAddedDetails.this, MainList.class);
                i.putExtra("calling_class", "BookAddedDetails");
                saveChanges();
                BookAddedDetails.this.startActivity(i);
            default:
                return super.onKeyUp(keyCode, event);
        }
    }
}
