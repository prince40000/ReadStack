package com.example.readstack;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainList extends AppCompatActivity{
    private BookItem newBook, tempbook;
    private ArrayList<BookItem> bookDetails, printList;
    private BookStore bookStore, newBookStore, storedStore;
    private FloatingActionButton new_button, debug_button;
    public String author, publisher, published_date, title, desc, thumbnail_link, info_link, id, caller;
    public Boolean choice = false;
    private static final String FILE_NAME="library.json";
    private FileOutputStream fos;
    private Reader reader, exportReader, importReader;
    private File file;
    private Gson gson;
    private BookGridAdapter gridAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.main_list_v2);
        create();

        //CHECKS IF FILE EXISTS, CREATES OUTPUT STREAM IF IT DOES
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

        //IF CLASS THAT CREATED THIS INTNENT ISN'T MAINACTIVITY OR BOOADDDEDDETAILS
        if(caller.equals("AddBook")) {
            caller = "";
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
            caller = "";
            RecyclerView recyclerView = findViewById(R.id.library_list);
            int numberOfColumns = 4;
            recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
            gridAdapter = new BookGridAdapter(this, printList, "MainList");
            recyclerView.setAdapter(gridAdapter);
        }

        //IF INTENT WAS CREATED BY MAIN ACTIVITY OR BOOK ADDDED DETAILS
        else{
            //TRY READING LIST FROM FILE
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

            //BINDS LIST TO GRID
            RecyclerView recyclerView = findViewById(R.id.library_list);
            int numberOfColumns = 4;
            recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
            gridAdapter = new BookGridAdapter(this, printList, "MainList");
            recyclerView.setAdapter(gridAdapter);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return(true);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.menu_import:
                Log.d("MENU", "Import Selected");
                importAlert();
                return true;
            case R.id.menu_export:
                Log.d("MENU", "Export Selected");
                exportAlert();
                return true;
            case R.id.menu_clear:
                clearAlert();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //CREATING BOOK ITEM FROM EXTRAS
    public BookItem createBook(){
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

    public void importAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainList.this);
        builder.setMessage("This will override your current library");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try{
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath(), FILE_NAME);
                    importReader = new FileReader(file.getAbsolutePath());
                    bookStore = gson.fromJson(importReader, BookStore.class);
                    writeToFile(gson.toJson(bookStore), MainList.this);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    importReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                caller = "";
                recreate();
            }
        });


        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener()     {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void exportAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainList.this);
        builder.setMessage("This will override previous exports");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    exportReader = new FileReader(file.getAbsoluteFile());
                    try {
                        file.delete();
                    }
                    catch (Exception e){

                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                storedStore = gson.fromJson(exportReader, BookStore.class);
                writeToStorage(gson.toJson(storedStore), MainList.this);
                caller = "";
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener()     {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void clearAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainList.this);
        builder.setMessage("This will clear your library");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                    Log.d("MENU", "Clear Selected");
                    file = new File(getFilesDir(), FILE_NAME);
                    file.delete();
                    bookStore = null;
                    printList = null;
                    caller = "";
                    recreate();
            }
        });


        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener()     {
            public void onClick(DialogInterface dialog, int id) {
                //do things
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void setter(Boolean input){
        choice = input;
    }
    //WRITES JSON TO FILE_NAME
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
    public void writeToStorage(String json, Context context){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Log.d("ExtraWrite", "Can Write");
        }
        try {
            File outFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), FILE_NAME);
            outFile.delete();
            FileOutputStream outputStream = new FileOutputStream(outFile, true);
            outputStream.write(json.getBytes(StandardCharsets.UTF_8));
            outputStream.close();

            Toast.makeText(getBaseContext(), "Library.json exported to documents", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //HANDLES VIEW
    public void create(){
        bookDetails = new ArrayList<>();
        printList = new ArrayList<>();
        bookStore = new BookStore();
        gson = new GsonBuilder().setPrettyPrinting().create();
        caller = getIntent().getStringExtra("calling_class");
        new_button = findViewById(R.id.new_book_button);
        debug_button = findViewById(R.id.debug_button);

        new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainList.this, BookSearch.class);
                MainList.this.startActivity(i);
            }
        });
        debug_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    reader = new FileReader(file.getAbsolutePath());
                    storedStore = gson.fromJson(reader, BookStore.class);
                    reader.close();
                    storedStore.duplicate();
                    file.delete();
                    file.createNewFile();
                    try {
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(MainList.this.openFileOutput(FILE_NAME, Context.MODE_APPEND));
                        outputStreamWriter.append(gson.toJson(storedStore));
                        outputStreamWriter.close();
                    }
                    catch (IOException e) {
                        Log.e("Exception", "File write failed: " + e.toString());
                    }
                }
                catch (IOException e){
                    newBookStore = new BookStore();
                    newBookStore.addBook(newBook);
                    //Log.d("CHECKTEST", String.valueOf(e));
                }
            }
        });
    }
}
