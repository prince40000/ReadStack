package com.example.readstack;
import java.util.ArrayList;
public class BookStore{
    public ArrayList<BookItem> books;
    public BookStore(ArrayList<BookItem> bookDetails){
        books = new ArrayList<BookItem>();
        for(int i=0;i<bookDetails.size();i++){
            addBook(bookDetails.get(i));
        }
    }
    public BookStore(){
        books = new ArrayList<BookItem>();
    }
    public BookItem getBook(int i){
        return books.get(i);
    }
    public void addBook(BookItem book){
        books.add(book);
    }
    public int length(){
        return books.size();
    }
}