package com.example.cherryproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.ViewHolder> {

    ArrayList<House> items = new ArrayList<House>();
    Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.house_item, parent, false);
        context = parent.getContext();

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HouseAdapter.ViewHolder holder, int position) {
        final House item = items.get(position);
        holder.setItem(item);

    }

    @Override
    public int getItemCount() { return items.size(); }

    public void addItem(House item){
        items.add(item);
    }

    public void clearItems() {
        this.items.clear();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView address1, address2;
        ImageView imageView;

        public final View layout;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            address1 = itemView.findViewById(R.id.textView3);
            address2 = itemView.findViewById(R.id.textView4);
            imageView = itemView.findViewById(R.id.imageView6);
            layout = itemView;
        }

        public void setItem(final House item) {
            address1.setText(item.address1);
            address2.setText(item.address2);
            String filename = "";

            if(!item.picturePath1.equals("NoImage")){
                filename = item.picturePath1;
                createFile(filename);
            }else if(!item.picturePath2.equals("NoImage")){
                filename = item.picturePath2;
                createFile(filename);
            }else if(!item.picturePath3.equals("NoImage")){
                filename = item.picturePath3;
                createFile(filename);
            }else if(!item.picturePath4.equals("NoImage")){
                filename = item.picturePath4;
                createFile(filename);
            }else if(!item.picturePath5.equals("NoImage")){
                filename = item.picturePath5;
                createFile(filename);
            }else{
                imageView.setImageResource(R.drawable.defaultimage);
            }
        }

        public void createFile(String name){
            String filename = name;
            File storageDir = context.getFilesDir();
            File outFile = new File(storageDir, filename);
            Bitmap bitmap = BitmapFactory.decodeFile(outFile.getAbsolutePath(), null);
            imageView.setImageBitmap(bitmap);
        }
    }
}
