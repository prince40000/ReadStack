package com.prince.readstack;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {
    ArrayList<BookItem> bookItemList;
    Context myContext;
    String whoCalled;
    public BookAdapter (ArrayList bookItemList, Context myContext, String caller) {
        this.bookItemList = bookItemList;
        this.myContext = myContext;
        this.whoCalled = caller;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BookItem bookItem = bookItemList.get(position);
        holder.book_title.setText(bookItem.getBook_title());
        holder.author.setText(bookItem.getAuthor_name());
        holder.publisher.setText(bookItem.getPublisher_name());
        holder.published_date.setText(bookItem.getPublished_date());
        //Log.d("Picasso", bookItem.getThumbnail_address());
        Picasso.get().load(bookItem.getThumbnail_address()).into(holder.thumbnail);

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(whoCalled.equals("BookSearch")) {
                    Intent i = new Intent(myContext, AddBook.class);
                    i.putExtra("author", bookItem.getAuthor_name());
                    i.putExtra("publisher", bookItem.getPublisher_name());
                    i.putExtra("published_date", bookItem.getPublished_date());
                    i.putExtra("title", bookItem.getBook_title());
                    Log.d("Test", bookItem.getBook_title());
                    i.putExtra("book_description", bookItem.getBook_description());
                    i.putExtra("thumbnail_link", bookItem.getThumbnail_address());
                    //Log.d("Picasso", bookItem.getThumbnail_address());
                    i.putExtra("info_link", bookItem.getInfo_link());
                    i.putExtra("id", bookItem.getId());
                    Log.d("Test", bookItem.getId());
                    myContext.startActivity(i);
                }
                else{
                    Intent i = new Intent(myContext, BookAddedDetails.class);
                    i.putExtra("author", bookItem.getAuthor_name());
                    i.putExtra("publisher", bookItem.getPublisher_name());
                    i.putExtra("published_date", bookItem.getPublished_date());
                    i.putExtra("title", bookItem.getBook_title());
                    i.putExtra("book_description", bookItem.getBook_description());
                    i.putExtra("thumbnail_link", bookItem.getThumbnail_address());
                    //Log.d("Picasso", bookItem.getThumbnail_address());
                    i.putExtra("info_link", bookItem.getInfo_link());
                    i.putExtra("id", bookItem.getId());
                    i.putExtra("book_item", bookItem);
                    myContext.startActivity(i);
                }
            }
        });
    }


@Override
    public int getItemCount() {
        try {
            return bookItemList.size();
        }
        catch (Exception e){
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView thumbnail;
        public TextView book_title, author, publisher, published_date;
        //ImageView thumbnail, TextView book_title, TextView author, TextView publisher, TextView published_date
        public MyViewHolder(View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail_view_item);
            book_title = itemView.findViewById(R.id.book_title);
            author = itemView.findViewById(R.id.author_name);
            publisher = itemView.findViewById(R.id.publisher);
            published_date = itemView.findViewById(R.id.published_date);
        }
    }
}
