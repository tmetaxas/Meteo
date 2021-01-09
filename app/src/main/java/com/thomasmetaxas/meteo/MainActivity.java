package com.thomasmetaxas.meteo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.ImageView;
import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView dateText, cityText, temperatureText, descriptionText, feelsLikeText, lowText, highText, humidityText, windText;
    ImageView image;
    String city = "Toronto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateText = findViewById(R.id.dateText);
        cityText = findViewById(R.id.cityText);
        temperatureText = findViewById(R.id.temperatureText);
        descriptionText = findViewById(R.id.descriptionText);
        feelsLikeText = findViewById(R.id.feelsLikeText);
        lowText = findViewById(R.id.lowText);
        highText = findViewById(R.id.highText);
        humidityText = findViewById(R.id.humidityText);
        windText = findViewById(R.id.windText);

        afficher();

    }

    public void afficher() {
        String url = "http://api.openweathermap.org/data/2.5/weather?q=Toronto&appid=4a496adeafb114fe32f0846ca97e4a09&units=metric";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    Log.d("Tag", "resultat =" + array.toString());
                    String temp = response.getJSONObject("main").getString("temp");
                    String low = response.getJSONObject("main").getString("temp_min");
                    String high = response.getJSONObject("main").getString("temp_max");
                    String feelsLike = response.getJSONObject("main").getString("feels_like");
                    String humidity = response.getJSONObject("main").getString("humidity");
                    String wind = response.getJSONObject("wind").getString("speed");
                    String city = response.getString("name");
                    temperatureText.setText(temp + "°C");
                    feelsLikeText.setText("Feels like: " + feelsLike + "°C");
                    lowText.setText("Low: " + low + "°C");
                    highText.setText("High: " + high + "°C");
                    humidityText.setText("Humidity: " + humidity + "%");
                    windText.setText("Wind: " + wind + "km/h");
                    cityText.setText(city);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }

}