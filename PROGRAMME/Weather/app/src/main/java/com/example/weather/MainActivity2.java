package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity2 extends AppCompatActivity {
    String nameCity = "";
    ImageView img_back;
    TextView txt_name;
    ListView listView;
    CustomAdapter customAdapter;
    ArrayList <Weather> arrWeather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        map();
        // nhận dữ liệu về
        Intent intent = getIntent();
        String City = intent.getStringExtra("name");
        Log.d("ket qua", "Du lieu truyen qua :" + City);
        //Get7DayData();
        // kh có giá trị thì mặc định
        if(City.equals(""))
        {
            nameCity = "Saigon";
            Get7DaysData(nameCity);
        }
        else
        {
            nameCity = City;
            Get7DaysData(nameCity);
        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void  map()
    {
        img_back = (ImageView)findViewById(R.id.img_back);
        txt_name = (TextView)findViewById(R.id.text_name);
        listView = (ListView)findViewById(R.id.listview);

        arrWeather = new ArrayList<Weather>();
        customAdapter = new CustomAdapter(MainActivity2.this, arrWeather);

        listView.setAdapter(customAdapter);
    }
    private void Get7DaysData(String data)
    {
        String url = "https://api.openweathermap.org/data/2.5/forecast?q="+data+"&units=metric&appid=09522e7c1b90d4c879371c025ae95996";
        // đọc dữ liệu thư viện Volley
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity2.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  Log.d("ketqua", "Json: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            // cityName
                            JSONObject jsonObjectCity = jsonObject.getJSONObject("city");
                            String name = jsonObjectCity.getString("name");
                            txt_name.setText(name);

                            // list
                            JSONArray jsonArrayList = jsonObject.getJSONArray("list");
                            for(int i = 0; i < jsonArrayList.length(); i++)
                            {
                                // truyền biến i để nó tự tăng dần
                                JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);
                                String thu = jsonObjectList.getString("dt");
                                    //*** thứ trong tuần ***//

                                // chuyển kiểu dữ liệu từ  string -> long
                                long l = Long.valueOf(thu);
                                // chuyển s -> ms
                                Date date = new Date(l*1000L);
                                // chèn giá trị format
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
                                String Day = simpleDateFormat.format(date);

                                    //*** temp ***//
                                JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("main");
                                String tempMax = jsonObjectTemp.getString("temp_max");
                                String tempMin = jsonObjectTemp.getString("temp_min");
                                Double a = Double.valueOf(tempMax);
                                Double b = Double.valueOf(tempMin);
                                String TempMax = String.valueOf(a.intValue());
                                String TempMin = String.valueOf(b.intValue());
                                    //*** weather ***//
                                JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0); // có duy nhất 1 cặp thẻ
                                //String status = jsonObjectWeather.getString("description");
                                String icon = jsonObjectWeather.getString("icon");

                                arrWeather.add(new Weather(Day, icon, TempMax, TempMin));
                            }
                            // nếu có dữ liệu mới thì ta update lại Ađapter
                            customAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }
}