package ai.atick.weather;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView celsius, fahrenheit, clock, temperature, forecast, weekDay;
    private LinearLayout location;
    private ImageView weatherIcon;

    private int temp_c, temp_f, hour, minute, hourOfDay, weatherID;
    private String currentTime, currentForecast, dayOfWeek, temp, am_pm;
    static String API_URL = "https://api.openweathermap.org/data/2.5/weather?q=Dhaka,BD&appid=85986e3adcf28b5eca7955b6edf7a67c";
    private boolean isCelsius = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /////////////////////////////////////////////
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.hide();
        }
        /////////////////////////////////////////////

        celsius = findViewById(R.id.celsius);
        fahrenheit = findViewById(R.id.fahrenheit);
        clock = findViewById(R.id.clock);
        temperature = findViewById(R.id.temperature);
        forecast = findViewById(R.id.forecast);
        weekDay = findViewById(R.id.week_day);
        location = findViewById(R.id.location);
        weatherIcon = findViewById(R.id.weather_icon);

        /////////////////////////////////////////////////////////////
        celsius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapValue();
            }
        });
        /////////////////////////////////////////////////////////////
        fahrenheit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapValue();
            }
        });
        /////////////////////////////////////////////////////////////

        _init();
        getTime();
        FetchWeatherData fetchWeatherData = new FetchWeatherData();
        fetchWeatherData.execute();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    void _init() {
        temp_c = 27;
        temp_f = 81;
        hour = 12;
        minute = 0;
        hourOfDay = 0;
        currentTime = "12:00 AM";
        currentForecast = "Rain";
        dayOfWeek = "Sunday";
        temp = "27°c";
        am_pm = "AM";

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void getTime() {
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        dayOfWeek = new DateFormatSymbols().getWeekdays()[calendar.get(Calendar.DAY_OF_WEEK)];
        if(hourOfDay > 11) {
            am_pm = "PM";
        } else {
            am_pm = "AM";
        }
        currentTime = String.format(Locale.getDefault(), "%d:%02d %s", hour, minute, am_pm);
        clock.setText(currentTime);
        weekDay.setText(dayOfWeek);
    }

    private int celsiusToFahrenheit(int c) {
        int f;
        f = (c*9)/5 + 32;
        return f;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void swapValue() {
        if(isCelsius) {
            temp = String.format(Locale.getDefault(), "%d°f", temp_f);
            temperature.setText(temp);
            celsius.setBackground(getResources().getDrawable(R.drawable.circle_gray));
            celsius.setTextColor(getResources().getColor(R.color.black));
            fahrenheit.setTextColor(getResources().getColor(R.color.white));
            fahrenheit.setBackground(getResources().getDrawable(R.drawable.circle_dark));
            isCelsius = false;
        } else {
            temp = String.format(Locale.getDefault(), "%d°c", temp_c);
            temperature.setText(temp);
            celsius.setBackground(getResources().getDrawable(R.drawable.circle_dark));
            celsius.setTextColor(getResources().getColor(R.color.white));
            fahrenheit.setTextColor(getResources().getColor(R.color.black));
            fahrenheit.setBackground(getResources().getDrawable(R.drawable.circle_gray));
            isCelsius = true;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressLint("StaticFieldLeak")
    private class FetchWeatherData extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            String update = "";
            try {
                URL url = new URL(API_URL);
                HttpURLConnection httpURLConnection = null;
                try {
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null)  {
                        stringBuilder.append(line).append("\n");
                    }
                    update = stringBuilder.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return update;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                //////////////////////////////////////////////////////////////////////
                JSONObject jsonObject1 = jsonObject.getJSONObject("main");
                temp_c = jsonObject1.getInt("temp") - 273;
                temp_f = celsiusToFahrenheit(temp_c);
                if(isCelsius) {
                    temp = String.format(Locale.getDefault(), "%d°c", temp_c);
                } else {
                    temp = String.format(Locale.getDefault(), "%d°f", temp_f);
                }
                temperature.setText(temp);
                //////////////////////////////////////////////////////////////////////
                JSONArray jsonArray = jsonObject.getJSONArray("weather");
                JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                currentForecast = jsonObject2.getString("main");
                forecast.setText(currentForecast);
                //////////////////////////////////////////////////////////////////////
                weatherID = jsonObject2.getInt("id");
                weatherID = weatherID/100;
                switch (weatherID){
                    case 2:
                        weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.tstorms));
                        break;
                    case 3:
                        weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.cloudy));
                        break;
                    case 5:
                        weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.rain));
                        break;
                    case 6:
                        weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.winter));
                        break;
                    case 7:
                        weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.hazy));
                        break;
                    case 8:
                        weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.mostlycloudy));
                        break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

}
