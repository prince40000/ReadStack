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
    public void removeBook(String bookId){ books.remove(findIndex(bookId)); }
    public int length(){
        return books.size();
    }

    public String toString(){
        String output = "";
        for(int i = 0; i < books.size(); i++)
        { output = output + "\n" + books.get(i).toString();}
        return output;
    }

    public int findIndex(String bookId){
        for(int i = 0; i < books.size(); i++){
            if(books.get(i).id.equals(bookId)) {
                return(i);
            }
        }
        //This should never happen
        return(-1);
    }
    public void duplicate(){
        int size = books.size();
        for(int i = 0; i < size; i++){
            books.add(books.get(i));
        }
    }
}