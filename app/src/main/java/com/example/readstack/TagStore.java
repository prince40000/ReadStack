package com.example.readstack;

import java.util.ArrayList;

public class TagStore{
    public ArrayList<String> tags;
    public TagStore(ArrayList<String> TagList){
        tags = new ArrayList<String>();
        for(int i=0;i<TagList.size();i++){
            tags.add(TagList.get(i));
        }
    }
    public TagStore(){
        tags = new ArrayList<String>();
    }
    public String getTag(int i){
        return tags.get(i);
    }
    public void addTag(String tag){
        tags.add(tag);
    }
    public void removeTag(String tag){ tags.remove(findIndex(tag)); }
    public int length(){
        return tags.size();
    }
    public boolean contains(String s){
        if(tags.contains(s)){
            return(true);
        }
        else{
            return(false);
        }
    }
    public String toString(){
        String output = "";
        for(int i = 0; i < tags.size(); i++)
        { output = output + "\n" + tags.get(i).toString();}
        return output;
    }

    public int findIndex(String tag){
        for(int i = 0; i < tags.size(); i++){
            if(tags.get(i).equals(tag)) {
                return(i);
            }
        }
        //This should never happen
        return(-1);
    }
    public String[] toArray() {
        try {
            return (tags.toArray(new String[0]));
        } catch (Exception e) {
            return (null);
        }
    }
}
