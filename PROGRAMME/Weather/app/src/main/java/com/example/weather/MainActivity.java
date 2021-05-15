package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    EditText edit_search;
    ImageButton btnSearch;
    Button  btnchange;
    TextView txt_name, txt_day, text_nhietdo, txt_status,
    txt_rise, txt_doam, txt_temperature, txt_tiauv, txt_wind, txt_set, txt_clouds;
    ImageView img_clouds;
    String city = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        map();
        getCurrenDay("Saigon");
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String City = edit_search.getText().toString();
                // nếu người dùng kh tìm kiếm, trả về mặc định
                if(City.equals(""))
                {
                    // gọi lại biến city
                    city = "Saigon";
                    getCurrenDay(city);
                }
                else
                {
                    city = City;
                    getCurrenDay(City);
                }

            }
        });
        btnchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // lấy dữ liệu từ từ edit chuyển hình qua màn hình thứ 2, lấy dữ liệu cho 7 ngày kế tiếp
                String City = edit_search.getText().toString();
                // chuyển sang màn hình thứ 2
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra("name", City);
                startActivity(intent);
            }
        });
//        ImageButton imgbtnSimple= (ImageButton)findViewById(R.id.btn_search);
//        imgbtnSimple.setImageResource(R.drawable.search); //set the image programmatically
    }

    public void getCurrenDay(String data)
    {
        // thực thi dữ liệu để gửi đi
        RequestQueue requestQueqe = Volley.newRequestQueue(MainActivity.this);

        String url = "https://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&appid=09522e7c1b90d4c879371c025ae95996";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.d("result", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String day = jsonObject.getString("dt");
                            String name_value = jsonObject.getString("name");
                            txt_name.setText(name_value);

                            long l = Long.valueOf(day);
                            // chuyển s -> ms
                            Date date = new Date(l*1000L);

                            // chèn giá trị format

                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd HH-mm-ss");

                            String Dt = simpleDateFormat.format(date);

                            txt_day.setText(Dt);

                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");

                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                            String status = jsonObjectWeather.getString("description");
                            String icon = jsonObjectWeather.getString("icon");

                            Picasso.with(MainActivity.this).load("https://openweathermap.org/img/wn/"+icon+".png").into(img_clouds);
                            txt_status.setText(status);
                                /// Humidity ///
                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String temp = jsonObjectMain.getString("temp");
                            String doam =  jsonObjectMain.getString("humidity");

                            // chuyển sang int
                                /// Temp ///
                            Double a = Double.valueOf(temp);
                            String Temp = String.valueOf(a.intValue());
                            text_nhietdo.setText(Temp+"°C");
                            txt_temperature.setText(Temp+"°C");
                            txt_doam.setText(doam+"%");

                                /// Wind ///
                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String gio = jsonObjectWind.getString("speed");
                            txt_wind.setText(gio+"m/s");

                                /// Clouds ///
                            JSONObject jsonObjectClouds = jsonObject.getJSONObject("clouds");
                            String may = jsonObjectClouds.getString("all");
                            txt_clouds.setText(may+"%");

                                /// RISE, SET ///
                            JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                            //String country = jsonObjectSys.getString("country");
                            String sun_rise = jsonObjectSys.getString("sunrise");
                            String sun_set = jsonObjectSys.getString("sunset");
                            txt_rise.setText(sun_rise);
                            txt_set.setText(sun_set);
                            long rise = Long.valueOf(sun_rise);
                            // chuyển s -> ms
                            Date _rise = new Date(rise*1000L);

                            // chèn giá trị format

                            SimpleDateFormat _simpleDateFormat = new SimpleDateFormat("HH:mm");

                            String sunrise = _simpleDateFormat.format(_rise);

                            txt_rise.setText(sunrise+" am");

                            long set = Long.valueOf(sun_set);
                            // chuyển s -> ms
                            Date _set = new Date(set*1000L);

                            // chèn giá trị format

                            SimpleDateFormat _simpleDate_Format = new SimpleDateFormat("HH:mm");

                            String sunset = _simpleDate_Format.format(_set);

                            txt_set.setText(sunset+" pm");


                            // tia uv
                            JSONObject jsonObjectCoord = jsonObject.getJSONObject("coord");
                            String _lon = jsonObjectCoord.getString("lon");
                            String _lat = jsonObjectCoord.getString("lat");

                            String _url = "https://api.openweathermap.org/data/2.5/onecall?lat="+_lat+"&lon="+_lon+"&exclude=hourly,daily&appid=09522e7c1b90d4c879371c025ae95996";
                           // String _url = "https://api.openweathermap.org/data/2.5/uvi?lat="+_lat+"&lon="+_lon+"&appid=09522e7c1b90d4c879371c025ae95996";
                            //txt_tiauv.setText(_url);
                            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                                    _url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            //Log.d("result", response);
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                JSONObject jsonObjectCurrent = jsonObject.getJSONObject("current");
                                                String tiauv = jsonObjectCurrent.getString("uvi");
                                                //String wind_speed = jsonObjectCurrent.getString("wind_speed");
                                                txt_tiauv.setText(tiauv);
//                                                txt_meter.setText( wind_speed + "m/s");
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
                                    requestQueqe.add(stringRequest);



                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueqe.add(stringRequest);


    }

    private void map()
    {
        edit_search = (EditText) findViewById(R.id.edit_search);
        btnSearch = (ImageButton) findViewById(R.id.btn_search);
        btnchange = (Button) findViewById(R.id.btn_change);
        txt_name = (TextView)findViewById(R.id.text_name);
        txt_day = (TextView)findViewById(R.id.text_day);
        text_nhietdo = (TextView)findViewById(R.id.text_nhietdo);
        txt_status = (TextView)findViewById(R.id.text_status);
        txt_rise = (TextView)findViewById(R.id.text_rise);
        txt_doam = (TextView)findViewById(R.id.text_doam);
        txt_temperature = (TextView)findViewById(R.id.text_temperature);
        txt_tiauv = (TextView)findViewById(R.id.text_tiauv);
        img_clouds = (ImageView)findViewById(R.id.img_clouds);
        txt_wind = (TextView) findViewById(R.id.text_wind);
        txt_set = (TextView) findViewById(R.id.text_set);
        txt_clouds = (TextView) findViewById(R.id.text_clouds);
    }
}