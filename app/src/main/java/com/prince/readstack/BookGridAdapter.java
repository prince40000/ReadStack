package com.prince.readstack;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookGridAdapter extends RecyclerView.Adapter<BookGridAdapter.ViewHolder> {

    private ArrayList<BookItem> bookItemList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private String whoCalled;
    private Context myContext;

    // data is passed into the constructor
    BookGridAdapter(Context context, ArrayList<BookItem> data, String caller) {
        this.mInflater = LayoutInflater.from(context);
        this.bookItemList = data;
        this.myContext = context;
        this.whoCalled = caller;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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

    // total number of cells
    @Override
    public int getItemCount() {
        return bookItemList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView myImageView;

        ViewHolder(View itemView) {
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
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

