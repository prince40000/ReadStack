package com.prince.readstack;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class BookItem implements Serializable {
    String author_name;
    String publisher_name;
    String published_date;
    String book_title;
    String book_description;
    String thumbnail_address;
    String info_link;
    String id;
    ArrayList<String> tags = new ArrayList<>();
    Boolean favorite;
    int status;
    float rating = 0;
    int progress = 0;
    String notes = "Notes";
    public BookItem(String author_name, String publisher_name, String published_date, String book_title, String book_description, String thumbnail_address,String info_link, String id, ArrayList<String> tags, Boolean favorite){
        this.author_name = author_name;
        this.publisher_name = publisher_name;
        this.published_date = published_date;
        this.book_title = book_title;
        this.book_description = book_description;
        this.thumbnail_address = thumbnail_address;
        this.info_link = info_link;
        this.id = id;
        this.tags = tags;
        this.favorite = favorite;

    }
    public int getStatus(){return(status);}
    public void setStatus(int s){status = s;}
    public void setProgress(int i){progress = i;}
    public int getProgress(){return(progress);}
    public float getRaiting(){return(rating);}
    public void setRaitng(float s){rating = s;}
    public String getNotes(){return(notes);}
    public void setNotes(String s){notes = s;}
    public String getAuthor_name(){
        return author_name;
    }
    public String getPublisher_name(){
        return publisher_name;
    }
    public String getPublished_date(){
        return published_date;
    }
    public String getBook_title(){
        return book_title;
    }
    public String getBook_description(){
        return book_description;
    }
    public String getThumbnail_address(){
        return thumbnail_address;
    }
    public String getInfo_link(){
        return info_link;
    }
    public String getId(){return id; }
    public ArrayList<String> getTags(){return tags;}
    public void addTag(String input){ tags.add(input); }
    public void clearTags(){ tags = new ArrayList<>();}
    public Boolean isFav(){
        if(favorite){
            return(true);
        }
        else{
            return(false);
        }
    }
    public void setFav(Boolean set){
        favorite = set;
    }
    public String toString(){
        return "Book " + id + " [Title="
                + book_title
                +", Author name"
                + author_name
                +", publisher name"
                + publisher_name
                +", published date"
                + published_date
                +", book_description"
                + book_description
                +", thumbnail address"
                + thumbnail_address
                +", info link"
                + info_link + "]";
    }
}
