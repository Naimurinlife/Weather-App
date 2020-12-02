package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    final String TAG = "MainActivity";

    TextView tCity, tFeelsLike, tWeather, tTemp, tTimeZone;
    ImageView tSearch;
    EditText tInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tCity = (TextView) findViewById(R.id.sCityName);
        tFeelsLike = (TextView) findViewById(R.id.sFeelLike);
        tWeather = (TextView) findViewById(R.id.sWeather);
        tTemp = (TextView) findViewById(R.id.sTemp);
        tTimeZone = (TextView) findViewById(R.id.sTimeZone);
        tSearch=(ImageView) findViewById(R.id.sSearch);
        tInput=(EditText) findViewById(R.id.sInput);

        tSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                String input = tInput.getText().toString();
                makeJSONRequest(input);
                Log.e(TAG, input);
            }
        });




    }

    private void makeJSONRequest(String cityName) {

        String tag_json_obj = "json_obj_req";

        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&" +
                "appid=6a1478102a6110ee43a0b40606644889";


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, response.toString());
                        try {
                            String name = response.getString("name");
                            Log.e("City Name", name);
                            tCity.setText(name);

                            //  mFeelLike, mTimezone, mWeather

                            JSONObject main = response.getJSONObject("main");
                            Double temp = main.getDouble("temp");
                            tTemp.setText(String.format("%.1f", temp -273)+ "°C");
                            Double feels_like = main.getDouble("feels_like");
                            tFeelsLike.setText(String.format("%.1f", feels_like -273)+ "°C");
                            Double temp_min = main.getDouble("temp_min");
                            Double temp_max = main.getDouble("temp_max");
                            Double pressure = main.getDouble("pressure");
                            Double humidity = main.getDouble("humidity");
                            long timezone = response.getLong("dt");
                            String dateTime = getDate(timezone *(long) 1000, "EEE, d MMM yyyy hh:mm:ss a");
                            tTimeZone.setText(dateTime);

                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject item = weatherArray.getJSONObject(0);
                            String weather = item.getString("main");
                            tWeather.setText(weather);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}