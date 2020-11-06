package com.fahim.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.android.volley.Request.Method.*;

public class MainActivity extends AppCompatActivity {

    private TextView city, date, temp, humidity, feels, wind, sunrise, sunset;
    private Button btn;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = findViewById(R.id.City);
        date = findViewById(R.id.Time);
        temp = findViewById(R.id.Temp);
        btn = findViewById(R.id.btn);
        feels = findViewById(R.id.Feels);
        humidity = findViewById(R.id.Humidity);
        wind = findViewById(R.id.Wind);
        sunrise = findViewById(R.id.Sunrise);
        sunset = findViewById(R.id.Sunset);
        editText = findViewById(R.id.editText);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = editText.getText().toString();
                parseJsonData(location);
            }
        });
    }

    private void parseJsonData(String location) {

        String url ="http://api.openweathermap.org/data/2.5/weather?q="+location+"&"+"appid=Paste Your App ID Here";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("ttt", "onResponse: "+response);

                try {


                    String cityname = response.getString("name");

                    Date dd = new Date();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
                    df.setTimeZone(TimeZone.getTimeZone(cityname));
                    date.setText(df.format(dd));

                    JSONObject main = response.getJSONObject("main");
                    Double temparet = main.getDouble("temp");
                    Double feelslike = main.getDouble("feels_like");
                    Integer hum = main.getInt("humidity");
                    humidity.setText("Humidity "+hum);
                    feels.setText("Feels Like "+(String.format("%.2f",temparet-273)+" °C"));
                    temp.setText((String.format("%.2f",temparet-273)+" °C"));


                    JSONObject winds = response.getJSONObject("wind");
                    Double speed = winds.getDouble("speed");
                    wind.setText("Wind "+speed+" km/h");


                    JSONObject sys = response.getJSONObject("sys");
                    String cn = sys.getString("country");

                    Integer sunr = sys.getInt("sunrise");
                    Integer suns = sys.getInt("sunset");
                    sunrise.setText("Sunrise: "+sunr);
                    sunset.setText("Sunset: "+suns);


                    city.setText(cityname+","+cn);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ttt", "onErrorResponse: "+error.getMessage());
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }


}