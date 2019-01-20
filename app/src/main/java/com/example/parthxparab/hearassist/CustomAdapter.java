package com.example.parthxparab.hearassist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private  int layout;
    private ArrayList<Items> itemList;

    public CustomAdapter(Context context, int layout, ArrayList<Items> itemList) {
        this.context = context;
        this.layout = layout;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView txtName, txtId;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtName = (TextView) row.findViewById(R.id.listname);
            holder.imageView = (ImageView) row.findViewById(R.id.img);
            holder.txtId = (TextView) row.findViewById(R.id.titlename);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        Items Items = itemList.get(position);

        holder.txtName.setText("Filename: "+Items.getName()+".jpg");
        holder.txtId.setText("TEST "+Items.geId());

        String ItemsImage = Items.getImage();
        Bitmap bitmap = BitmapFactory.decodeFile(ItemsImage);
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }
}
