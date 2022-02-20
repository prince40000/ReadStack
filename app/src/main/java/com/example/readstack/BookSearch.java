package com.example.readstack;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.AutoScrollHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BookSearch extends AppCompatActivity {

    private RequestQueue myRequestQueue;
    private ArrayList<BookItem> bookDetails;
    private EditText searchBar;
    private Button searchButton;
    private Spinner search_spinner;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_search);
        search_spinner = findViewById(R.id.search_spinner);
        @SuppressLint("WrongViewCast") ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.search_spinner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        search_spinner.setAdapter(adapter);
        searchBar = findViewById(R.id.search_bar_view);
        searchButton = findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchBar.getText().toString() == ""){
                    //Log.d("C", "fumble");
                }
                else{
                    getBookDetails(searchBar.getText().toString());
                    //Log.d("C", "made it here!");
                }
            }
        });
    }
    public void getBookDetails(String bookTitle){
        bookDetails = new ArrayList<>();
        myRequestQueue = Volley.newRequestQueue(BookSearch.this);
        myRequestQueue.getCache().clear();
        String url;
        if(search_spinner.toString() == "Author"){
            url = "https://www.googleapis.com/books/v1/volumes?q=author:" + bookTitle;
            Log.d("Search", "here!!!");
        }
        else if(search_spinner.toString() == "ISBN"){
            url = "https://www.googleapis.com/books/v1/volumes?q=ISBN:" + bookTitle;
        }
        else {
            url = "https://www.googleapis.com/books/v1/volumes?q=" + bookTitle;
        }
        RequestQueue myQueue = Volley.newRequestQueue(BookSearch.this);

        JsonObjectRequest bookRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray items = response.getJSONArray("items");
                    String author = "";
                    String thumbnail = "";
                    for(int i=0;i<items.length();i++){
                        JSONObject itemsObj = items.getJSONObject(i);
                        JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
                        JSONArray authors = volumeObj.getJSONArray("authors");
                        String publisher = volumeObj.optString("publisher");
                        String published_date = volumeObj.optString("publishedDate");
                        String title = volumeObj.optString("title");
                        String description = volumeObj.optString("description");
                        JSONObject images = volumeObj.optJSONObject("imageLinks");
                        String id = itemsObj.optString("id");
                        try {
                            thumbnail = images.optString("thumbnail");
                            thumbnail = thumbnail.substring(0, 4) + "s" + thumbnail.substring(4, thumbnail.length());
                            //Log.d("Picasso", thumbnail);
                        }
                        catch (Exception e){
                            thumbnail =  "https://i.imgur.com/6lgk6mg.png";
                        }
                        String info_link = volumeObj.optString("infoLink");
                        if(authors.length() != 0){
                            for(int c=0;c<authors.length();c++){
                                author = authors.getString(c) + " ";
                            }
                        }
                        else{
                            author = "No author found";
                        }
                        BookItem book = new BookItem(author, publisher, published_date, title, description, thumbnail, info_link, id);
                        bookDetails.add(book);

                        BookAdapter myAdapter = new BookAdapter(bookDetails, BookSearch.this, "BookSearch");
                        LinearLayoutManager layout = new LinearLayoutManager(BookSearch.this, RecyclerView.VERTICAL, false);
                        RecyclerView myRecycler = (RecyclerView) findViewById(R.id.search_resaults_list);
                        myRecycler.setLayoutManager(layout);
                        myRecycler.setAdapter(myAdapter);
                        //Log.d("C", "weird");
                    }
                } catch (JSONException e) {
                    Log.d("JSON", "JSON ERROR");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        myQueue.add(bookRequest);
    }
}
