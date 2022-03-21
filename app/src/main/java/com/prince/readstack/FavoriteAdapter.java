package com.prince.readstack;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
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

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder> {
    ArrayList<BookItem> bookItemList;
    Context myContext;
    String whoCalled;
    ItemClickListener mClickListener;
    public FavoriteAdapter (ArrayList bookItemList, Context myContext, String caller) {
        this.bookItemList = bookItemList;
        this.myContext = myContext;
        this.whoCalled = caller;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BookItem bookItem = bookItemList.get(position);
        Picasso.get().load(bookItemList.get(position).getThumbnail_address()).into(holder.myImageView);

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

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView myImageView;

        MyViewHolder(View itemView) {
            super(itemView);
            myImageView = itemView.findViewById(R.id.mainListItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    BookItem getItem(int id) {
        return bookItemList.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(BookGridAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = (ItemClickListener) itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
