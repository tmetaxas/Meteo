package com.thomasmetaxas.meteo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.ImageView;
import com.android.volley.Request;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView dateText, cityText, temperatureText, descriptionText, feelsLikeText, lowText, highText, humidityText, windText;
    ImageView imageView;
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

        String pattern = "EEEEE MMMMM dd";
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        dateText.setText(date);

        afficher();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recherche, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setQueryHint("Ville");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                city = query;
                afficher();

                //Clavier
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void afficher() {
        cityText.setText(city);
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=4a496adeafb114fe32f0846ca97e4a09&units=metric";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    Log.d("Tag", "resultat =" + array.toString());
                    JSONObject object = array.getJSONObject(0);
                    String description = object.getString("description");
                    int temp = (int) Math.round(response.getJSONObject("main").getDouble("temp"));
                    int low = (int) Math.round(response.getJSONObject("main").getDouble("temp_min"));
                    int high = (int) Math.round(response.getJSONObject("main").getDouble("temp_max"));
                    int feelsLike = (int) Math.round(response.getJSONObject("main").getDouble("feels_like"));
                    String humidity = response.getJSONObject("main").getString("humidity");
                    String wind = response.getJSONObject("wind").getString("speed");
                    String city = response.getString("name");
                    descriptionText.setText(description);
                    temperatureText.setText(temp + "°C");
                    feelsLikeText.setText("Feels like: " + feelsLike + "°C");
                    lowText.setText("Low: " + low + "°C");
                    highText.setText("High: " + high + "°C");
                    humidityText.setText("Humidity: " + humidity + "%");
                    windText.setText("Wind: " + wind + "km/h");

                    String icon = object.getString("icon");
                    String image = "http://openweathermap.org/img/w/" + icon + ".png";
                    imageView = findViewById(R.id.imageView);
                    Uri uri = Uri.parse(image);
                    Picasso.with(MainActivity.this).load(uri).resize(200, 200).into(imageView);


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