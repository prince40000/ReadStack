package com.prince.readstack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.MyViewHolder>{
    ArrayList<String> tagList;
    Context myContext;
    String whoCalled;
    public TagAdapter(ArrayList tagList, Context myContext, String caller) {
        this.tagList = tagList;
        this.myContext = myContext;
        this.whoCalled = caller;
    }

    @NonNull
    @Override
    public TagAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent, false);
        return new TagAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagAdapter.MyViewHolder holder, int position) {
        String tagName = tagList.get(position);
        holder.tagCheck.setText(tagName);

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            return tagList.size();
        }
        catch (Exception e){
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public CheckBox tagCheck;
        public MyViewHolder(View itemView) {
            super(itemView);
            tagCheck = itemView.findViewById(R.id.tag_item);
        }
    }
}
