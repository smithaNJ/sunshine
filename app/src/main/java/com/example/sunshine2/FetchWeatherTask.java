package com.example.sunshine2;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
public class FetchWeatherTask extends AsyncTask<String,Void,String[]> {

    private final int NUM_DAYS=5;
    private String[] weatherDataForecast=new String[NUM_DAYS];
    private final String LOG_TAG=FetchWeatherTask.class.getSimpleName();
    protected String[] weatherDataParser(String jsonStr){
        final String OWM_LIST="list";
        final String OWM_MAIN="main";
        final String OWM_WEATHER="weather";
        final String TEMP_MIN="temp_min";
        final String TEMP_MAX="temp_max";
        final String PRESSURE="pressure";
        final String HUMIDITY="humidity";
        final String WIND="wind";
        final String DESCRIPTION="description";
        final String SPEED="speed";
        float tmin,tmax,d_humidity,d_pressure,d_speed;
        String d_weatherdescription;

        try {
            JSONObject weatherData=new JSONObject(jsonStr);
            for(int i=0;i<NUM_DAYS;i++) {
                JSONObject day = weatherData.getJSONArray(OWM_LIST).getJSONObject(i);
                JSONObject day_main=day.getJSONObject(OWM_MAIN);
                JSONArray day_weather=day.getJSONArray(OWM_WEATHER);
                JSONObject day_wind=day.getJSONObject(WIND);

                 tmin=(float)day_main.getDouble(TEMP_MIN);
                 tmax=(float)day_main.getDouble(TEMP_MAX);


                 d_pressure=(float)day_main.getDouble(PRESSURE);
                 d_humidity=(float)day_main.getDouble(HUMIDITY);

                d_weatherdescription=day_weather.getJSONObject(0).getString(DESCRIPTION);
                d_speed=(float)day_wind.getDouble(SPEED);

               weatherDataForecast[i]= "TempMin="+String.valueOf(tmin)+" -- TempMax="+String.valueOf(tmax)+" -- Description="+d_weatherdescription+" -- Humidity="+String.valueOf(d_humidity)+" -- Pressure="+String.valueOf(d_pressure)+" -- Speed="+String.valueOf(d_speed);
                Log.d(LOG_TAG,weatherDataForecast[i]);


            }

        }catch (JSONException e){
            Log.e("weatherDataParser","Error",e);

        }

        return weatherDataForecast;
    }
    @Override
    protected String[] doInBackground(String... strings) {
         final  String API_KEY="6f2241a533a39c62c586a9cf148730ab";
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http");
        builder.authority("api.openweathermap.org");
        builder.appendPath("data");
        builder.appendPath("2.5");
        builder.appendPath("forecast");
       // builder.appendPath("daily");
        builder.appendQueryParameter("id", strings[0]);
        builder.appendQueryParameter("mode", "json");
        builder.appendQueryParameter("units", "metric");
        builder.appendQueryParameter("cnt","5");
        builder.appendQueryParameter("appid", API_KEY);
        String stringurl = builder.build().toString();

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;


        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            URL url = new URL(stringurl);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder strBuilder = new StringBuilder();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                strBuilder.append(line + "\n");
            }

            if (strBuilder.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            forecastJsonStr = strBuilder.toString();
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }

     weatherDataForecast=weatherDataParser(forecastJsonStr);
        return weatherDataForecast;

    }

    @Override
    protected void onPostExecute(String[] s) {
        super.onPostExecute(s);
         if(s!=null){
          BlankFragment.arrayAdapter.clear();
             for (String forecast:s) {
                 BlankFragment.arrayAdapter.add(forecast);
             }

         }


    }
}
