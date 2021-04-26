package com.example.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter
{
    Context context;
    ArrayList <Weather> arrayList;

    public CustomAdapter(Context context, ArrayList<Weather> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // truyền về dữ liệu đã custom
        convertView = inflater.inflate(R.layout.row_listview, null);

        Weather weather = arrayList.get(position);

        TextView txtday= (TextView) convertView.findViewById(R.id.text_ngay);
        TextView txtmax= (TextView) convertView.findViewById(R.id.text_maxtemp);
        TextView txtmin= (TextView) convertView.findViewById(R.id.text_mintemp);
        ImageView imgstatus = (ImageView) convertView.findViewById(R.id.img_status);

        txtday.setText(weather.day);
        txtmax.setText(weather.Maxtemp+"°C");
        txtmin.setText(weather.Mintemp+"°C");

        Picasso.with(context).load("https://openweathermap.org/img/wn/"+weather.img+".png").into(imgstatus);
        return convertView;
    }
}
