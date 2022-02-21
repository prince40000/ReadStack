package com.example.readstack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;

public class MainList extends AppCompatActivity{
    private BookItem newBook;
    private ArrayList<BookItem> bookDetails, printList;
    private BookStore bookStore, newBookStore;
    private FloatingActionButton new_button;
    public String author, publisher, published_date, title, desc, thumbnail_link, info_link, id, caller;
    private static final String FILE_NAME="library.json";
    private FileOutputStream fos;
    private Reader reader;
    private File file;
    private Gson gson;
    private BookGridAdapter gridAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list);
        create();

        try {
            file = new File(getFilesDir(), FILE_NAME);
            if(!file.exists()){
            }
            fos = openFileOutput(FILE_NAME, Context.MODE_APPEND);
            fos.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if(!caller.equals("MainActivity") && !caller.equals("BookAddedDetails")) {
            newBook = createBook();
            bookDetails.add(newBook);
            try {
                reader = new FileReader(file.getAbsolutePath());
                newBookStore = gson.fromJson(reader, BookStore.class);
                newBookStore.addBook(newBook);
                reader.close();
                file.delete();
                file.createNewFile();
            }
            catch (Exception e){
                newBookStore = new BookStore();
                newBookStore.addBook(newBook);
                Log.d("CHECKTEST", String.valueOf(e));
            }
            Log.d("TESTER", newBook.toString());

            for(int i=0;i<newBookStore.length();i++){
                printList.add(newBookStore.getBook(i));
            }
            writeToFile(gson.toJson(newBookStore), this);
            /*
            BookAdapter myAdapter = new BookAdapter(printList, MainList.this, "MainList");
            LinearLayoutManager layout = new LinearLayoutManager(MainList.this, RecyclerView.VERTICAL, false);
            RecyclerView myRecycler = (RecyclerView) findViewById(R.id.library_list);
            myRecycler.setLayoutManager(layout);
            myRecycler.setAdapter(myAdapter);
            */
            //GridLayoutManager newLayout = new GridLayoutManager(MainList.this, RecyclerView, false);

            RecyclerView recyclerView = findViewById(R.id.library_list);
            int numberOfColumns = 4;
            recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
            gridAdapter = new BookGridAdapter(this, printList, "MainList");
            recyclerView.setAdapter(gridAdapter);
        }

        else{
            try {
                reader = new FileReader(file.getAbsoluteFile());
                bookStore = gson.fromJson(reader, BookStore.class);
                reader.close();
                for (int i = 0; i < bookStore.length(); i++) {
                    printList.add(bookStore.getBook(i));
                }
            }
            catch(Exception e){
            }
            /*
            BookAdapter myAdapter = new BookAdapter(printList, MainList.this, "MainList");
            LinearLayoutManager layout = new LinearLayoutManager(MainList.this, RecyclerView.VERTICAL, false);
            RecyclerView myRecycler = (RecyclerView) findViewById(R.id.library_list);
            myRecycler.setLayoutManager(layout);
            myRecycler.setAdapter(myAdapter);
            */
            RecyclerView recyclerView = findViewById(R.id.library_list);
            int numberOfColumns = 4;
            recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
            gridAdapter = new BookGridAdapter(this, printList, "MainList");
            recyclerView.setAdapter(gridAdapter);
        }
    }

    public BookItem createBook(){
        //CREATING BOOK ITEM FROM EXTRAS
        author = getIntent().getStringExtra("author");
        publisher = getIntent().getStringExtra("publisher");
        published_date = getIntent().getStringExtra("published_date");
        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("book_description");
        thumbnail_link = getIntent().getStringExtra("thumbnail_link");
        info_link = getIntent().getStringExtra("info_link");
        id = getIntent().getStringExtra("id");
        BookItem book = new BookItem(author,publisher,published_date,title,desc,thumbnail_link,info_link, id);
        return book;
    }

    public void writeToFile(String json, Context context){
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(FILE_NAME, Context.MODE_APPEND));
            outputStreamWriter.append(json);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /*public JSONObject convertBookToJSON(BookItem book, JSONArray bookList){
        //JSONArray bookList = new JSONArray();
        JSONObject jsonBook = new JSONObject();
        try {
            //Log.d("Check", "Here");
            jsonBook.put("id", book.getId());
            jsonBook.put("title", book.getBook_title());
            //Log.d("Test", book.getBook_title());
            jsonBook.put("author", book.getAuthor_name());
            jsonBook.put("publisher", book.getPublisher_name());
            jsonBook.put("published_date", book.getPublished_date());
            jsonBook.put("thumbnail", book.getThumbnail_address());
            jsonBook.put("info", book.getInfo_link());
            //bookList.put(jsonBook);
            //return bookList;
            return jsonBook;
        }
        catch (Exception e){
            return null;
        }
    }
    public void printJSON(JSONArray booklist, JSONObject jsonBook){
        JSONArray bookList = new JSONArray();
        bookList.put(jsonBook);
        writeToFile(bookList.toString(), this);
    }*/

    public void create(){
        bookDetails = new ArrayList<>();
        printList = new ArrayList<>();
        bookStore = new BookStore();
        gson = new GsonBuilder().setPrettyPrinting().create();
        caller = getIntent().getStringExtra("calling_class");
        new_button = findViewById(R.id.new_book_button);
        new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainList.this, BookSearch.class);
                MainList.this.startActivity(i);
            }
        });
    }
}
