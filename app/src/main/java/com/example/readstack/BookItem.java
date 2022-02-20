package com.example.readstack;

public class BookItem{
    String author_name;
    String publisher_name;
    String published_date;
    String book_title;
    String book_description;
    String thumbnail_address;
    String info_link;
    String id;
    public BookItem(String author_name, String publisher_name, String published_date, String book_title, String book_description, String thumbnail_address,String info_link, String id){
        this.author_name = author_name;
        this.publisher_name = publisher_name;
        this.published_date = published_date;
        this.book_title = book_title;
        this.book_description = book_description;
        this.thumbnail_address = thumbnail_address;
        this.info_link = info_link;
        this.id = id;
    }

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
