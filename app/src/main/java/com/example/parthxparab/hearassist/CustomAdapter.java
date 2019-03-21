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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private  int layout;
    private ArrayList<Items> itemList;
    String date1 ="",date2="",namelist ="";

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

            date1=Items.getName();
            namelist= ""+Items.getUser();
        date2=date1.substring(0,2)+'-'+date1.substring(2,4)+'-'+date1.substring(4,8)+' '+' '+' '+date1.substring(8,10)+':'+date1.substring(10);

        holder.txtName.setText(""+date2+"  |  "+namelist);
        holder.txtId.setText("Test ID : "+Items.getName());

        String ItemsImage = Items.getImage();
        Bitmap bitmap = BitmapFactory.decodeFile(ItemsImage);
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }
}
