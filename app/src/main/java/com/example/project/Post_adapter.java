package com.example.project;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class Post_adapter extends RecyclerView.Adapter<post_viewholder> {
    private ArrayList<post_details> items;
    private int itemLayout;
    public Post_adapter( ArrayList<post_details> items, int Layout){
        this.items = items;
        this.itemLayout = Layout;
    }
    @NonNull
    @Override
    public post_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new post_viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull post_viewholder holder, int position) {
        if(items != null && holder != null) {

            //holder.set_values(items.get(holder.getAdapterPosition()));
        }
    }

    @Override
    public int getItemCount() {
        if(items != null)
            return items.size();
        else
            return 0;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}
